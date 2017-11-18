package com.weirddev.testme.intellij.generator;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.codeInsight.FileModificationService;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.impl.source.PostprocessReformattingAspect;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopesCore;
import com.intellij.testIntegration.createTest.JavaTestGenerator;
import com.intellij.util.IncorrectOperationException;
import com.weirddev.testme.intellij.template.FileTemplateContext;
import org.apache.velocity.app.Velocity;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Map;

/**
 * Date: 10/19/2016
 *
 * @author Yaron Yamin
 * @see JavaTestGenerator
 */
public class TestMeGenerator {
    private final TestClassElementsLocator testClassElementsLocator;
    private final TestTemplateContextBuilder testTemplateContextBuilder;
    private final CodeRefactorUtil codeRefactorUtil;
    private static final Logger LOG = Logger.getInstance(TestMeGenerator.class.getName());

    public TestMeGenerator() {
        this(new TestClassElementsLocator(), new TestTemplateContextBuilder(),new CodeRefactorUtil());
    }
    
    TestMeGenerator(TestClassElementsLocator testClassElementsLocator, TestTemplateContextBuilder testTemplateContextBuilder, CodeRefactorUtil codeRefactorUtil) {
        this.testClassElementsLocator = testClassElementsLocator;
        this.testTemplateContextBuilder = testTemplateContextBuilder;
        this.codeRefactorUtil = codeRefactorUtil;
    }

    public PsiElement generateTest(final FileTemplateContext context) {
        final Project project = context.getProject();
        return PostprocessReformattingAspect.getInstance(project).postponeFormattingInside(new Computable<PsiElement>() {
            public PsiElement compute() {
                return ApplicationManager.getApplication().runWriteAction(new Computable<PsiElement>() {
                    public PsiElement compute() {
                        try {
                            final long start = new Date().getTime();
                            IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();
                            PsiClass targetClass = createTestClass(context);
                            if (targetClass == null) {
                                return null;
                            }
                            try {
                                final PsiElement optimalCursorLocation = testClassElementsLocator.findOptimalCursorLocation(targetClass);
                                if (optimalCursorLocation != null) {
                                    CodeInsightUtil.positionCursor(project, targetClass.getContainingFile(), optimalCursorLocation);
                                }
                            } catch (Throwable e) {
                                LOG.warn("unable to locate optimal cursor location post test generation",e);
//                                new OpenFileDescriptor(project, targetClass.getContainingFile().getVirtualFile()).navigate(true);
                            }
                            LOG.debug("Done generating class "+context.getTargetClass()+" in "+(new Date().getTime()-start)+" millis");
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
        try {
            FileTemplate codeTemplate = fileTemplateManager.getInternalTemplate(templateName);
            codeTemplate.setReformatCode(false);
            Velocity.setProperty( Velocity.VM_MAX_DEPTH, 200);
            final long startGeneration = new Date().getTime();
            final PsiElement psiElement = FileTemplateUtil.createFromTemplate(codeTemplate, context.getTargetClass(), templateCtxtParams, targetDirectory, null);
            LOG.debug("Done generating PsiElement from template "+codeTemplate.getName()+" in "+(new Date().getTime()-startGeneration)+" millis");
            final long startReformating = new Date().getTime();
            final PsiElement resolvedPsiElement=resolveEmbeddedClass(psiElement);
            if (resolvedPsiElement instanceof PsiClass) {
                PsiClass psiClass = (PsiClass) resolvedPsiElement;
                JavaCodeStyleManager codeStyleManager = JavaCodeStyleManager.getInstance(targetDirectory.getProject());
                if (context.getFileTemplateConfig().isOptimizeImports()) {
                    codeStyleManager.optimizeImports(psiClass.getContainingFile());
                }
                codeRefactorUtil.uncommentImports(psiClass, context.getProject());
                if (context.getFileTemplateConfig().isReplaceFqn()) {
                    codeStyleManager.shortenClassReferences(psiClass);
                }
                if (context.getFileTemplateConfig().isReformatCode()) {
                    final PsiFile containingFile = psiClass.getContainingFile();
                    final TextRange textRange = containingFile.getTextRange();
                    CodeStyleManager.getInstance(context.getProject()).reformatText(containingFile, textRange.getStartOffset(), textRange.getEndOffset());
                }
                LOG.debug("Done reformatting generated PsiClass in "+(new Date().getTime()-startReformating)+" millis");
                final PsiElement formattedPsiElement=resolveEmbeddedClass(psiElement);
                if (formattedPsiElement instanceof PsiClass) {
                    return (PsiClass) formattedPsiElement;
                } else {
//                    flushOperations(context, psiClass);
                    return psiClass;
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            LOG.error("error generating test class",e);
            return null;
        }
    }

//    private void flushOperations(FileTemplateContext context, PsiClass psiClass) {
//        final Document document = psiClass.getContainingFile().getViewProvider().getDocument();
//        if (document != null) {
//            PsiDocumentManager.getInstance(context.getProject()).doPostponedOperationsAndUnblockDocument(document);
//        }
//    }

    private PsiElement resolveEmbeddedClass(PsiElement psiElement) {
        //Important for Groovy support - expecting org.jetbrains.plugins.groovy.lang.psi.GroovyFile. see org.jetbrains.plugins.groovy.annotator.intentions.CreateClassActionBase.createClassByType
        final PsiElement resolveEmbeddedClass = resolveEmbeddedClassRecursive(psiElement, 2);
        if (resolveEmbeddedClass == null) {
            return psiElement;
        } else {
            return resolveEmbeddedClass;
        }
    }

    @Nullable
    private PsiElement resolveEmbeddedClassRecursive(PsiElement psiElement, int recursionLevel) {
        if (psiElement instanceof PsiClass) {
            return psiElement;
        } else  if (recursionLevel <= 0) {
            return null;
        }
        else{
            final PsiElement[] psiElementChildren = psiElement.getChildren();
            for (PsiElement psiElementChild : psiElementChildren) {
                final PsiElement resolvedPsiClass= resolveEmbeddedClassRecursive(psiElementChild, recursionLevel - 1);
                if (resolvedPsiClass != null) {
                    return resolvedPsiClass;
                }
            }
        }
        return null;
    }

    static void showErrorLater(final Project project, final String targetClassName) {
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
