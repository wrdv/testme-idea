package com.weirddev.testme.intellij.scala.resolvers;

import com.intellij.psi.PsiClass;
import org.jetbrains.plugins.scala.lang.psi.api.toplevel.typedef.ScClass;

/**
 * Date: 18/11/2017
 *
 * @author Yaron Yamin
 */
public class ScalaTypeUtils {
    public static boolean isCaseClass(PsiClass psiClass) {
        if (psiClass instanceof ScClass) {
            return ((ScClass) psiClass).isCase();
        }
        return false;
    }

    public static boolean isEnum(PsiClass psiClass) {
        return "scala.Enumeration.Value".equals(psiClass.getQualifiedName());
    }

}
