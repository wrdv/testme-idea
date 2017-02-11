package com.weirddev.testme.intellij.generator;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInsight.generation.CommentByLineCommentHandler;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.impl.source.PostprocessReformattingAspect;
import com.intellij.psi.impl.source.PsiImportStatementImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopesCore;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testIntegration.createTest.JavaTestGenerator;
import com.intellij.util.IncorrectOperationException;
import com.weirddev.testme.intellij.template.FileTemplateContext;
import org.apache.velocity.app.Velocity;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.java.generate.psi.PsiAdapter;

import java.util.Collection;
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
    private static final Logger LOG = Logger.getInstance(TestMeGenerator.class.getName());

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
        try {
            FileTemplate codeTemplate = fileTemplateManager.getCodeTemplate(templateName);
            codeTemplate.setReformatCode(context.isReformatCode());
            Velocity.setProperty( Velocity.VM_MAX_DEPTH, 200);
            final PsiElement psiElement = FileTemplateUtil.createFromTemplate(codeTemplate, templateName, templateCtxtParams, targetDirectory, null);
            if (psiElement instanceof PsiClass) {
                PsiClass psiClass = (PsiClass) psiElement;
                JavaCodeStyleManager codeStyleManager = JavaCodeStyleManager.getInstance(targetDirectory.getProject());
                if (context.isOptimizeImports()) {
                    codeStyleManager.optimizeImports(psiClass.getContainingFile());
                }
                if (context.isReplaceFqn()) {
                    codeStyleManager.shortenClassReferences(psiClass.getContainingFile());
                }

                //todo replace //import
                final Document document = psiClass.getContainingFile().getViewProvider().getDocument();
                if (document != null) {
                    PsiDocumentManager.getInstance(context.getProject()).doPostponedOperationsAndUnblockDocument(document);
                    final Collection<PsiComment> psiComments = PsiTreeUtil.findChildrenOfType(psiClass.getParent(), PsiComment.class);
                    final String commentedImportToken = "//import ";
                    for (PsiComment psiComment : psiComments) {
                        final String commentText = psiComment.getText();
                        if (commentText != null && commentText.startsWith(commentedImportToken)) {
                            final int startIndex = document.getText().indexOf(commentedImportToken);
                            if (startIndex != 0) {
                                document.replaceString(startIndex,startIndex+commentedImportToken.length(),"import ");
                            }
//                        String unCommentedImport=commentText.replace("////import ", "import ");
//                        new CommentByLineCommentHandler().invoke(context.getProject(),,);
//                        addImportStatement(psiClass.)
//                        final PsiJavaFile aFile = createDummyJavaFile("import " + packageName + ".*;");
//                        final PsiImportStatementBase statement = extractImport(aFile, false);
//                        (PsiImportStatement) CodeStyleManager.getInstance(myManager.getProject()).reformat(statement);
//
//                        PsiImportStatement is = factory.createImportStatementOnDemand(fixImportStatement(importStatementOnDemand));
//                        psiComment.replace(new PsiImportStatementImpl(JavaTokenType.END_OF_LINE_COMMENT, commentText));
                        }
                    }
                }

                return psiClass;
            }
            return null;

        } catch (Exception e) {
            LOG.error("error generating test class",e);
            return null;
        }
    }

//    /**
//     * @see PsiAdapter#addImportStatement(com.intellij.psi.PsiJavaFile, java.lang.String)
//     */
//    public void addImportStatement(PsiJavaFile javaFile, String importStatementOnDemand) {
//        PsiElementFactory factory = JavaPsiFacade.getInstance(javaFile.getProject()).getElementFactory();
//        PsiImportStatement is = createImportStatementOnDemand(importStatementOnDemand, importStatementOnDemand.contains("static"));
//
//        // add the import to the file, and optimize the imports
//        PsiImportList importList = javaFile.getImportList();
//        if (importList != null) {
//            importList.add(is);
//        }
//
//        JavaCodeStyleManager.getInstance(javaFile.getProject()).optimizeImports(javaFile);
//    }
//
//    /**
//     * @see PsiElementFactory#createImportStatementOnDemand(java.lang.String)
//     */
//    @NotNull
//    public PsiImportStatement createImportStatementOnDemand(@NotNull final String importStatement, boolean isStatic) throws IncorrectOperationException {
//        final PsiJavaFile aFile = createDummyJavaFile(importStatement);
//        final PsiImportStatementBase statement = extractImport(aFile, isStatic);
//        return (PsiImportStatement)statement;
//    }
//    private static PsiImportStatementBase extractImport(final PsiJavaFile aFile, final boolean isStatic) {
//        final PsiImportList importList = aFile.getImportList();
//        assert importList != null : aFile;
//        final PsiImportStatementBase[] statements = isStatic ? importList.getImportStaticStatements() : importList.getImportStatements();
//        assert statements.length == 1 : aFile.getText();
//        return statements[0];
//    }
//
//
//    protected PsiJavaFile createDummyJavaFile(@NonNls final String text) {
//        final FileType type = JavaFileType.INSTANCE;
//        return (PsiJavaFile)PsiFileFactory.getInstance(myManager.getProject()).createFileFromText("_Dummy_.java", type, text);
//    }

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
