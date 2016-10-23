package com.weirddev.testme.intellij;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.codeInsight.FileModificationService;
import com.intellij.execution.junit.JUnit4Framework;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PostprocessReformattingAspect;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopesCore;
import com.intellij.testIntegration.TestFramework;
import com.intellij.testIntegration.createTest.JavaTestGenerator;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

/**
 * Date: 10/19/2016
 *
 * @author Yaron Yamin
 * @see JavaTestGenerator
 */
public class TestMeGenerator {
    public PsiElement generateTest(final FileTemplateContext context) {
        final Project project = context.getProject();
        return PostprocessReformattingAspect.getInstance(project).postponeFormattingInside(new Computable<PsiElement>() {
            public PsiElement compute() {
                return ApplicationManager.getApplication().runWriteAction(new Computable<PsiElement>() {
                    public PsiElement compute() {
                        try {
                            IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();

                            PsiClass targetClass = createTestClass(context);
                            if (targetClass == null) {
                                return null;
                            }
                            final TestFramework frameworkDescriptor = new JUnit4Framework();// context.getSelectedTestFrameworkDescriptor();
//                            final String defaultSuperClass = frameworkDescriptor.getDefaultSuperClass();
//                            final String superClassName = null;//context.getSuperClassName();
//                            if (!Comparing.strEqual(superClassName, defaultSuperClass)) {
//                                addSuperClass(targetClass, project, superClassName);
//                            }

                            Editor editor = CodeInsightUtil.positionCursor(project, targetClass.getContainingFile(), targetClass.getLBrace());

//                            addTestMethods(editor,
//                                    targetClass,
//                                    frameworkDescriptor,
//                                    null,//context.getSelectedMethods(),
//                                    true /*context.shouldGeneratedBefore()*/,
//                                    true /*context.shouldGeneratedAfter()*/);
                            return targetClass;
                        }
                        catch (IncorrectOperationException e) {
                            showErrorLater(project, context.getTargetClass());
                            return null;
                        }
                    }
                });
            }
        });
    }

    @Nullable
    private static PsiClass createTestClass(FileTemplateContext context) {
        final TestFramework testFrameworkDescriptor = new JUnit4Framework(); //TODO consider removing the dependency
//        final FileTemplateDescriptor fileTemplateDescriptor = TestIntegrationUtils.MethodKind.TEST_CLASS.getFileTemplateDescriptor(testFrameworkDescriptor);
        final PsiDirectory targetDirectory = context.getTargetDirectory();

        final PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage(targetDirectory);
        if (aPackage != null) {
            final GlobalSearchScope scope = GlobalSearchScopesCore.directoryScope(targetDirectory, false);
            final PsiClass[] classes = aPackage.findClassByShortName(context.getTargetClass(), scope);
            if (classes.length > 0) {
                if (!FileModificationService.getInstance().preparePsiElementForWrite(classes[0])) {
                    return null;
                }
                return classes[0];
            }
        }
//        if (fileTemplateDescriptor != null) {
            final PsiClass classFromTemplate = createTestClassFromCodeTemplate(context, targetDirectory);
            if (classFromTemplate != null) {
                return classFromTemplate;
            }
//        }

        return JavaDirectoryService.getInstance().createClass(targetDirectory, context.getTargetClass());
    }

    private static PsiClass createTestClassFromCodeTemplate(final FileTemplateContext context,
                                                            final PsiDirectory targetDirectory) {
        final String templateName = context.getFileTemplateDescriptor().getFileName();
        final FileTemplate fileTemplate = FileTemplateManager.getInstance(targetDirectory.getProject()).getCodeTemplate(templateName);
        final Properties defaultProperties = FileTemplateManager.getInstance(targetDirectory.getProject()).getDefaultProperties();
        Properties properties = new Properties(defaultProperties);
        properties.setProperty(FileTemplate.ATTRIBUTE_NAME, context.getTargetClass());
        final PsiClass targetClass = context.getSrcClass();
        if (targetClass != null && targetClass.isValid()) {
            properties.setProperty(FileTemplate.ATTRIBUTE_CLASS_NAME, targetClass.getQualifiedName());
        }
        try {
            final PsiElement psiElement = FileTemplateUtil.createFromTemplate(fileTemplate, templateName, properties, targetDirectory);
            if (psiElement instanceof PsiClass) {
                return (PsiClass)psiElement;
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }

    private static void showErrorLater(final Project project, final String targetClassName) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                Messages.showErrorDialog(project,
                        CodeInsightBundle.message("intention.error.cannot.create.class.message", targetClassName),
                        CodeInsightBundle.message("intention.error.cannot.create.class.title"));
            }
        });
    }


    @Override
    public String toString() {
        return CodeInsightBundle.message("intention.create.test.dialog.java");
    }
}
