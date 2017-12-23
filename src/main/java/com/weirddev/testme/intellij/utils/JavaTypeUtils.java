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

    public static String resolveCanonicalName(Object psiElement, Object typeElement) {
        String canonicalName = null;
        String scalaCanonicalName = null;
        if (typeElement instanceof PsiElement) {
            final PsiElement psiTypeElement = (PsiElement) typeElement;
            if (LanguageUtils.isScala(psiTypeElement.getLanguage())) {
                scalaCanonicalName = ScalaPsiTreeUtils.resolveParameterizedCanonicalName(psiTypeElement);
            }
        }
        else if (typeElement != null && LanguageUtils.isScalaPluginObject(typeElement)) {
            scalaCanonicalName = ScalaPsiTreeUtils.resolveCanonicalNameOfObject(typeElement);
        }
        if (scalaCanonicalName == null) {
            if (psiElement instanceof PsiType) {
                canonicalName = ((PsiType) psiElement).getCanonicalText();
            } else if (psiElement instanceof PsiClass) {
                canonicalName = ((PsiClass) psiElement).getQualifiedName();
            }
        } else {
            canonicalName = scalaCanonicalName;
        }

        return canonicalName;
    }

}
