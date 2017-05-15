package com.weirddev.testme.intellij.groovy;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.path.GrMethodCallExpression;

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


}
