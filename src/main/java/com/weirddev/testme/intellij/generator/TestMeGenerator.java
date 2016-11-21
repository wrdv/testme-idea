package com.weirddev.testme.intellij.generator;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.codeInsight.FileModificationService;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PostprocessReformattingAspect;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopesCore;
import com.intellij.testIntegration.createTest.JavaTestGenerator;
import com.intellij.util.IncorrectOperationException;
import com.weirddev.testme.intellij.FileTemplateContext;
import com.weirddev.testme.intellij.template.TestMeTemplateParams;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Date: 10/19/2016
 *
 * @author Yaron Yamin
 * @see JavaTestGenerator
 */
public class TestMeGenerator {
    private final TestClassElementsLocator testClassElementsLocator;
    private final TestTemplateContextBuilder testTemplateContextBuilder;

    public TestMeGenerator() {
        this(new TestClassElementsLocator(), new TestTemplateContextBuilder());
    }

    TestMeGenerator(TestClassElementsLocator testClassElementsLocator, TestTemplateContextBuilder testTemplateContextBuilder) {
        this.testClassElementsLocator = testClassElementsLocator;
        this.testTemplateContextBuilder = testTemplateContextBuilder;
    }

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
                            CodeInsightUtil.positionCursor(project, targetClass.getContainingFile(), testClassElementsLocator.findOptimalCursorLocation(targetClass));
                            return targetClass;
                        } catch (IncorrectOperationException e) {
                            showErrorLater(project, context.getTargetClass());
                            return null;
                        }
                    }
                });
            }
        });
    }

    @Nullable
    private PsiClass createTestClass(FileTemplateContext context) {
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
        final PsiClass classFromTemplate = createTestClassFromCodeTemplate(context, targetDirectory);
        if (classFromTemplate != null) {
            return classFromTemplate;
        }
        return JavaDirectoryService.getInstance().createClass(targetDirectory, context.getTargetClass());
    }

    private PsiClass createTestClassFromCodeTemplate(final FileTemplateContext context, final PsiDirectory targetDirectory) {
        final String templateName = context.getFileTemplateDescriptor().getFileName();
        FileTemplateManager fileTemplateManager = FileTemplateManager.getInstance(targetDirectory.getProject());
        Map<String, Object> templateCtxtParams = testTemplateContextBuilder.build(context, fileTemplateManager.getDefaultProperties());

        final PsiClass targetClass = context.getSrcClass();
        if (targetClass != null && targetClass.isValid()) {
            templateCtxtParams.put(TestMeTemplateParams.TESTED_CLASS_NAME, targetClass.getName());
        }
        try {
            FileTemplate codeTemplate = fileTemplateManager.getCodeTemplate(templateName);
            codeTemplate.setReformatCode(context.isReformatCode());
            final PsiElement psiElement = FileTemplateUtil.createFromTemplate(codeTemplate, templateName, templateCtxtParams, targetDirectory, null);
            if (psiElement instanceof PsiClass) {
                return (PsiClass) psiElement;
            }
            return null;
        } catch (Exception e) {
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
