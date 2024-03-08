package com.weirddev.testme.intellij.utils;

import com.intellij.psi.*;
import com.weirddev.testme.intellij.common.utils.LanguageUtils;
import com.weirddev.testme.intellij.scala.resolvers.ScalaPsiTreeUtils;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.template.context.Type;

import java.util.ArrayList;
import java.util.List;

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
                scalaCanonicalName = ScalaPsiTreeUtils.resolveCanonicalName(psiTypeElement);
            }
        }
        else if (typeElement != null && LanguageUtils.isScalaPluginObject(typeElement)) {
            scalaCanonicalName = ScalaPsiTreeUtils.resolveCanonicalNameOfObject(typeElement,psiElement);
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

    /**
     * build annotations type from PsiAnnotation
     * @param annotations psi annotations
     * @param typeDictionary type dictionary
     * @param maxRecursionDepth depth
     * @return type list of psi annotation
     */
    public static List<Type> buildAnnotations(PsiAnnotation[] annotations, TypeDictionary typeDictionary,
        int maxRecursionDepth) {
        List<Type> annotationTypes = new ArrayList<>();
        if (null != annotations) {
            for (PsiAnnotation psiAnnotation : annotations) {
                PsiClassType psiClassType = resolveAnnotationType(psiAnnotation);
                if (null != psiClassType) {
                    annotationTypes.add(buildType(psiClassType, typeDictionary, maxRecursionDepth));
                }
            }
        }
        return annotationTypes;
    }


    private static PsiClassType resolveAnnotationType(PsiAnnotation psiAnnotation) {
        PsiClass psiClass = psiAnnotation.resolveAnnotationType();
        return psiClass != null ? Type.resolveType(psiClass) : null;
    }

    /**
     *  move the buildType method form Field class, and make it reusable for others
     * @param type PsiType
     * @param typeDictionary type dictionary
     * @param maxRecursionDepth recursion depth
     * @return the Type from PsiType
     */
    public static Type buildType(PsiType type, TypeDictionary typeDictionary, int maxRecursionDepth) {
        if (typeDictionary == null) {
            return new Type(type, null, null, 0, false);
        } else {
            return typeDictionary.getType(type, maxRecursionDepth, true);
        }
    }

}
