package com.weirddev.testme.intellij.generator;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CodeRefactorUtil {

    public void uncommentImports(PsiClass psiClass, Project project) {
        final Collection<PsiComment> psiComments = PsiTreeUtil.findChildrenOfType(psiClass.getParent(), PsiComment.class);
        final String commentedImportToken = "//import ";
        for (PsiComment psiComment : psiComments) {
            final String commentText = psiComment.getText();
            if (commentText != null && commentText.startsWith(commentedImportToken)) {
                String unCommentedImport = commentText.replace(commentedImportToken, "import ");
                PsiElement newImport = createImportStatementOnDemand(project, unCommentedImport, unCommentedImport.contains("static"));
                psiComment.replace(newImport);
            }
        }
    }

    /**
     * @see PsiElementFactory#createImportStatementOnDemand(String)
     */
    @NotNull
    private PsiElement createImportStatementOnDemand(Project project, @NotNull final String importStatement, boolean isStatic) throws IncorrectOperationException {
        final PsiJavaFile aFile = createDummyJavaFile(project, importStatement);
        final PsiImportStatementBase statement = extractImport(aFile, isStatic);
        return statement;
    }

    /**
     * @see PsiElementFactoryImpl#extractImport(com.intellij.psi.PsiJavaFile, boolean)
     */
    static PsiImportStatementBase extractImport(final PsiJavaFile aFile, final boolean isStatic) {
        final PsiImportList importList = aFile.getImportList();
        assert importList != null : aFile;
        final PsiImportStatementBase[] statements = isStatic ? importList.getImportStaticStatements() : importList.getImportStatements();
        assert statements.length == 1 : aFile.getText();
        return statements[0];
    }

    /**
     * @ com.intellij.psi.impl.PsiJavaParserFacadeImpl#createDummyJavaFile(java.lang.String)
     */
    private PsiJavaFile createDummyJavaFile(Project project, @NonNls final String text) {
        final FileType type = JavaFileType.INSTANCE;
        return (PsiJavaFile) PsiFileFactory.getInstance(project).createFileFromText("_Dummy_.java", type, text);
    }

}