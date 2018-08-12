package com.weirddev.testme.intellij.scala.resolvers;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import org.jetbrains.plugins.scala.lang.psi.api.toplevel.typedef.ScClass;

/**
 * Date: 18/11/2017
 *
 * @author Yaron Yamin
 */
public class ScalaTypeUtils {

    private static final String SCALA_ENUMERATION_VALUE = "scala.Enumeration.Value";

    public static boolean isCaseClass(PsiClass psiClass) {
        if (psiClass instanceof ScClass) {
            return ((ScClass) psiClass).isCase();
        }
        return false;
    }

    public static boolean isEnum(PsiClass psiClass) {
        return SCALA_ENUMERATION_VALUE.equals(psiClass.getQualifiedName());
    }

    public static boolean isEnum(PsiType psiElement) {
        return SCALA_ENUMERATION_VALUE.equals(psiElement.getCanonicalText());
    }
}
