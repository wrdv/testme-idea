package com.weirddev.testme.intellij.resolvers.to;

import com.intellij.psi.PsiMethod;
import com.weirddev.testme.intellij.common.utils.PsiMethodUtils;

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
        methodId = PsiMethodUtils.formatMethodId(psiMethod);
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


}
