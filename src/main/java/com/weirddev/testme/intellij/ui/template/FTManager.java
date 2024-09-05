package com.weirddev.testme.intellij.ui.template;

import com.intellij.application.options.CodeStyle;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.impl.CustomFileTemplate;
import com.intellij.ide.fileTemplates.impl.DefaultTemplate;
import com.intellij.ide.fileTemplates.impl.FileTemplateBase;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtilRt;
import com.intellij.util.io.PathKt;
import com.weirddev.testme.intellij.template.TemplateDescriptor;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.ui.model.TestMeFileTemplate;
import gnu.trove.THashMap;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Eugene Zhuravlev
 */
public class FTManager {
  private static final Logger LOG = Logger.getInstance("#FTManager");
  private static final String DEFAULT_TEMPLATE_EXTENSION = "ft";
  static final String TEMPLATE_EXTENSION_SUFFIX = "." + DEFAULT_TEMPLATE_EXTENSION;
  private static final String ENCODED_NAME_EXT_DELIMITER = "\u0F0Fext\u0F0F.";

  private final String myName;
  private final boolean myInternal;
  private final Path myTemplatesDir;
  @Nullable
  private final FTManager myOriginal;
  private final Map<String, FileTemplateBase> myTemplates = new HashMap<>();
  private volatile List<FileTemplateBase> mySortedTemplates;
  private final List<DefaultTemplate> myDefaultTemplates = new ArrayList<>();
  private final TemplateRegistry templateRegistry = new TemplateRegistry(); //todo consider making this a service

  FTManager(@NotNull @NonNls String name, @NotNull @NonNls Path defaultTemplatesDirName) {
    this(name, defaultTemplatesDirName, false);
  }

  FTManager(@NotNull @NonNls String name, @NotNull @NonNls Path defaultTemplatesDirName, boolean internal) {
    myName = name;
    myInternal = internal;
    myTemplatesDir = defaultTemplatesDirName;
    myOriginal = null;
  }

  FTManager(@NotNull FTManager original) {
    myOriginal = original;
    myName = original.getName();
    myTemplatesDir = original.myTemplatesDir;
    myInternal = original.myInternal;
    myTemplates.putAll(original.myTemplates);
    myDefaultTemplates.addAll(original.myDefaultTemplates);
  }

  @NotNull
  public String getName() {
    return myName;
  }

  @NotNull
  Collection<FileTemplateBase> getAllTemplates(boolean includeDisabled) {
    List<FileTemplateBase> sorted = mySortedTemplates;
    if (sorted == null) {
      sorted = sortTemplates();
      mySortedTemplates = sorted;
    }
//    return filterDisabled(includeDisabled, sorted);
    return sorted;
  }

  /**
   * sort OOB templates first. keep order as listed by TemplateRegistry
   */
  @NotNull
  private List<FileTemplateBase> sortTemplates() {
    List<FileTemplateBase> defaultTemplates = getTemplates().values().stream().filter(FileTemplate::isDefault).collect(Collectors.toList());
    sortOobTemplates(defaultTemplates);
    List<FileTemplateBase> customTemplates = getTemplates().values().stream().filter(fileTemplateBase -> !fileTemplateBase.isDefault()).collect(Collectors.toList());
    sort(customTemplates);
    ArrayList<FileTemplateBase> templates = new ArrayList<>();
    templates.addAll(customTemplates);
    templates.addAll(defaultTemplates);
    return templates;
  }

  private void sortOobTemplates(List<FileTemplateBase> oobTemplates) {
    List<String> sortedOobTemplateNames = templateRegistry.getEnabledTemplateDescriptors().stream().map(TemplateDescriptor::getFilename).collect(Collectors.toList());
    oobTemplates.sort(Comparator.comparing(o -> indexOf(sortedOobTemplateNames, o)));
  }

  private Integer indexOf(List<String> sortedOobTemplateNames, FileTemplateBase template) {
    return sortedOobTemplateNames.indexOf(template.getQualifiedName());
  }

  private void sort(List<FileTemplateBase> sorted) {
    sorted.sort((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()));
  }

  /**
   * @return template no matter enabled or disabled it is
   */
  @Nullable
  FileTemplateBase getTemplate(@NotNull String templateQname) {
    return getTemplates().get(templateQname);
  }

  /**
   * Disabled templates are never returned
   */
  @Nullable
  public FileTemplateBase findTemplateByName(@NotNull String templateName) {
    final FileTemplateBase template = getTemplates().get(templateName);
    if (template != null) {
//      final boolean isEnabled = !(template instanceof BundledFileTemplate) || ((BundledFileTemplate)template).isEnabled();
//      if (isEnabled) {
//      }
      return template;
    }
    // templateName must be non-qualified name, since previous lookup found nothing
    for (FileTemplateBase t : getAllTemplates(false)) {
      final String qName = t.getQualifiedName();
      if (qName.startsWith(templateName) && qName.length() > templateName.length()) {
        String remainder = qName.substring(templateName.length());
        if (remainder.startsWith(ENCODED_NAME_EXT_DELIMITER) || remainder.charAt(0) == '.') {
          return t;
        }
      }
    }
    return null;
  }

  @NotNull
  public FileTemplateBase addTemplate(@NotNull String name, @NotNull String extension) {
    final String qName = FileTemplateBase.getQualifiedName(name, extension);
    FileTemplateBase template = getTemplate(qName);
    if (template == null) {
      //todo consider formatted html name for custom test templates
//      TestMeFileTemplate tTemplate =new TestMeFileTemplate(name, extension, false);
//      tTemplate.setDisplayName(TemplateFileNameFormatter.filenameToTemplateName(name));
//      template = tTemplate;
      template = new CustomFileTemplate(name, extension);
      getTemplates().put(qName, template);
      mySortedTemplates = null;
    }
    return template;
  }

  void updateTemplates(@NotNull Collection<? extends FileTemplate> newTemplates) {
    restoreDefaults();
    for (FileTemplate template : newTemplates) {
      final FileTemplateBase _template = addTemplate(template.getName(), template.getExtension());
      _template.setText(template.getText());
      _template.setReformatCode(template.isReformatCode());
      _template.setLiveTemplateEnabled(template.isLiveTemplateEnabled());
    }
    saveTemplates(true);
  }

  private void restoreDefaults() {
    getTemplates().clear();
    mySortedTemplates = null;
    for (DefaultTemplate template : myDefaultTemplates) {
    createAndStoreBundledTemplate(template);
    }
  }
  void setDefaultTemplates(@NotNull Collection<DefaultTemplate> templates) {
    myDefaultTemplates.clear();
    myDefaultTemplates.addAll(templates);
    for (DefaultTemplate template : templates) {
      createAndStoreBundledTemplate(template);
    }
  }

  @NotNull
  private TestMeFileTemplate createAndStoreBundledTemplate(@NotNull DefaultTemplate template) {
    TestMeFileTemplate testMeFileTemplate = new TestMeFileTemplate(template.getName(), template.getExtension(), true);
    testMeFileTemplate.setText(template.getText());
    testMeFileTemplate.setDescription(template.getDescriptionText());
    Optional<String> optHtmlDisplayName = templateRegistry.getEnabledTemplateDescriptors().stream().filter(t -> t.getFilename().equals(template.getName()+"."+template.getExtension())).findAny().map(TemplateDescriptor::getHtmlDisplayName);
    optHtmlDisplayName.ifPresent(testMeFileTemplate::setDisplayName);
    final String qName = testMeFileTemplate.getQualifiedName();
    final FileTemplateBase previous = getTemplates().put(qName, testMeFileTemplate);
    mySortedTemplates = null;

    LOG.assertTrue(previous == null, "Duplicate bundled template " + qName +
                                     " [" + template.getName() + ", " + previous + ']');
    return testMeFileTemplate;
  }

  void loadCustomizedContent() {
    final List<Path> templateWithDefaultExtension = new ArrayList<>();
    final Set<String> processedNames = new THashSet<>();
    try(DirectoryStream<Path> stream = Files.newDirectoryStream(getConfigRoot(), file -> !Files.isDirectory(file) && !Files.isHidden(file))) {
      for (Path file : stream) {
        String fileName = file.getFileName().toString();
        // check it here and not in filter to reuse fileName
        if (FileTypeManager.getInstance().isFileIgnored(fileName)) {
          continue;
        }

        if (fileName.endsWith(TEMPLATE_EXTENSION_SUFFIX)) {
          templateWithDefaultExtension.add(file);
        }
        else {
          processedNames.add(fileName);
          addTemplateFromFile(fileName, file);
        }
      }
    }
    catch (NoSuchFileException ignored) {
    }
    catch (IOException e) {
      LOG.error(e);
      return;
    }

    for (Path file : templateWithDefaultExtension) {
      String name = file.getFileName().toString();
      // cut default template extension
      name = name.substring(0, name.length() - TEMPLATE_EXTENSION_SUFFIX.length());
      if (!processedNames.contains(name)) {
        addTemplateFromFile(name, file);
      }

      try {
        Files.delete(file);
      }
      catch (IOException e) {
        LOG.error(e);
      }
    }
  }

  private void addTemplateFromFile(@NotNull String fileName, @NotNull Path file) {
    Pair<String,String> nameExt = decodeFileName(fileName);
    final String extension = nameExt.second;
    final String templateQName = nameExt.first;
    if (templateQName.isEmpty()) {
      return;
    }
    try {
      addTemplate(templateQName, extension).setText(PathKt.readText(file));
    }
    catch (IOException e) {
      LOG.error(e);
    }
  }

  public void saveTemplates() {
    saveTemplates(false);
  }

  private void saveTemplates(boolean removeDeleted) {
    final Set<String> allNames = new THashSet<>();
    final Path configRoot = getConfigRoot();
    final Map<String, Path> templatesOnDisk = new THashMap<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(getConfigRoot(), file -> !Files.isDirectory(file) && !Files.isHidden(file))) {
      for (Path file : stream) {
        String fileName = file.getFileName().toString();//todo if under testMeTests -> decode html
        templatesOnDisk.put(fileName, file);
        allNames.add(fileName);
      }
    }
    catch (NoSuchFileException ignored) {
    }
    catch (IOException e) {
      LOG.error(e);
    }

    final Map<String, FileTemplateBase> templatesToSave = new THashMap<>();

    for (FileTemplateBase template : getAllTemplates(true)) {
      final String name = template.getQualifiedName();
      if (template instanceof CustomFileTemplate) {
        templatesToSave.put(name, template);
        allNames.add(name);
      }
    }

    if (allNames.isEmpty()) {
      return;
    }

    try {
      Files.createDirectories(myTemplatesDir);
    }
    catch (IOException e) {
      LOG.info("Cannot create directory: " + myTemplatesDir);
    }

    final String lineSeparator = CodeStyle.getDefaultSettings().getLineSeparator();
    for (String name : allNames) {
      final Path customizedTemplateFile = templatesOnDisk.get(name);
      final FileTemplateBase templateToSave = templatesToSave.get(name);
      if (customizedTemplateFile == null) {
        // template was not saved before
        try {
          saveTemplate(configRoot, templateToSave, lineSeparator);
        }
        catch (IOException e) {
          LOG.error("Unable to save template " + name, e);
        }
      }
      else if (templateToSave == null) {
        // template was removed
        if (removeDeleted) {
          try {
            Files.delete(customizedTemplateFile);
          }
          catch (IOException e) {
            LOG.error(e);
          }
        }
      }
      else {
        // both customized content on disk and corresponding template are present
        try {
          final String diskText = StringUtilRt.convertLineSeparators(PathKt.readText(customizedTemplateFile));
          final String templateText = templateToSave.getText();
          if (!diskText.equals(templateText)) {
            // save only if texts differ to avoid unnecessary file touching
            saveTemplate(configRoot, templateToSave, lineSeparator);
          }
        }
        catch (IOException e) {
          LOG.error("Unable to save template " + name, e);
        }
      }
    }
  }

  /** Save template to file. If template is new, it is saved to specified directory. Otherwise it is saved to file from which it was read.
   *  If template was not modified, it is not saved.
   */
  private static void saveTemplate(@NotNull Path parentDir, @NotNull FileTemplateBase template, @NotNull String lineSeparator) throws IOException {

    String name = template.getName();
    String extension = template.getExtension();
    String extSuffix = "." + extension;
    if (name.endsWith(extSuffix)) {
      name = name.substring(0, name.length() - (extSuffix).length());
    }
// avoid encoding html for now ( high risks for max file path >255 issues on win. need to persist html name in conf files instead)
//    String fileName = parentDir.endsWith(FileTemplatesLoader.INCLUDES_DIR)? name: TemplateFileNameFormatter.templateNameToFile(name);
    String fileName = name;
    final Path templateFile = parentDir.resolve(encodeFileName(fileName, extension));
    try (OutputStream fileOutputStream = startWriteOrCreate(templateFile);
         OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8)) {
      String content = template.getText();
      if (!lineSeparator.equals("\n")) {
        content = StringUtilRt.convertLineSeparators(content, lineSeparator);
      }
      outputStreamWriter.write(content);
    }
  }

  @NotNull
  private static OutputStream startWriteOrCreate(@NotNull Path templateFile) throws IOException {
    try {
      return Files.newOutputStream(templateFile);
    }
    catch (NoSuchFileException e) {
      // try to recover from the situation 'file exists, but is a directory'
      PathKt.delete(templateFile);
      return Files.newOutputStream(templateFile);
    }
  }

  @NotNull
  Path getConfigRoot() {
    return myTemplatesDir;
  }

  @Override
  public String toString() {
    return myName + " file template manager";
  }

  @NotNull
  public static String encodeFileName(@NotNull String templateName, @NotNull String extension) {
    String nameExtDelimiter = extension.contains(".") ? ENCODED_NAME_EXT_DELIMITER : ".";
    return templateName + nameExtDelimiter + extension;
  }

  @NotNull
  private static Pair<String,String> decodeFileName(@NotNull String fileName) {
    String name = fileName;
    String ext = "";
    String nameExtDelimiter = fileName.contains(ENCODED_NAME_EXT_DELIMITER) ? ENCODED_NAME_EXT_DELIMITER : ".";
    int extIndex = fileName.lastIndexOf(nameExtDelimiter);
    if (extIndex >= 0) {
      name = fileName.substring(0, extIndex);
      ext = fileName.substring(extIndex + nameExtDelimiter.length());
    }
    return Pair.create(name, ext);
  }

  @NotNull
  public Map<String, FileTemplateBase> getTemplates() {
    return myOriginal != null ? myOriginal.myTemplates : myTemplates;
  }
}
