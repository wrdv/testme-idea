package com.weirddev.testme.intellij.action;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testIntegration.createTest.CreateTestAction;
import com.intellij.util.IncorrectOperationException;
import com.weirddev.testme.intellij.action.helpers.ClassNameSelection;
import com.weirddev.testme.intellij.action.helpers.GeneratedClassNameResolver;
import com.weirddev.testme.intellij.action.helpers.TargetDirectoryLocator;
import com.weirddev.testme.intellij.generator.TestMeGenerator;
import com.weirddev.testme.intellij.template.FileTemplateContext;
import com.weirddev.testme.intellij.template.TemplateDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public static final int MAX_RECURSION_DEPTH = 9;
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
        final PsiClass srcClass = getContainingClass(element);
        if (srcClass == null) return;
        PsiDirectory srcDir = element.getContainingFile().getContainingDirectory();
        final PsiPackage srcPackage = JavaDirectoryService.getInstance().getPackage(srcDir);

        final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        Module testModule = suggestModuleForTests(project, srcModule);
        final List<VirtualFile> testRootUrls = computeTestRoots(testModule);
//            final HashSet<VirtualFile> testFolders = new HashSet<VirtualFile>(); //from v14
//        checkForTestRoots(srcModule, testFolders); //from v14
        if (testRootUrls.isEmpty() /*&& computeSuitableTestRootUrls(testModule).isEmpty() Added post v14 */) {
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
        final ClassNameSelection classNameSelection = generatedClassNameResolver.resolveClassName(project, targetDirectory, srcClass.getName(), templateDescriptor);
        if (classNameSelection.getUserDecision() != ClassNameSelection.UserDecision.Abort) {
            CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                @Override
                public void run() {
                    testMeGenerator.generateTest(new FileTemplateContext(new FileTemplateDescriptor(templateDescriptor.getFilename()),project, classNameSelection.getClassName(), srcPackage, srcModule, targetDirectory, srcClass, true, true, MAX_RECURSION_DEPTH, true, true));
                }
            }, "TestMe Generate Test", this);
        }
        LOG.debug("End CreateTestMeAction.invoke");
    }
    public static void checkForTestRoots(Module srcModule, Set<VirtualFile> testFolders) {
        CreateTestAction.checkForTestRoots(srcModule, testFolders);
    }

    @NotNull
    private static Module suggestModuleForTests(@NotNull Project project, @NotNull Module productionModule) {
        try {
        final Method suggestModuleForTests = CreateTestAction.class.getDeclaredMethod("suggestModuleForTests", Project.class,Module.class);
        suggestModuleForTests.setAccessible(true);
            try {
                final Object module = suggestModuleForTests.invoke(null, project,productionModule);
                if (module == null) {
                    return null;
                } else {
                    return (Module) module;
                }
            } catch (Exception e) {
                LOG.debug("error invoking suggestModuleForTests through reflection. falling back to older implementation",e);
            }
        } catch (Exception e) {
        LOG.debug("suggestModuleForTests Method mot found . this is probably not idea 2016. falling back to older implementation");
        }
        return productionModule;
    }

    public static List<VirtualFile> computeTestRoots(Module srcModule) {
        try {
            final Method computeTestRootsMethod = CreateTestAction.class.getDeclaredMethod("computeTestRoots", Module.class);
            computeTestRootsMethod.setAccessible(true);
            try {
                final Object roots = computeTestRootsMethod.invoke(null, srcModule);
                if (roots == null) {
                    return null;
                } else if (roots instanceof List && !((List)roots).isEmpty() &&((List)roots).get(0) instanceof VirtualFile) {
                    return (List<VirtualFile>) roots;
                }
            } catch (Exception e) {
                LOG.debug("error invoking computeTestRootsMethod through reflection. falling back to older implementation",e);
            }
        } catch (Exception e) {
            LOG.debug("computeTestRoots Method mot found. this is probably not idea 2016. falling back to older implementation");
        }
        final HashSet<VirtualFile> testFolders = new HashSet<VirtualFile>();
        CreateTestAction.checkForTestRoots(srcModule, testFolders);
        return new ArrayList<VirtualFile>(testFolders);
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
