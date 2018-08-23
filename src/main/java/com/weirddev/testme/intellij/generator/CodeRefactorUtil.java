package com.weirddev.testme.intellij.generator;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiElementFactoryImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.weirddev.testme.intellij.common.utils.LanguageUtils;
import com.weirddev.testme.intellij.scala.resolvers.ScalaPsiRefactoringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Collection;

public class CodeRefactorUtil {

    public static final String COMMENTED_IMPORT_TOKEN = "//import ";
    private static final Logger LOG = Logger.getInstance(CodeRefactorUtil.class.getName());

    public void uncommentImports(PsiClass psiClass, Project project) {
        final Collection<PsiComment> psiComments = PsiTreeUtil.findChildrenOfType(psiClass.getParent(), PsiComment.class);
        for (PsiComment psiComment : psiComments) {
            final String commentText = psiComment.getText();
            if (commentText != null && commentText.startsWith(COMMENTED_IMPORT_TOKEN)) {
                PsiElement newImport = extractImportStatement(psiClass, project, commentText.replace(COMMENTED_IMPORT_TOKEN, "import "));
                if (newImport != null) {
                    final String prevSiblingText = psiComment.getPrevSibling()==null?null:psiComment.getPrevSibling().getText();
                    if(prevSiblingText !=null && (prevSiblingText.equals("\n\n")|| prevSiblingText.equals("\n"))){
                        psiComment.getPrevSibling().delete();//WA fix - 2 newlines were added in groovy files. 1 newline in java due to styling
                    }
                    psiComment.replace(newImport);
                }
            }
        }
    }

    private PsiElement extractImportStatement(PsiClass psiClass, Project project, String unCommentedImport) {
        PsiElement newImport = null;
        final String fileTypeName = psiClass.getContainingFile().getFileType().getName();
        if ("JAVA".equalsIgnoreCase(fileTypeName)) {
            newImport = createImportStatementOnDemand(project, unCommentedImport, unCommentedImport.contains("static"));
        } else if ("Groovy".equalsIgnoreCase(fileTypeName)) {
            newImport = createGroovyImport(project, unCommentedImport);
        } else if (LanguageUtils.isScala(psiClass.getLanguage())) {
            newImport = ScalaPsiRefactoringUtils.createScalaImport(project, unCommentedImport);
        }
        else {
            LOG.warn("Unsupported source file type "+fileTypeName);
        }
        return newImport;
    }

    private PsiElement createGroovyImport(Project project, String unCommentedImport) {
        //Groovy plugin dependant code - Start
        //            final GroovyPsiElementFactory factory = GroovyPsiElementFactory.getInstance(project);//Groovy is an optional dependency - no direct code dependency for now
        //            newImport = factory.createImportStatementFromText(unCommentedImport);
        //Groovy plugin dependant code - End
        //The reflective version - not dependant on plugin existence:
        PsiElement newImport = null;
        try {
            final Class<?> aClass = Class.forName("org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory");
            final Object service = ServiceManager.getService(project, aClass);
            if (service != null && aClass.isAssignableFrom(service.getClass())) {
                final Method method = aClass.getMethod("createImportStatementFromText", String.class);
                final Object psiImport = method.invoke(service, unCommentedImport);
                if (psiImport instanceof PsiElement) {
                    newImport=(PsiElement) psiImport;
                }
            }
        } catch (Throwable  e) {
            LOG.warn("Unable to use groovy plugin to replace commented import. groovy plugin might be disabled or not compatible with this TestMe plugin version",e);
        }
        return newImport;
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