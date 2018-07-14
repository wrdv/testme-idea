package com.weirddev.testme.intellij.scala.resolvers;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.weirddev.testme.intellij.common.utils.LanguageUtils;

/**
 * Date: 14/07/2018
 *
 * @author Yaron Yamin
 */
public class ScalaPsiRefactoringUtils {
    public static PsiElement createScalaImport(Project project, String importText) {
        final PsiFile fileFromText = PsiFileFactory.getInstance(project).createFileFromText("dummy.scala" , LanguageUtils.getScalaLang(), importText);
        return fileFromText.getFirstChild();
    }
}