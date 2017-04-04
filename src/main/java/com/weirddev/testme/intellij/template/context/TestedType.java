package com.weirddev.testme.intellij.template.context;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifier;

/**
 * Date: 12/31/2016
 *
 * @author Yaron Yamin
 */
public class TestedType extends Type {
    private final TestedType parentContainerClass;
    private final boolean isStatic;
    /**
     * A nested class on the tested nested class/s path
     */
    private final TestedType childNestedClass;

    public TestedType(PsiClass psiClass,TestedType childNestedClass) {//todo consider refactoring and unifying with Type
        super(psiClass.getQualifiedName());
        isStatic = psiClass.getModifierList() != null && psiClass.getModifierList().hasExplicitModifier(PsiModifier.STATIC);
        this.childNestedClass = childNestedClass;
        final PsiElement parent = psiClass.getParent();
        if (parent instanceof PsiClass) {
            this.parentContainerClass = new TestedType((PsiClass) parent, this);
        } else {
            this.parentContainerClass = null;
        }
    }
    public TestedType getParentContainerClass() {
        return parentContainerClass;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public TestedType getChildNestedClass() {
        return childNestedClass;
    }
}
