package com.weirddev.testme.intellij.action;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.JavaProjectRootsUtil;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.SourceFolder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testIntegration.createTest.CreateTestAction;
import com.intellij.util.IncorrectOperationException;
import com.weirddev.testme.intellij.action.helpers.ClassNameSelection;
import com.weirddev.testme.intellij.action.helpers.GeneratedClassNameResolver;
import com.weirddev.testme.intellij.action.helpers.TargetDirectoryLocator;
import com.weirddev.testme.intellij.configuration.TestMeConfigPersistent;
import com.weirddev.testme.intellij.generator.TestMeGenerator;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.FileTemplateContext;
import com.weirddev.testme.intellij.template.TemplateDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;
import org.jetbrains.jps.model.java.JavaResourceRootProperties;
import org.jetbrains.jps.model.java.JavaSourceRootProperties;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Date: 10/18/2016
 *
 * @author Yaron Yamin
 *
 * @see CreateTestAction
 */
public class CreateTestMeAction extends CreateTestAction {
    private static final Logger LOG = Logger.getInstance(CreateTestMeAction.class.getName());
    private static final String CREATE_TEST_IN_THE_SAME_ROOT = "create.test.in.the.same.root";
    private final TestMeGenerator testMeGenerator;
    private final GeneratedClassNameResolver generatedClassNameResolver;
    private TemplateDescriptor templateDescriptor;
    private final TargetDirectoryLocator targetDirectoryLocator;

    public CreateTestMeAction(TemplateDescriptor templateDescriptor) {
        this(templateDescriptor, new TestMeGenerator(), new TargetDirectoryLocator(), new GeneratedClassNameResolver());
    }

    CreateTestMeAction(TemplateDescriptor templateDescriptor, TestMeGenerator testMeGenerator, TargetDirectoryLocator targetDirectoryLocator, GeneratedClassNameResolver generatedClassNameResolver) {
        this.testMeGenerator = testMeGenerator;
        this.generatedClassNameResolver = generatedClassNameResolver;
        this.templateDescriptor = templateDescriptor;
        this.targetDirectoryLocator = targetDirectoryLocator;
    }


    @NotNull
    public String getText(){
        return "Generate test with TestMe";
    }

    @NotNull
    public String getFamilyName() {
        return super.getFamilyName();
    }


    @Override
    public void invoke(@NotNull final Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        LOG.debug("Start CreateTestMeAction.invoke");
        final Module srcModule = ModuleUtilCore.findModuleForPsiElement(element);
        if (srcModule == null) return;
        final PsiClass srcClass = getContainingClass(element);
        if (srcClass == null) return;
        PsiDirectory srcDir = element.getContainingFile().getContainingDirectory();
        final PsiPackage srcPackage = JavaDirectoryService.getInstance().getPackage(srcDir);

        final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        Module testModule = suggestModuleForTestsReflective(project, srcModule);
        final List<VirtualFile> testRootUrls = computeTestRoots(testModule);
//            final HashSet<VirtualFile> testFolders = new HashSet<VirtualFile>(); //from v14
//        checkForTestRoots(srcModule, testFolders); //from v14
        if (testRootUrls==null|| testRootUrls.isEmpty() && computeSuitableTestRootUrls(testModule).isEmpty()) {
            testModule = srcModule;
            if (!propertiesComponent.getBoolean(CREATE_TEST_IN_THE_SAME_ROOT, false)) {
                if (Messages.showOkCancelDialog(project, "Create test in the same source root?", "No Test Roots Found", Messages.getQuestionIcon()) !=
                        Messages.OK) {
                    return;
                }
                propertiesComponent.setValue(CREATE_TEST_IN_THE_SAME_ROOT, String.valueOf(true));
            }
        }
        final PsiDirectory targetDirectory = targetDirectoryLocator.getOrCreateDirectory(project, srcPackage, testModule);
        if (targetDirectory == null) {
            return;
        }
        LOG.debug("targetDirectory:"+targetDirectory.getVirtualFile().getUrl());
        final ClassNameSelection classNameSelection = generatedClassNameResolver.resolveClassName(project, targetDirectory, srcClass, templateDescriptor);
        if (classNameSelection.getUserDecision() != ClassNameSelection.UserDecision.Abort) {
            final Module finalTestModule = testModule;
            CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                @Override
                public void run() {
                    testMeGenerator.generateTest(
                            new FileTemplateContext(
                                    new FileTemplateDescriptor(templateDescriptor.getFilename()),templateDescriptor.getLanguage(),project, classNameSelection.getClassName(), srcPackage, srcModule, finalTestModule,targetDirectory, srcClass,
                                    new FileTemplateConfig(TestMeConfigPersistent.getInstance().getState())
                            )
                    );
                }
            }, "TestMe Generate Test", this);
        }
        LOG.debug("End CreateTestMeAction.invoke");
    }

    /**
     * @see CreateTestAction#computeSuitableTestRootUrls
     */
    public static List<String> computeSuitableTestRootUrls(Module module) {
        final ArrayList<String> rootUrls = new ArrayList<String>();
        for (SourceFolder sourceFolder : suitableTestSourceFolders(module)) {
            rootUrls.add(sourceFolder.getUrl());
        }
        return rootUrls;
    }
    /**
     * @see CreateTestAction#suitableTestSourceFolders
     */
    private static List<SourceFolder> suitableTestSourceFolders(@NotNull Module module) {
        final ArrayList<SourceFolder> sourceFolders = new ArrayList<SourceFolder>();
        for (ContentEntry contentEntry : ModuleRootManager.getInstance(module).getContentEntries()) {
            final List<SourceFolder> testSourceFolders = contentEntry.getSourceFolders(JavaSourceRootType.TEST_SOURCE);
            for (SourceFolder sourceFolder : testSourceFolders) {
                if (!isForGeneratedSources(sourceFolder)) {
                    sourceFolders.add(sourceFolder);
                }
            }
        }
        return sourceFolders;
    }
    /**
     * @see JavaProjectRootsUtil#isForGeneratedSources(com.intellij.openapi.roots.SourceFolder)
     */
    private static boolean isForGeneratedSources(SourceFolder sourceFolder) {
        JavaSourceRootProperties properties = sourceFolder.getJpsElement().getProperties(JavaModuleSourceRootTypes.SOURCES);
        JavaResourceRootProperties resourceProperties = sourceFolder.getJpsElement().getProperties(JavaModuleSourceRootTypes.RESOURCES);
        return properties != null && properties.isForGeneratedSources() || resourceProperties != null && resourceProperties.isForGeneratedSources();
    }

//    public static void checkForTestRoots(Module srcModule, Set<VirtualFile> testFolders) {
//        CreateTestAction.checkForTestRoots(srcModule, testFolders);
//    }

    /**
     * safely calls CreateTestAction.suggestModuleForTests which was introduced in v15
     * @see CreateTestAction.suggestModuleForTests
     * @return
     */
    @NotNull
    private static Module suggestModuleForTestsReflective(@NotNull Project project, @NotNull Module productionModule) {
        try {
            Method suggestModuleForTests = null;
            //          suggestModuleForTests = CreateTestAction.class.getDeclaredMethod("suggestModuleForTests", Project.class,Module.class);
            // this didn't do the trick. actual compiled method name differs from original source code. so locating by signature..
            for (Method method : CreateTestAction.class.getDeclaredMethods()) {
                final Class<?>[] parameters = method.getParameterTypes();
                if ( method.getReturnType().isAssignableFrom(Module.class)  && parameters != null && parameters.length == 2 && parameters[0].isAssignableFrom(Project.class) && parameters[1].isAssignableFrom(Module.class) ) {
                    suggestModuleForTests = method;
                }
            }
            if (suggestModuleForTests != null) {
                suggestModuleForTests.setAccessible(true);
                try {
                    final Object module = suggestModuleForTests.invoke(null, project,productionModule);
                    if (module != null) {
                        return (Module) module;
                    }
                } catch (Exception e) {
                    LOG.debug("error invoking suggestModuleForTests through reflection. falling back to older implementation",e);
                }
            }

        } catch (Exception e) {
            LOG.debug("suggestModuleForTests Method mot found. expected to exist on idea 15 - 2017. falling back to older implementation",e);
        }
        return productionModule;
    }

    /**
     * @see  CreateTestAction#computeTestRoots
     */
    public static List<VirtualFile> computeTestRoots(@NotNull Module mainModule) {
        final ArrayList<VirtualFile> virtualFiles = new ArrayList<VirtualFile>();
        final List<SourceFolder> sourceFolders = suitableTestSourceFolders(mainModule);
        if (!sourceFolders.isEmpty()) {
            //create test in the same module, if the test source folder doesn't exist yet it will be created
            for (SourceFolder sourceFolder : sourceFolders) {
                if (sourceFolder.getFile() != null) {
                    virtualFiles.add(sourceFolder.getFile());
                }
            }
        } else {
            //suggest to choose from all dependencies modules
            final HashSet<Module> modules = new HashSet<Module>();
            ModuleUtilCore.collectModulesDependsOn(mainModule, modules);
            for (Module module : modules) {
                final List<SourceFolder> folders = suitableTestSourceFolders(module);
                for (SourceFolder sourceFolder : folders) {
                    if (sourceFolder.getFile() != null) {
                        virtualFiles.add(sourceFolder.getFile());
                    }
                }
            }
        }
        return virtualFiles;
    }

    /**
     * @see CreateTestAction#getContainingClass(com.intellij.psi.PsiElement)
     * base method used to be private in IJ 14
     */
    @Nullable
    public static PsiClass getContainingClass(PsiElement element) {
        final PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class, false);
        if (psiClass == null) {
            final PsiFile containingFile = element.getContainingFile();
            if (containingFile instanceof PsiClassOwner){
                final PsiClass[] classes = ((PsiClassOwner)containingFile).getClasses();
                if (classes.length == 1) {
                    return classes[0];
                }
            }
        }
        return psiClass;
    }
}
