package com.weirddev.testme.intellij.resolvers.to;

import com.intellij.psi.PsiMethod;
import com.weirddev.testme.intellij.common.utils.PsiMethodUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * Date: 16/09/2017
 *
 * @author Yaron Yamin
 */
@Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString(onlyExplicitlyIncluded = true)
public class ResolvedMethodCall {
    private final PsiMethod psiMethod;
    private final List<MethodCallArg> methodCallArguments;
    @EqualsAndHashCode.Include @ToString.Include private final String methodId;

    public ResolvedMethodCall(PsiMethod psiMethod, List<MethodCallArg> methodCallArguments) {
        this.psiMethod = psiMethod;
        this.methodCallArguments = methodCallArguments;
        methodId = PsiMethodUtils.formatMethodId(psiMethod);
    }
}
