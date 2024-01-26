
package com.weirddev.testme.intellij.ui.template;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import com.intellij.ide.fileTemplates.InternalTemplateBean;
import com.intellij.ide.fileTemplates.impl.CustomFileTemplate;
import com.intellij.ide.fileTemplates.impl.FileTemplateBase;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.components.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.openapi.project.DefaultProjectFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.project.ProjectKt;
import com.intellij.util.SystemProperties;
import com.intellij.util.text.DateFormatUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.weirddev.testme.intellij.template.TemplateDescriptor;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.template.TemplateRole;
import com.weirddev.testme.intellij.template.context.Language;
import com.weirddev.testme.intellij.template.context.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @see com.intellij.ide.fileTemplates.impl.FileTemplateManagerImpl
 */
@State(
        name="TestMeTemplateManager",
        storages = {
                @Storage("TestMeTemplateManager.xml")
        }
)
public class TestMeTemplateManager extends FileTemplateManager implements PersistentStateComponent<TestMeTemplateManager.State> {
  private static final Logger LOG = Logger.getInstance("#TestMeTemplateManager");
  public static final String TEST_TEMPLATES_CATEGORY = "Tests";
  private final State myState = new State();
//  private final FileTypeManagerEx myTypeManager;
//  private final FileTemplateSettings myProjectSettings;
  private final FileTemplateSettings myDefaultSettings;
  private final Project myProject;

  private final FileTemplatesScheme myProjectScheme;
  private FileTemplatesScheme myScheme = FileTemplatesScheme.DEFAULT;
  private boolean myInitialized;
  private final TemplateRegistry templateRegistry = new TemplateRegistry(); //todo consider making this a service

  public static TestMeTemplateManager getInstance(@NotNull Project project){
    return project.getService(TestMeTemplateManager.class);
  }

  public static TestMeTemplateManager getDefaultInstance(){
    return TestMeTemplateManager.getInstance(DefaultProjectFactory.getInstance().getDefaultProject());
  }

  TestMeTemplateManager(
//          @NotNull FileTypeManagerEx typeManager,
//                            FileTemplateSettings projectSettings,
//                            ExportableFileTemplateSettings defaultSettings,
                            final Project project) {
//    myTypeManager = typeManager;
//    myProjectSettings = projectSettings;
//    myDefaultSettings = defaultSettings;
//    myDefaultSettings = ApplicationManager.getApplication().getService(ExportableFileTemplateSettings.class);;
    myDefaultSettings = ApplicationManager.getApplication().getService(FileTemplateSettings.class);
    myProject = project;

    myProjectScheme = project !=null && project.isDefault() ? null : new FileTemplatesScheme("Project") {
      @NotNull
      @Override
      public String getTemplatesDir() {
          assert project != null;
          return FileUtilRt.toSystemDependentName(Objects.requireNonNull(ProjectKt.getStateStore(project).getDirectoryStorePath()).toFile().getPath() + "/" + TEMPLATES_DIR);
      }

      @NotNull
      @Override
      public Project getProject() {
        return project;
      }
    };
  }

  private FileTemplateSettings getSettings() {
      return myScheme == FileTemplatesScheme.DEFAULT ? myDefaultSettings : myProject.getService(FileTemplateSettings.class);
  }

  @NotNull
  @Override
  public FileTemplatesScheme getCurrentScheme() {
    return myScheme;
  }

  @Override
  public void setCurrentScheme(@NotNull FileTemplatesScheme scheme) {
    for (FTManager child : getAllManagers()) {
      child.saveTemplates();
    }
    setScheme(scheme);
  }

  private void setScheme(@NotNull FileTemplatesScheme scheme) {
    myScheme = scheme;
    myInitialized = true;
  }

  @NotNull
  @Override
  protected FileTemplateManager checkInitialized() {
    if (!myInitialized) {
      // loadState() not called; init default scheme
      setScheme(myScheme);
    }
    return this;
  }

  @Nullable
  @Override
  public FileTemplatesScheme getProjectScheme() {
    return myProjectScheme;
  }

  @NotNull
  @Override
  public FileTemplate[] getTemplates(@NotNull String category) {
    if (TestMeTemplateManager.TEST_TEMPLATES_CATEGORY.equals(category)) return getInternalTemplates();
    if (INCLUDES_TEMPLATES_CATEGORY.equals(category)) return getAllPatterns();
    throw new IllegalArgumentException("Unknown category: " + category);
  }

  @Override
  @NotNull
  public FileTemplate[] getAllTemplates() {
//    final Collection<FileTemplateBase> templates = getSettings().getDefaultTemplatesManager().getAllTemplates(false);
//    return templates.toArray(FileTemplate.EMPTY_ARRAY);
    return FileTemplate.EMPTY_ARRAY;
  }

  @Override
  public FileTemplate getTemplate(@NotNull String templateName) {
//    return getSettings().getDefaultTemplatesManager().findTemplateByName(templateName);
    return null;
  }

  @Override
  @NotNull
  public FileTemplate addTemplate(@NotNull String name, @NotNull String extension) {
//    return getSettings().getDefaultTemplatesManager().addTemplate(name, extension);
    return null;
  }

  @Override
  public void removeTemplate(@NotNull FileTemplate template) { // exists for testing onlu
//    final String qName = ((FileTemplateBase)template).getQualifiedName();
//    for (FTManager manager : getAllManagers()) {
//      manager.removeTemplate(qName);
//    }
  }

  @Override
  @NotNull
  public Properties getDefaultProperties() {
    @NonNls Properties props = new Properties();

    Calendar calendar = Calendar.getInstance();
    Date date = myTestDate == null ? calendar.getTime() : myTestDate;
    SimpleDateFormat sdfMonthNameShort = new SimpleDateFormat("MMM");
    SimpleDateFormat sdfMonthNameFull = new SimpleDateFormat("MMMM");
    SimpleDateFormat sdfDayNameShort = new SimpleDateFormat("EEE");
    SimpleDateFormat sdfDayNameFull = new SimpleDateFormat("EEEE");
    SimpleDateFormat sdfYearFull = new SimpleDateFormat("yyyy");

    props.setProperty("DATE", DateFormatUtil.formatDate(date));
    props.setProperty("TIME", DateFormatUtil.formatTime(date));
    props.setProperty("YEAR", sdfYearFull.format(date));
    props.setProperty("MONTH", getCalendarValue(calendar, Calendar.MONTH));
    props.setProperty("MONTH_NAME_SHORT", sdfMonthNameShort.format(date));
    props.setProperty("MONTH_NAME_FULL", sdfMonthNameFull.format(date));
    props.setProperty("DAY", getCalendarValue(calendar, Calendar.DAY_OF_MONTH));
    props.setProperty("DAY_NAME_SHORT", sdfDayNameShort.format(date));
    props.setProperty("DAY_NAME_FULL", sdfDayNameFull.format(date));
    props.setProperty("HOUR", getCalendarValue(calendar, Calendar.HOUR_OF_DAY));
    props.setProperty("MINUTE", getCalendarValue(calendar, Calendar.MINUTE));
    props.setProperty("SECOND", getCalendarValue(calendar, Calendar.SECOND));

    props.setProperty("USER", SystemProperties.getUserName());
    props.setProperty("PRODUCT_NAME", ApplicationNamesInfo.getInstance().getFullProductName());

    props.setProperty("DS", "$"); // Dollar sign, strongly needed for PHP, JS, etc. See WI-8979

    props.setProperty(PROJECT_NAME_VARIABLE, myProject.getName());

    return props;
  }

  @NotNull
  private static String getCalendarValue(final Calendar calendar, final int field) {
    int val = calendar.get(field);
    if (field == Calendar.MONTH) val++;
    final String result = Integer.toString(val);
    if (result.length() == 1) {
      return "0" + result;
    }
    return result;
  }

  @Override
  @NotNull
  public Collection<String> getRecentNames() {
//    validateRecentNames(); // todo: no need to do it lazily
    return myState.getRecentNames();
  }

  @Override
  public void addRecentName(@NotNull @NonNls String name) {
    myState.addName(name);
  }

  @Override
  @NotNull
  public FileTemplate[] getInternalTemplates() {
    final Collection<FileTemplateBase> allTemplates = getSettings().getInternalTestTemplatesManager().getAllTemplates(true);
    return allTemplates.toArray(FileTemplate.EMPTY_ARRAY);
  }
  @NotNull
  public List<TemplateDescriptor> getTestTemplates() {
    final Collection<FileTemplateBase> allTemplates = getSettings().getInternalTestTemplatesManager().getAllTemplates(true);
    Map<String, List<TemplateDescriptor>> oobEnabledTemplates = templateRegistry.getEnabledTemplateDescriptors().stream().collect(Collectors.groupingBy(TemplateDescriptor::getFilename));
    return allTemplates.stream()
            .filter(t -> t instanceof CustomFileTemplate || oobEnabledTemplates.containsKey(t.getQualifiedName()))
            .map(t -> Optional.ofNullable(oobEnabledTemplates.get(t.getQualifiedName())).map(l->l.get(0)).orElse(new TemplateDescriptor(t.getName(),t.getName(),t.getQualifiedName(), resolveLanguage(t), TemplateRole.Tester)) )
            .collect(Collectors.toList());
  }

  @NotNull
  private Language resolveLanguage(FileTemplateBase t) {
    try {
      return Language.valueOf(StringUtils.capitalizeFirstLetter(t.getExtension().toLowerCase()));
    } catch (IllegalArgumentException ignore) {
        return Language.Java;
    }
  }

  //  @NotNull
  @Override
  public FileTemplate getInternalTemplate(@NotNull @NonNls String templateName) {
    return findInternalTemplate(templateName);
  }

  @Override
  public FileTemplate findInternalTemplate(@NotNull @NonNls String templateName) {
    return getSettings().getInternalTestTemplatesManager().findTemplateByName(templateName);
  }

  public FileTemplate findCustomTestTemplate(@NotNull @NonNls String templateName) { //todo hookup
    return getSettings().getCustomTestTemplatesManager().findTemplateByName(templateName);
  }

  @Override
  @NotNull
  public String internalTemplateToSubject(@NotNull @NonNls String templateName) {
    for(InternalTemplateBean bean: InternalTemplateBean.EP_NAME.getExtensionList()) {
      if (bean.name.equals(templateName) && bean.subject != null) {
        return bean.subject;
      }
    }
    return templateName.toLowerCase();
  }

  @NotNull
  @Override
  public FileTemplate getCodeTemplate(@NotNull @NonNls String templateName) {
//    return getTemplateFromManager(templateName, getSettings().getCodeTemplatesManager());
    return null;
  }

  @NotNull
  @Override
  public FileTemplate getJ2eeTemplate(@NotNull @NonNls String templateName) {
//    return getTemplateFromManager(templateName, getSettings().getJ2eeTemplatesManager());
    return null;
  }

  @NotNull
  private static FileTemplate getTemplateFromManager(@NotNull final String templateName, @NotNull final FTManager ftManager) {
    FileTemplateBase template = ftManager.getTemplate(templateName);
    if (template != null) {
      return template;
    }
    template = ftManager.findTemplateByName(templateName);
    if (template != null) {
      return template;
    }

    String message = "Template not found: " + templateName/*ftManager.templateNotFoundMessage(templateName)*/;
    LOG.error(message);
    throw new IllegalStateException(message);
  }

  @Override
  @NotNull
  public FileTemplate getDefaultTemplate(@NotNull final String name) {
    //todo check if needed since default cannot be customized ( only copied)
    final String templateQName = getQualifiedName(name);
    for (FTManager manager : getAllManagers()) {
      FileTemplateBase template = manager.getTemplate(templateQName);
      if (template != null) {
//        if (template instanceof BundledFileTemplate) {
//          template = ((BundledFileTemplate)template).clone();
//          ((BundledFileTemplate)template).revertToDefaults();
//        }
        return template;
      }
    }

    String message = "Default template not found: " + name;
    LOG.error(message);
    throw new RuntimeException(message);
  }

  @Override
  public void setTemplates(@NotNull String templatesCategory, @NotNull Collection<? extends FileTemplate> templates) {
    for (FTManager manager : getAllManagers()) {
      if (templatesCategory.equals(manager.getName())) {
        manager.updateTemplates(templates);
        break;
      }
    }
  }

  @NotNull
  private String getQualifiedName(@NotNull String name) {
    return FileTypeManagerEx.getInstanceEx().getExtension(name).isEmpty() ? FileTemplateBase.getQualifiedName(name, "java") : name;
  }

  @Override
  @NotNull
  public FileTemplate[] getAllPatterns() {
    final Collection<FileTemplateBase> allTemplates = getSettings().getPatternsManager().getAllTemplates(false);
    return allTemplates.toArray(FileTemplate.EMPTY_ARRAY);
  }

  @Override
  public FileTemplate getPattern(@NotNull String name) {
    return getSettings().getPatternsManager().findTemplateByName(name);
  }

  @Override
  @NotNull
  public FileTemplate[] getAllCodeTemplates() {
//    final Collection<FileTemplateBase> templates = getSettings().getCodeTemplatesManager().getAllTemplates(false);
//    return templates.toArray(FileTemplate.EMPTY_ARRAY);
    return FileTemplate.EMPTY_ARRAY;
  }

  @Override
  @NotNull
  public FileTemplate[] getAllJ2eeTemplates() {
//    final Collection<FileTemplateBase> templates = getSettings().getJ2eeTemplatesManager().getAllTemplates(false);
//    return templates.toArray(FileTemplate.EMPTY_ARRAY);
    return FileTemplate.EMPTY_ARRAY;
  }

//  @Override
//  public void setTemplates(@NotNull String templatesCategory, @NotNull Collection<FileTemplate> templates) {
//    for (FTManager manager : getAllManagers()) {
//      if (templatesCategory.equals(manager.getName())) {
//        manager.updateTemplates(templates);
//        break;
//      }
//    }
//  }

  @Override
  public void saveAllTemplates() {
    for (FTManager manager : getAllManagers()) {
      manager.saveTemplates();
    }
  }

  public URL getDefaultTemplateDescription() {
    return myDefaultSettings.getDefaultTemplateDescription();
  }

  URL getDefaultIncludeDescription() {
    return myDefaultSettings.getDefaultIncludeDescription();
  }

  private Date myTestDate;

  @TestOnly
  public void setTestDate(Date testDate) {
    myTestDate = testDate;
  }

  @Nullable
  @Override
  public State getState() {
    myState.SCHEME = myScheme.getName();
    return myState;
  }

  @Override
  public void loadState(@NotNull State state) {
    XmlSerializerUtil.copyBean(state, myState);
    FileTemplatesScheme scheme = myProjectScheme != null && myProjectScheme.getName().equals(state.SCHEME) ? myProjectScheme : FileTemplatesScheme.DEFAULT;
    setScheme(scheme);
  }

  private FTManager[] getAllManagers() {
    return getSettings().getAllManagers();
  }

//  @TestOnly
//  public void setDefaultFileIncludeTemplateTextTemporarilyForTest(String simpleName, String text, @NotNull Disposable parentDisposable) {
//    FTManager defaultTemplatesManager = getSettings().getPatternsManager();
//    String qName = getQualifiedName(simpleName);
//    FileTemplateBase oldTemplate = defaultTemplatesManager.getTemplate(qName);
//    Map<String, FileTemplateBase> templates = defaultTemplatesManager.getTemplates();
//    templates.put(qName, new FileTemplateBase() {
//      @NotNull
//      @Override
//      public String getName() {
//        return simpleName;
//      }
//
//      @Override
//      public void setName(@NotNull String name) {
//        throw new AbstractMethodError();
//      }
//
//      @Override
//      public boolean isDefault() {
//        return true;
//      }
//
//      @NotNull
//      @Override
//      public String getDescription() {
//        throw new AbstractMethodError();
//      }
//
//      @NotNull
//      @Override
//      public String getExtension() {
//        return qName.substring(simpleName.length());
//      }
//
//      @Override
//      public void setExtension(@NotNull String extension) {
//        throw new AbstractMethodError();
//      }
//
//      @NotNull
//      @Override
//      protected String getDefaultText() {
//        return text;
//      }
//    });
//    Disposer.register(parentDisposable, () -> templates.put(qName, oldTemplate));
//  }

  public static class State {
    public List<String> RECENT_TEMPLATES = new ArrayList<>();
    public String SCHEME = FileTemplatesScheme.DEFAULT.getName();

    public void addName(@NotNull @NonNls String name) {
      RECENT_TEMPLATES.remove(name);
      RECENT_TEMPLATES.add(name);
    }

    @NotNull
    Collection<String> getRecentNames() {
      int size = RECENT_TEMPLATES.size();
      int resultSize = Math.min(FileTemplateManager.RECENT_TEMPLATES_SIZE, size);
      return RECENT_TEMPLATES.subList(size - resultSize, size);
    }

    void validateNames(List<String> validNames) {
      RECENT_TEMPLATES.retainAll(validNames);
    }
  }
}
