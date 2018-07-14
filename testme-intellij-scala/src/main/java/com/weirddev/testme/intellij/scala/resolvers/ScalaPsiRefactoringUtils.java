package com.weirddev.testme.intellij.scala.resolvers;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.plugins.scala.lang.psi.impl.ScalaPsiElementFactory;

/**
 * Date: 14/07/2018
 *
 * @author Yaron Yamin
 */
public class ScalaPsiRefactoringUtils {

    public static PsiElement createScalaImport(Project project, String unCommentedImport) {
        return ScalaPsiElementFactory.createImportFromText(unCommentedImport,PsiManager.getInstance(project));
    }
}