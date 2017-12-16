package com.weirddev.testme.intellij.utils;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import com.weirddev.testme.intellij.resolvers.groovy.LanguageUtils;
import com.weirddev.testme.intellij.scala.resolvers.ScalaPsiTreeUtils;

/**
 * Date: 16/12/2017
 *
 * @author Yaron Yamin
 */
public class JavaTypeUtils {

    public static String resolveCanonicalName(Object psiElement, PsiElement typePsiElement) {
        String canonicalName = null;
        String scalaParameterizedCanonicalName = null;
        if (typePsiElement!=null &&  LanguageUtils.isScala(typePsiElement.getLanguage())) {
            scalaParameterizedCanonicalName = ScalaPsiTreeUtils.resolveParameterizedCanonicalName(typePsiElement);
        }
        if (scalaParameterizedCanonicalName == null) {
            if (psiElement instanceof PsiType) {
                canonicalName = ((PsiType) psiElement).getCanonicalText();
            } else if (psiElement instanceof PsiClass) {
                canonicalName = ((PsiClass) psiElement).getQualifiedName();
            }
        } else {
            canonicalName = scalaParameterizedCanonicalName;
        }
        return canonicalName;
    }
}
