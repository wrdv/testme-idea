package com.weirddev.testme.intellij.utils;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.weirddev.testme.intellij.resolvers.to.ResolvedReference;
import com.weirddev.testme.intellij.template.context.MethodCallArgument;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Date: 15/05/2017
 *
 * @author Yaron Yamin
 */
public class JavaPsiTreeUtils {
    @NotNull
    public static List<ResolvedReference> findReferences(PsiMethod psiMethod) {
        List<ResolvedReference> resolvedReferences =new ArrayList<ResolvedReference>();
        final Collection<PsiReferenceExpression> psiReferenceExpressions = PsiTreeUtil.findChildrenOfType(psiMethod, PsiReferenceExpression.class);
        for (PsiReferenceExpression psiReferenceExpression : psiReferenceExpressions) {
            final PsiType refType = psiReferenceExpression.getType();
            final PsiElement psiElement = psiReferenceExpression.resolve();
            if (refType != null && !(psiElement instanceof PsiMethod)) {
                final PsiType psiOwnerType = psiReferenceExpression.getLastChild()==null?null: resolveOwnerType(psiReferenceExpression.getLastChild());
                if (psiOwnerType != null) {
                    resolvedReferences.add(new ResolvedReference(psiReferenceExpression.getReferenceName() , refType, psiOwnerType));
                }
            }
        }
        return resolvedReferences;
    }
    @NotNull
    public static List<PsiMethod> findMethodReferences(PsiMethod psiMethod) {
        List<PsiMethod> resolvedReferences = new ArrayList<PsiMethod>();

        final Collection<PsiJavaToken> psiJavaTokens= PsiTreeUtil.findChildrenOfType(psiMethod, PsiJavaToken.class);
        for (PsiJavaToken psiJavaToken : psiJavaTokens) {
            if (JavaTokenType.DOUBLE_COLON == psiJavaToken.getTokenType() && psiJavaToken.getParent() instanceof PsiMethodReferenceExpression /*|| "::".equals(psiJavaToken.getText())*/) {
                final PsiMethodReferenceExpression psiMethodReferenceExpression = (PsiMethodReferenceExpression) psiJavaToken.getParent();
                final PsiElement resolved = psiMethodReferenceExpression.resolve();
                if (resolved instanceof PsiMethod) {
                    resolvedReferences.add(((PsiMethod) resolved));
                }
            }
        }
        return resolvedReferences;
    }
    private static PsiType resolveOwnerType(PsiElement psiElement) {
        boolean dotAppeared = false;
        for(PsiElement prevSibling  = psiElement.getPrevSibling();prevSibling!=null;prevSibling=prevSibling.getPrevSibling()) {
            if(".".equals(prevSibling.getText())) {
                dotAppeared = true;
            }
            else if(dotAppeared && prevSibling instanceof PsiExpression) {
                return ((PsiExpression) prevSibling).getType();
            }
        }
        return null;
    }

    @NotNull
    public static List<MethodCalled> findMethodCalls(PsiMethod psiMethod) {
        List<MethodCalled> methodCalled=new ArrayList<MethodCalled>();
        final Collection<PsiCallExpression> psiMethodCallExpressions = PsiTreeUtil.findChildrenOfType(psiMethod, PsiCallExpression.class);
        for (PsiCallExpression psiMethodCallExpression : psiMethodCallExpressions) {
            final PsiMethod psiMethodResolved = psiMethodCallExpression.resolveMethod();
            if (psiMethodResolved != null) {
                final PsiExpressionList argumentList = psiMethodCallExpression.getArgumentList();
                final ArrayList<MethodCallArgument> methodCallArguments = new ArrayList<MethodCallArgument>();
                if (argumentList != null) {
                    for (PsiElement psiElement : argumentList.getChildren()) {
                        if (psiElement instanceof PsiJavaToken || psiElement instanceof PsiWhiteSpace ) {
                            continue;
                        }
                        methodCallArguments.add(new MethodCallArgument(psiElement.getText()==null?"":psiElement.getText().trim()));
                    }
                }
                methodCalled.add(new MethodCalled(psiMethodResolved,methodCallArguments));
            }
        }
        return methodCalled;
    }

    public static class MethodCalled {
        private final PsiMethod psiMethod;
        private final ArrayList<MethodCallArgument> methodCallArguments;

        public MethodCalled(PsiMethod psiMethod, ArrayList<MethodCallArgument> methodCallArguments) {
            this.psiMethod = psiMethod;
            this.methodCallArguments = methodCallArguments;
        }

        public PsiMethod getPsiMethod() {
            return psiMethod;
        }

        public ArrayList<MethodCallArgument> getMethodCallArguments() {
            return methodCallArguments;
        }
    }
}
