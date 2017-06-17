package com.weirddev.testme.intellij.groovy;

import com.intellij.lang.Language;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrParenthesizedExpression;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.path.GrMethodCallExpression;
import org.jetbrains.plugins.groovy.lang.psi.impl.statements.arguments.GrArgumentLabelImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Date: 09/05/2017
 *
 * @author Yaron Yamin
 */
public class GroovyPsiTreeUtils {

    public static final String GROOVY_LANGUAGE_ID = "Groovy";

    public static boolean isGroovy(Language language) {
        return language == com.intellij.lang.Language.findLanguageByID(GROOVY_LANGUAGE_ID) && LanguageUtils.isPluginEnabled(LanguageUtils.GROOVY_PLUGIN_ID);
    }

    public static PsiType resolveType(PsiElement prevSibling) {
        return prevSibling instanceof GrReferenceExpression ?((GrReferenceExpression) prevSibling).getType():null;
    }

    public static List<ResolvedReference> findReferences(PsiElement psiMethod){

        List<ResolvedReference> resolvedReferences =new ArrayList<ResolvedReference>();
        final Collection<GrReferenceExpression> grReferenceExpressions = PsiTreeUtil.findChildrenOfType(psiMethod, GrReferenceExpression.class);
        for (GrReferenceExpression grReferenceExpression : grReferenceExpressions) {
            final PsiType refType = grReferenceExpression.getType();
            if (refType != null) {
                final PsiType psiOwnerType = grReferenceExpression.getLastChild()==null?null:resolveOwnerType(grReferenceExpression.getLastChild());
                if (psiOwnerType != null) {
                    resolvedReferences.add(new ResolvedReference(grReferenceExpression.getReferenceName() , refType, psiOwnerType));
                }
            }
        }
        return resolvedReferences;
    }

    public static List<PsiMethod> findMethodCalls(PsiElement psiMethod){
        List<PsiMethod> psiMethods=new ArrayList<PsiMethod>();
        final Collection<GrMethodCallExpression> grMethodCallExpressions = PsiTreeUtil.findChildrenOfType(psiMethod, GrMethodCallExpression.class);
        for (GrMethodCallExpression grMethodCallExpression : grMethodCallExpressions) {
            final PsiMethod psiMethodResolved  = grMethodCallExpression.resolveMethod();
            if (psiMethodResolved != null) {
                psiMethods.add(psiMethodResolved);
            }
        }
        final Collection<GrArgumentLabelImpl> grArgLabels = PsiTreeUtil.findChildrenOfType(psiMethod, GrArgumentLabelImpl.class);
        for (GrArgumentLabelImpl grArgumentLabel : grArgLabels) {
            final PsiElement psiElement = grArgumentLabel.resolve();
            if (psiElement != null && psiElement instanceof PsiMethod) {
                final PsiMethod psiMethodResolved  = (PsiMethod) psiElement;
                psiMethods.add(psiMethodResolved);
            }
        }
        return psiMethods;
    }

    private static PsiType resolveOwnerType(PsiElement psiElement) {
        boolean dotAppeared = false;
        for(PsiElement prevSibling  = psiElement.getPrevSibling();prevSibling!=null;prevSibling=prevSibling.getPrevSibling()) {
            if(".".equals(prevSibling.getText())) {
                dotAppeared = true;
            }
            else if(dotAppeared && prevSibling instanceof GrReferenceExpression ) {
                return GroovyPsiTreeUtils.resolveType(prevSibling);
            }
        }
        return null;
    }

    public static PsiField resolveGrLeftHandExpressionAsField(PsiElement element) {
        if (!(element instanceof GrReferenceExpression)) {
            return null;
        }
        final GrReferenceExpression grExpr = (GrReferenceExpression) element;
        PsiElement parent = PsiTreeUtil.skipParentsOfType(grExpr, GrParenthesizedExpression.class);
        if (!(parent instanceof GrAssignmentExpression)) {
            return null;
        }
        final GrAssignmentExpression grAssignmentExpression = (GrAssignmentExpression) parent;
        final PsiReference reference = grAssignmentExpression.getLValue().getReference();
        final PsiElement possibleFieldElement = reference != null ? reference.resolve() : null;
        return possibleFieldElement == null || !(possibleFieldElement instanceof PsiField) ? null : (PsiField)possibleFieldElement ;
    }
}
