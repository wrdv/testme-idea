package com.weirddev.testme.intellij;

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
import com.weirddev.testme.intellij.generator.TestMeGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
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
    private final TestMeGenerator testMeGenerator;
    private TemplateDescriptor templateDescriptor;

    public CreateTestMeAction(TemplateDescriptor templateDescriptor) {
        this.templateDescriptor = templateDescriptor;
        testMeGenerator = new TestMeGenerator();
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
        final Module srcModule = ModuleUtilCore.findModuleForPsiElement(element);
        final PsiClass srcClass = getContainingClass(element);
        if (srcClass == null) return;
        PsiDirectory srcDir = element.getContainingFile().getContainingDirectory();
        final PsiPackage srcPackage = JavaDirectoryService.getInstance().getPackage(srcDir);

        final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        final HashSet<VirtualFile> testFolders = new HashSet<VirtualFile>();
        checkForTestRoots(srcModule, testFolders);
        if (testFolders.isEmpty() && !propertiesComponent.getBoolean(CREATE_TEST_IN_THE_SAME_ROOT, false)) {
            if (Messages.showOkCancelDialog(project, "Create test in the same source root?", "No Test Roots Found", Messages.getQuestionIcon()) !=
                    Messages.OK) {
                return;
            }
            propertiesComponent.setValue(CREATE_TEST_IN_THE_SAME_ROOT, String.valueOf(true));
        }
        final String targetClass = getClassName(srcClass);
        final TargetDirectoryLocator targetDirectoryLocator = new TargetDirectoryLocator();
        final PsiDirectory targetDirectory = targetDirectoryLocator.getOrCreateDirectory(project, srcPackage, srcModule, targetClass);
        //TODO show merge , new , cancel prompt. alt. to com.intellij.refactoring.util.RefactoringMessageUtil.checkCanCreateClass()
        if (targetDirectory != null) {
            LOG.debug("targetDirectory:"+targetDirectory.getVirtualFile().getUrl());
            CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                @Override
                public void run() {
                    testMeGenerator.generateTest(new FileTemplateContext(new FileTemplateDescriptor(templateDescriptor.getFilename()),project, targetClass, srcPackage, srcModule, targetDirectory, srcClass, true, true, 9, true));
                }
            }, "TestMe Generate Test", this);
        }


    }
    private String getClassName(PsiClass myTargetClass) {
        return String.format(templateDescriptor.getTestClassFormat(), myTargetClass.getName());
    }

    protected static void checkForTestRoots(Module srcModule, Set<VirtualFile> testFolders) {
        CreateTestAction.checkForTestRoots(srcModule, testFolders);
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
