
package com.weirddev.testme.intellij.ui.template;

import com.intellij.CommonBundle;
import com.intellij.application.options.schemes.AbstractSchemeActions;
import com.intellij.application.options.schemes.SchemesModel;
import com.intellij.application.options.schemes.SimpleSchemesPanel;
import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ide.fileTemplates.FileTemplatesScheme;
import com.intellij.ide.fileTemplates.impl.BundledFileTemplate;
import com.intellij.ide.fileTemplates.impl.CustomFileTemplate;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.lang.IdeLanguageCustomization;
import com.intellij.lang.Language;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.options.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.TabbedPaneWrapper;
import com.intellij.ui.border.CustomLineBorder;
import com.intellij.util.PlatformIcons;
import com.intellij.util.PlatformUtils;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.JBIterable;
import com.intellij.util.ui.JBUI;
import com.weirddev.testme.intellij.TestMeBundle;
import com.weirddev.testme.intellij.icon.TemplateNameFormatter;
import com.weirddev.testme.intellij.template.TemplateDescriptor;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.template.TemplateRole;
import com.weirddev.testme.intellij.ui.model.TestMeFileTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TestTemplatesConfigurable implements SearchableConfigurable, Configurable.NoMargin, Configurable.NoScroll, Configurable.VariableProjectAppLevel {

  private static final Logger LOG = Logger.getInstance(TestTemplatesConfigurable.class);

  private static final String TEMPLATES_TITLE = TestMeBundle.message("testMe.settings.templates.tab.tests");
  private static final String INCLUDES_TITLE = TestMeBundle.message("testMe.settings.templates.tab.includes");

  private TemplateNameFormatter templateNameFormatter;

  final static class Provider extends ConfigurableProvider {
    private final Project myProject;

    Provider(@NotNull Project project) {
      myProject = project;
    }

    @NotNull
    @Override
    public Configurable createConfigurable() {
      return new TestTemplatesConfigurable(myProject);
    }

    @Override
    public boolean canCreateConfigurable() {
      return !PlatformUtils.isDataGrip();
    }
  }

  private final Project myProject;
  private final FileTemplateManager myManager;
  private JPanel myMainPanel;
  private FileTemplateTab myCurrentTab;
  private FileTemplateTab myTemplatesList;
  private FileTemplateTab myIncludesList;
  private JComponent myToolBar;
  private TabbedPaneWrapper myTabbedPane;
  private FileTemplateConfigurable myEditor;
  private boolean myModified;
  private JComponent myEditorComponent;
  private JPanel myLeftPanel;
  private FileTemplateTab[] myTabs;
  private Disposable myUIDisposable;
  private final Set<String> myInternalTemplateNames;

  private FileTemplatesScheme myScheme;
  private final Map<FileTemplatesScheme, Map<String, FileTemplate[]>> myChangesCache = new HashMap<>();

  private final TemplateRegistry templateRegistry;

  private static final String CURRENT_TAB = "FileTemplates.CurrentTab";
  private static final String SELECTED_TEMPLATE = "FileTemplates.SelectedTemplate";

  public TestTemplatesConfigurable(Project project) {
    myProject = project;
    myManager = TestMeTemplateManagerImpl.getInstance(project);
    myScheme = myManager.getCurrentScheme();
    myInternalTemplateNames = ContainerUtil.map2Set(myManager.getInternalTemplates(), FileTemplate::getName);
    templateRegistry = new TemplateRegistry(); //todo consider making this a service
    templateNameFormatter = new TemplateNameFormatter();
  }

  private void onRemove() {
    myCurrentTab.removeSelected();
    myModified = true;
  }

  private void onAdd() {
    String ext = JBIterable.from(IdeLanguageCustomization.getInstance().getPrimaryIdeLanguages())
            .filterMap(Language::getAssociatedFileType)
            .filterMap(FileType::getDefaultExtension)
            .first();
    createTemplate(IdeBundle.message("template.unnamed"), StringUtil.notNullize(ext, "txt"), "");
  }

  @NotNull
  FileTemplate createTemplate(@NotNull final String prefName, @NotNull final String extension, @NotNull final String content) {
    final FileTemplate[] templates = myCurrentTab.getTemplates();
    final FileTemplate newTemplate = FileTemplateUtil.createTemplate(prefName, extension, content, templates);
    myCurrentTab.addTemplate(newTemplate);
    myModified = true;
    myCurrentTab.selectTemplate(newTemplate);
    fireListChanged();
    myEditor.focusToNameField();
    return newTemplate;
  }

  private void onClone() {
    try {
      myEditor.apply();
    }
    catch (ConfigurationException ignore) {
    }

    final FileTemplate selected = myCurrentTab.getSelectedTemplate();
    if (selected == null) {
      return;
    }

    final FileTemplate[] templates = myCurrentTab.getTemplates();
    final Set<String> names = new HashSet<>();
    for (FileTemplate template : templates) {
      names.add(template.getName());
    }
//    @SuppressWarnings("UnresolvedPropertyKey")
//    final String nameTemplate = IdeBundle.message("template.copy.N.of.T");
//
//    String name = MessageFormat.format(nameTemplate, "", selected.getName());
    String  name = templateNameFormatter.formatClonedName(selected.getName(), "Copy of ");
    int i = 0;
    while (names.contains(name)) {
      name = templateNameFormatter.formatClonedName(selected.getName(), "Copy "+ (++i)+" of ");
    }
    final FileTemplate newTemplate = new CustomFileTemplate(name, selected.getExtension());
    newTemplate.setText(selected.getText());
    newTemplate.setReformatCode(selected.isReformatCode());
    newTemplate.setLiveTemplateEnabled(selected.isLiveTemplateEnabled());
    myCurrentTab.addTemplate(newTemplate);
    myModified = true;
    myCurrentTab.selectTemplate(newTemplate);
    fireListChanged();
  }

  @Override
  public String getDisplayName() {
    return TestMeBundle.message("testMe.title.file.templates");

  }

  @Override
  public String getHelpTopic() {
    int index = myTabbedPane.getSelectedIndex();
    switch (index) {
      case 0:
        return "fileTemplates.templates";
      case 1:
        return "fileTemplates.includes";
      default:
        throw new IllegalStateException("wrong index: " + index);
    }
  }

  @Override
  public JComponent createComponent() {
    myUIDisposable = Disposer.newDisposable();

    myTemplatesList = new FileTemplateTabAsList(TEMPLATES_TITLE) {
      @Override
      public void onTemplateSelected() {
        onListSelectionChanged();
      }
    };
    myIncludesList = new FileTemplateTabAsList(INCLUDES_TITLE) {
      @Override
      public void onTemplateSelected() {
        onListSelectionChanged();
      }
    };
    myCurrentTab = myTemplatesList;

    final List<FileTemplateTab> allTabs = new ArrayList<>(Arrays.asList(myTemplatesList, myIncludesList/*, myCodeTemplatesList*/));

    myEditor = new FileTemplateConfigurable(myProject);
    myEditor.addChangeListener(__ -> onEditorChanged());
    myEditorComponent = myEditor.createComponent();
    myEditorComponent.setBorder(JBUI.Borders.empty(10, 0, 10, 10));

    myTabs = allTabs.toArray(new FileTemplateTab[0]);
    myTabbedPane = new TabbedPaneWrapper(myUIDisposable);
    myTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    myLeftPanel = new JPanel(new CardLayout());
    for (FileTemplateTab tab : myTabs) {
      myLeftPanel.add(ScrollPaneFactory.createScrollPane(tab.getComponent()), tab.getTitle());
      JPanel fakePanel = new JPanel();
      fakePanel.setPreferredSize(new Dimension(0, 0));
      myTabbedPane.addTab(tab.getTitle(), fakePanel);
    }

    myTabbedPane.addChangeListener(__ -> onTabChanged());

    DefaultActionGroup group = new DefaultActionGroup();
    AnAction removeAction = new AnAction(TestMeBundle.message("testMe.settings.templates.remove.template"), null, AllIcons.General.Remove) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        onRemove();
      }

      @Override
      public void update(@NotNull AnActionEvent e) {
        super.update(e);
        if (myCurrentTab == null) {
          e.getPresentation().setEnabled(false);
          return;
        }
        FileTemplate selectedItem = myCurrentTab.getSelectedTemplate();
        e.getPresentation().setEnabled(selectedItem != null && !isInternalTemplate(selectedItem));
      }
    };
    AnAction addAction = new AnAction(TestMeBundle.message("testMe.settings.templates.create.template"), null, AllIcons.General.Add) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        onAdd();
      }

      @Override
      public void update(@NotNull AnActionEvent e) {
        super.update(e);
        e.getPresentation().setEnabled(true);
      }
    };
    AnAction cloneAction = new AnAction(TestMeBundle.message("testMe.settings.templates.copy.template"), null, PlatformIcons.COPY_ICON) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        onClone();
      }

      @Override
      public void update(@NotNull AnActionEvent e) {
        super.update(e);
        e.getPresentation().setEnabled(myCurrentTab.getSelectedTemplate() != null);
      }
    };
    AnAction resetAction = new AnAction(TestMeBundle.message("testMe.settings.templates.reset.to.default"), null, AllIcons.Actions.Rollback) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        onReset();
      }

      @Override
      public void update(@NotNull AnActionEvent e) {
        super.update(e);
        if (myCurrentTab == null) {
          e.getPresentation().setEnabled(false);
          return;
        }
        final FileTemplate selectedItem = myCurrentTab.getSelectedTemplate();
        e.getPresentation().setEnabled(selectedItem instanceof BundledFileTemplate && !selectedItem.isDefault());
      }
    };
    group.add(addAction);
    group.add(removeAction);
    group.add(cloneAction);
    group.add(resetAction);

    addAction.registerCustomShortcutSet(CommonShortcuts.INSERT, myCurrentTab.getComponent());
    removeAction.registerCustomShortcutSet(CommonShortcuts.getDelete(),
                                           myCurrentTab.getComponent());

    myToolBar = ActionManager.getInstance().createActionToolbar("FileTemplatesConfigurable", group, true).getComponent();
    myToolBar.setBorder(new CustomLineBorder(1, 1, 0, 1));

    SchemesPanel schemesPanel = new SchemesPanel();
    schemesPanel.setBorder(JBUI.Borders.empty(5, 10, 0, 10));
    schemesPanel.resetSchemes(Arrays.asList(FileTemplatesScheme.DEFAULT, myManager.getProjectScheme()));

    JPanel leftPanelWrapper = new JPanel(new BorderLayout());
    leftPanelWrapper.setBorder(JBUI.Borders.empty(10, 10, 10, 0));
    leftPanelWrapper.add(BorderLayout.NORTH, myToolBar);
    leftPanelWrapper.add(BorderLayout.CENTER, myLeftPanel);

    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(myTabbedPane.getComponent(), BorderLayout.NORTH);
    Splitter splitter = new Splitter(false, 0.3f);
    splitter.setDividerWidth(JBUI.scale(10));
    splitter.setFirstComponent(leftPanelWrapper);
    splitter.setSecondComponent(myEditorComponent);
    centerPanel.add(splitter, BorderLayout.CENTER);

    myMainPanel = new JPanel(new BorderLayout());
    myMainPanel.add(schemesPanel, BorderLayout.NORTH);
    myMainPanel.add(centerPanel, BorderLayout.CENTER);

    final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
    final String tabName = propertiesComponent.getValue(CURRENT_TAB);
    selectTab(tabName);

    return myMainPanel;
  }

  private void onReset() {
    FileTemplate selected = myCurrentTab.getSelectedTemplate();
    if (selected instanceof BundledFileTemplate) {
      if (Messages.showOkCancelDialog(IdeBundle.message("prompt.reset.to.original.template"),
                                      IdeBundle.message("title.reset.template"), "Reset", CommonBundle.getCancelButtonText(), Messages.getQuestionIcon()) !=
          Messages.OK) {
        return;
      }
//      ((BundledFileTemplate)selected).revertToDefaults();
      myEditor.reset();
      myModified = true;
    }
  }

  private void onEditorChanged() {
    fireListChanged();
  }

  private void onTabChanged() {
    applyEditor(myCurrentTab.getSelectedTemplate());

    FileTemplateTab tab = myCurrentTab;
    final int selectedIndex = myTabbedPane.getSelectedIndex();
    if (0 <= selectedIndex && selectedIndex < myTabs.length) {
      myCurrentTab = myTabs[selectedIndex];
    }
    ((CardLayout)myLeftPanel.getLayout()).show(myLeftPanel, myCurrentTab.getTitle());
    onListSelectionChanged();
    // request focus to a list (or tree) later to avoid moving focus to the tabbed pane
    if (tab != myCurrentTab) EventQueue.invokeLater(myCurrentTab.getComponent()::requestFocus);
  }

  private void onListSelectionChanged() {
    FileTemplate selectedValue = myCurrentTab.getSelectedTemplate();
    FileTemplate prevTemplate = myEditor == null ? null : myEditor.getTemplate();
    if (prevTemplate != selectedValue) {
      LOG.assertTrue(myEditor != null, "selected:" + selectedValue + "; prev:" + prevTemplate);
      //selection has changed
      if (Arrays.asList(myCurrentTab.getTemplates()).contains(prevTemplate) && !applyEditor(prevTemplate)) {
        return;
      }
      if (selectedValue == null) {
        myEditor.setTemplate(null, TestMeTemplateManagerImpl.getInstanceImpl(myProject).getDefaultTemplateDescription());
        myEditorComponent.repaint();
      }
      else {
        selectTemplate(selectedValue);
      }
    }
  }

  private boolean applyEditor(FileTemplate prevTemplate) {
    if (myEditor.isModified()) {
      try {
        myModified = true;
        myEditor.apply();
        fireListChanged();
      }
      catch (ConfigurationException e) {
        if (Arrays.asList(myCurrentTab.getTemplates()).contains(prevTemplate)) {
          myCurrentTab.selectTemplate(prevTemplate);
        }
        Messages.showErrorDialog(myMainPanel, e.getMessage(), IdeBundle.message("title.cannot.save.current.template"));
        return false;
      }
    }
    return true;
  }

  private void selectTemplate(FileTemplate template) {
    URL defDesc = null;
    if (myCurrentTab == myTemplatesList) {
      defDesc = TestMeTemplateManagerImpl.getInstanceImpl(myProject).getDefaultTemplateDescription();
    }
    else if (myCurrentTab == myIncludesList) {
      defDesc = TestMeTemplateManagerImpl.getInstanceImpl(myProject).getDefaultIncludeDescription();
    }
    if (myEditor.getTemplate() != template) {
      myEditor.setTemplate(template, defDesc);
      myEditor.setShowInternalMessage(isInternalTemplate(template) ? " " : null);
      myEditor.setShowAdjustCheckBox(myTemplatesList == myCurrentTab);
    }
  }

  @Override
  public boolean isProjectLevel() {
    return myScheme != null && !myScheme.getProject().isDefault();
  }

  /**
   * internal templates are not editable and not removable
   */
  static boolean isInternalTemplate(FileTemplate template){
    return template instanceof TestMeFileTemplate && ((TestMeFileTemplate) template).isDefault();
  }

  private void initLists() {
    FileTemplatesScheme scheme = myManager.getCurrentScheme();
    myManager.setCurrentScheme(myScheme);
    List<TemplateDescriptor> templateDescriptors = templateRegistry.getEnabledTemplateDescriptors();
    //todo need separate lists for default included + custom included?
    List<TestMeFileTemplate> fileTemplates = createFileTemplates(templateDescriptors);
    //todo merge changesCache with internal/default + pre-saved customized
    myTemplatesList.init(fileTemplates.toArray(new FileTemplate[]{}));
//    myTemplatesList.init(getTemplates(FileTemplateManager.DEFAULT_TEMPLATES_CATEGORY));
//    myTemplatesList.init(getTemplates(FileTemplateManager.INTERNAL_TEMPLATES_CATEGORY));
//    myIncludesList.init(getTemplates(FileTemplateManager.INCLUDES_TEMPLATES_CATEGORY));
    myIncludesList.init(createFileTemplates(templateRegistry.getIncludedTemplateDescriptors()).toArray(new FileTemplate[]{}));
 //todo add custom templates: Map<String, FileTemplate[]> templates = myChangesCache.get(myScheme);
    myManager.setCurrentScheme(scheme);
  }

  @NotNull
  private List<TestMeFileTemplate> createFileTemplates(List<TemplateDescriptor> templateDescriptors) {
    if (templateDescriptors == null) {
      return new ArrayList<>();
    }
    return templateDescriptors.stream().map(t -> createFileTemplate(t,myProject)).filter(Objects::nonNull).collect(Collectors.toList());
  }

  private TestMeFileTemplate createFileTemplate(TemplateDescriptor templateDescriptor, Project proj) {
    TestMeFileTemplate fileTemplate = new TestMeFileTemplate(templateDescriptor.getHtmlDisplayName(), templateDescriptor.getLanguage().name().toLowerCase());
    FileTemplate codeTemplate = null;
    try {
      if (templateDescriptor.getTemplateRole() == TemplateRole.Included) {
        codeTemplate = TestMeTemplateManagerImpl.getInstance(proj).getPattern(templateDescriptor.getFilename());
      }
      else {
        codeTemplate = TestMeTemplateManagerImpl.getInstance(proj).getInternalTemplate(templateDescriptor.getFilename());
      }

    } catch (Exception e) {
      LOG.warn("could not load template class", e);
      return null;
    }
    fileTemplate.setText(codeTemplate.getText());
    return fileTemplate;
  }

  private FileTemplate[] getTemplates(String category) {
    Map<String, FileTemplate[]> templates = myChangesCache.get(myScheme);
    if (templates == null) {
      return myManager.getTemplates(category);
    }
    return templates.get(category);
  }

  @Override
  public boolean isModified() {
    return myScheme != myManager.getCurrentScheme() || !myChangesCache.isEmpty() || isSchemeModified();
  }

  private boolean isSchemeModified() {
    return myModified || myEditor != null && myEditor.isModified();
  }

  private void checkCanApply(FileTemplateTab list) throws ConfigurationException {
    final FileTemplate[] templates = myCurrentTab.getTemplates();
    final List<String> allNames = new ArrayList<>();
    FileTemplate itemWithError = null;
    String errorString = null;
    for (FileTemplate template : templates) {
      final String currName = template.getName();
      if (currName.isEmpty()) {
        itemWithError = template;
        errorString = IdeBundle.message("error.please.specify.template.name");
        break;
      }
      if (allNames.contains(currName)) {
        itemWithError = template;
        errorString = "Template with name \'" + currName + "\' already exists. Please specify a different template name";
        break;
      }
      allNames.add(currName);
    }

    if (itemWithError != null) {
      myTabbedPane.setSelectedIndex(Arrays.asList(myTabs).indexOf(list));
      selectTemplate(itemWithError);
      list.selectTemplate(itemWithError);
      ApplicationManager.getApplication().invokeLater(myEditor::focusToNameField);
      throw new ConfigurationException(errorString);
    }
  }

  private void fireListChanged() {
    if (myCurrentTab != null) {
      myCurrentTab.fireDataChanged();
    }
    if (myMainPanel != null) {
      myMainPanel.revalidate();
    }
  }

  @Override
  public void apply() throws ConfigurationException {
    if (myEditor != null && myEditor.isModified()) {
      myModified = true;
      myEditor.apply();
    }

    for (FileTemplateTab list : myTabs) {
      checkCanApply(list);
    }
    updateCache();
    for (Map.Entry<FileTemplatesScheme, Map<String, FileTemplate[]>> entry : myChangesCache.entrySet()) {
      myManager.setCurrentScheme(entry.getKey());
      Map<String, FileTemplate[]> templates = entry.getValue();
      persistTemplates(templates, TestMeTemplateManagerImpl.INTERNAL_TEMPLATES_CATEGORY);
//      myManager.setTemplates(FileTemplateManager.INTERNAL_TEMPLATES_CATEGORY, Arrays.asList(templates.get(FileTemplateManager.INTERNAL_TEMPLATES_CATEGORY)));
      persistTemplates(templates, TestMeTemplateManagerImpl.INCLUDES_TEMPLATES_CATEGORY);
    }
    myChangesCache.clear();

    myManager.setCurrentScheme(myScheme);

    if (myEditor != null) {
      myModified = false;
      fireListChanged();
    }
  }

  private void persistTemplates(Map<String, FileTemplate[]> templates, String templatesCategory) {
    List<FileTemplate> customTemplates = Stream.of(templates.get(templatesCategory)).filter(t -> !t.isDefault()).collect(Collectors.toList());
    if (customTemplates.size() > 0) {
      myManager.setTemplates(templatesCategory, customTemplates);
    }
  }

  private boolean selectTab(String tabName) {
    int idx = 0;
    for (FileTemplateTab tab : myTabs) {
      if (Comparing.strEqual(tab.getTitle(), tabName)) {
        myCurrentTab = tab;
        myTabbedPane.setSelectedIndex(idx);
        return true;
      }
      idx++;
    }
    return false;
  }

  @Override
  public void reset() {
    myEditor.reset();
    changeScheme(myManager.getCurrentScheme());
    myChangesCache.clear();
    myModified = false;
  }

  @Override
  public void disposeUIResources() {
    if (myCurrentTab != null) {
      final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
      propertiesComponent.setValue(CURRENT_TAB, myCurrentTab.getTitle(), TEMPLATES_TITLE);
      final FileTemplate template = myCurrentTab.getSelectedTemplate();
      if (template != null) {
        propertiesComponent.setValue(SELECTED_TEMPLATE, template.getName());
      }
    }

    if (myEditor != null) {
      myEditor.disposeUIResources();
      myEditor = null;
      myEditorComponent = null;
    }
    myMainPanel = null;
    if (myUIDisposable != null) {
      Disposer.dispose(myUIDisposable);
      myUIDisposable = null;
    }
    myTabbedPane = null;
    myToolBar = null;
    myTabs = null;
    myCurrentTab = null;
    myTemplatesList = null;
//    myCodeTemplatesList = null;
    myIncludesList = null;
//    myOtherTemplatesList = null;
  }

  @Override
  @NotNull
  public String getId() {
    return "fileTemplates";
  }

  public static void editCodeTemplate(@NotNull final String templateId, Project project) {
    final ShowSettingsUtil util = ShowSettingsUtil.getInstance();
    final TestTemplatesConfigurable configurable = new TestTemplatesConfigurable(project);
    util.editConfigurable(project, configurable, () -> {
//      configurable.myTabbedPane.setSelectedIndex(ArrayUtil.indexOf(configurable.myTabs, configurable.myCodeTemplatesList));
//      for (FileTemplate template : configurable.myCodeTemplatesList.getTemplates()) {
//        if (Comparing.equal(templateId, template.getName())) {
//          configurable.myCodeTemplatesList.selectTemplate(template);
//          break;
//        }
//      }
    });
  }

  void changeScheme(@NotNull FileTemplatesScheme scheme) {
    if (myEditor != null && myEditor.isModified()) {
      myModified = true;
      try {
        myEditor.apply();
      }
      catch (ConfigurationException e) {
        Messages.showErrorDialog(myEditorComponent, e.getMessage(), e.getTitle());
        return;
      }
    }
    updateCache();
    myScheme = scheme;
    initLists();
  }

  private void updateCache() {
    if (isSchemeModified()) {
      if (!myChangesCache.containsKey(myScheme)) {
        Map<String, FileTemplate[]> templates = new HashMap<>();
        FileTemplate[] allTemplates = myTemplatesList.getTemplates();
        templates.put(FileTemplateManager.INTERNAL_TEMPLATES_CATEGORY, ContainerUtil.filter(allTemplates,
                                                                       template -> !myInternalTemplateNames.contains(template.getName())).toArray(FileTemplate.EMPTY_ARRAY)); // todo filter out testme oob templates
        templates.put(FileTemplateManager.INCLUDES_TEMPLATES_CATEGORY, myIncludesList.getTemplates());//todo filter out testme oob includes templates
        myChangesCache.put(myScheme, templates);
      }
    }
  }

  @NotNull
  public FileTemplateManager getManager() {
    return myManager;
  }

  @TestOnly
  FileTemplateConfigurable getEditor() {
    return myEditor;
  }

  @TestOnly
  FileTemplateTab[] getTabs() {
    return myTabs;
  }

  private final class SchemesPanel extends SimpleSchemesPanel<FileTemplatesScheme> implements SchemesModel<FileTemplatesScheme> {
    @NotNull
    @Override
    protected AbstractSchemeActions<FileTemplatesScheme> createSchemeActions() {
      return new AbstractSchemeActions<FileTemplatesScheme>(this) {
        @Override
        protected void resetScheme(@NotNull FileTemplatesScheme scheme) {
          throw new UnsupportedOperationException();
        }

        @Override
        protected void duplicateScheme(@NotNull FileTemplatesScheme scheme, @NotNull String newName) {
          throw new UnsupportedOperationException();
        }

        @Override
        protected void onSchemeChanged(@Nullable FileTemplatesScheme scheme) {
          if (scheme != null) changeScheme(scheme);
        }

        @Override
        protected void renameScheme(@NotNull FileTemplatesScheme scheme, @NotNull String newName) {
          throw new UnsupportedOperationException();
        }

        @NotNull
        @Override
        protected Class<FileTemplatesScheme> getSchemeType() {
          return FileTemplatesScheme.class;
        }
      };
    }

    @NotNull
    @Override
    public SchemesModel<FileTemplatesScheme> getModel() {
      return this;
    }

    @Override
    protected boolean supportsProjectSchemes() {
      return false;
    }

    @Override
    protected boolean highlightNonDefaultSchemes() {
      return false;
    }

    @Override
    public boolean useBoldForNonRemovableSchemes() {
      return true;
    }

    @Override
    public boolean canDuplicateScheme(@NotNull FileTemplatesScheme scheme) {
      return false;
    }

    @Override
    public boolean canResetScheme(@NotNull FileTemplatesScheme scheme) {
      return false;
    }

    @Override
    public boolean canDeleteScheme(@NotNull FileTemplatesScheme scheme) {
      return false;
    }

    @Override
    public boolean isProjectScheme(@NotNull FileTemplatesScheme scheme) {
      return false;
    }

    @Override
    public boolean canRenameScheme(@NotNull FileTemplatesScheme scheme) {
      return false;
    }

    @Override
    public boolean containsScheme(@NotNull String name, boolean projectScheme) {
      return false;
    }

    @Override
    public boolean differsFromDefault(@NotNull FileTemplatesScheme scheme) {
      return false;
    }

    @Override
    public void removeScheme(@NotNull FileTemplatesScheme scheme) {
      throw new UnsupportedOperationException();
    }
  }
}
