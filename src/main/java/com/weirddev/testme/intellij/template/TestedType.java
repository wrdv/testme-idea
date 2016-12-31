package com.weirddev.testme.intellij.template;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifier;

/**
 * Date: 12/31/2016
 *
 * @author Yaron Yamin
 */
public class TestedType extends Type {
    private final TestedType parent;
    private final boolean isStatic;
    private final TestedType child;

    public TestedType(PsiClass psiClass,TestedType child) {
        super(psiClass.getQualifiedName());
        isStatic = psiClass.getModifierList() != null && psiClass.getModifierList().hasExplicitModifier(PsiModifier.STATIC);
        this.child = child;
        final PsiElement parent = psiClass.getParent();
        if (parent instanceof PsiClass) {
            this.parent = new TestedType((PsiClass) parent, this);
        } else {
            this.parent = null;
        }
    }
    public TestedType getParent() {
        return parent;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public TestedType getChild() {
        return child;
    }
}
