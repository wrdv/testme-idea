package com.weirddev.testme.intellij.common.utils;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;

/**
 * Date: 28/12/2017
 *
 * @author Yaron Yamin
 */
public class PsiMethodUtils {

    public static String formatMethodId(PsiMethod psiMethod) {
        String name = psiMethod.getName();
        String ownerClassCanonicalType = psiMethod.getContainingClass() == null ? null : psiMethod.getContainingClass().getQualifiedName();
        return ownerClassCanonicalType + "." + name + "(" + formatMethodParams(psiMethod.getParameterList().getParameters()) + ")";
    }

    static String formatMethodParams(PsiParameter[] parameters) {
        final StringBuilder sb = new StringBuilder();
        if (parameters != null) {
            for (PsiParameter parameter : parameters) {
                sb.append(parameter.getType().getCanonicalText()).append(",");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
