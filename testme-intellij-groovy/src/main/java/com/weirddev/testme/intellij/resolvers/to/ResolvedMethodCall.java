package com.weirddev.testme.intellij.resolvers.to;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;

import java.util.ArrayList;

/**
 * Date: 16/09/2017
 *
 * @author Yaron Yamin
 */
public class ResolvedMethodCall { //todo move to a common module
    private final PsiMethod psiMethod;
    private final ArrayList<String> methodCallArguments;
    private final String methodId;

    public ResolvedMethodCall(PsiMethod psiMethod, ArrayList<String> methodCallArguments) {
        this.psiMethod = psiMethod;
        this.methodCallArguments = methodCallArguments;
        methodId = formatMethodId(psiMethod);
    }

    public PsiMethod getPsiMethod() {
        return psiMethod;
    }

    public ArrayList<String> getMethodCallArguments() {
        return methodCallArguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResolvedMethodCall)) return false;

        ResolvedMethodCall that = (ResolvedMethodCall) o;

        return methodId.equals(that.methodId);
    }

    @Override
    public int hashCode() {
        return methodId.hashCode();
    }

    static String formatMethodId(PsiMethod psiMethod) {//todo move to a new common module (copied from com.weirddev.testme.intellij.template.context.Method)
        String name = psiMethod.getName();
        String ownerClassCanonicalType = psiMethod.getContainingClass() == null ? null : psiMethod.getContainingClass().getQualifiedName();
        return ownerClassCanonicalType + "." + name + "(" + formatMethodParams(psiMethod.getParameterList().getParameters()) + ")";

    }

    static String formatMethodParams(PsiParameter[] parameters) { //todo move to a new common module (copied from com.weirddev.testme.intellij.template.context.Method)
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
