package com.weirddev.testme.intellij.template.context;

import com.intellij.psi.*;
import com.weirddev.testme.intellij.template.TypeDictionary;

/**
 * Date: 12/31/2016
 *
 * @author Yaron Yamin
 */
public class TestedType extends Type {
    private final TestedType parentContainerClass;

    /**
     * A nested class on the tested nested class/s path
     */
    private final TestedType childNestedClass;

    public TestedType(PsiClass psiClass, TypeDictionary typeDictionary, int maxRecursionDepth, TestedType childNextedClass) {//todo consider refactoring and unifying with Type
        super(typeDictionary.getType(JavaPsiFacade.getInstance(psiClass.getProject()).getElementFactory().createType(psiClass), maxRecursionDepth));
        this.childNestedClass = childNextedClass;
        final PsiElement parent = psiClass.getParent();
        if (parent instanceof PsiClass) {
            this.parentContainerClass = new TestedType((PsiClass) parent,typeDictionary,maxRecursionDepth, this);
        } else {
            this.parentContainerClass = null;
        }
    }
    public TestedType getParentContainerClass() {
        return parentContainerClass;
    }

    public TestedType getChildNestedClass() {
        return childNestedClass;
    }
}
