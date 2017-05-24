package com.weirddev.testme.intellij.utils;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.weirddev.testme.intellij.groovy.ResolvedReference;
import com.weirddev.testme.intellij.template.context.Method;
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
            if (refType != null) {
                final PsiType psiOwnerType = psiReferenceExpression.getLastChild()==null?null: resolveOwnerType(psiReferenceExpression.getLastChild());
                if (psiOwnerType != null) {
                    resolvedReferences.add(new ResolvedReference(psiReferenceExpression.getReferenceName() , refType, psiOwnerType));
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
    public static List<PsiMethod> findMethodCalls(PsiMethod psiMethod) {
        List<PsiMethod> psiMethods=new ArrayList<PsiMethod>();
        final Collection<PsiMethodCallExpression> psiMethodCallExpressions = PsiTreeUtil.findChildrenOfType(psiMethod, PsiMethodCallExpression.class);
        for (PsiMethodCallExpression psiMethodCallExpression : psiMethodCallExpressions) {
            final PsiMethod psiMethodResolved = psiMethodCallExpression.resolveMethod();
            if (psiMethodResolved != null) {
                psiMethods.add(psiMethodResolved);
            }
        }
        return psiMethods;
    }
}
