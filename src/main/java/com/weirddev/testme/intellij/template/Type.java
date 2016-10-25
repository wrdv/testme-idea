package com.weirddev.testme.intellij.template;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;

/**
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Type {
    private final String canonicalName;
    private final String name;
    private final boolean isPrimitive;
    private final boolean isFinal;//TODO consider refactoring - not relevant in some contexts when  aClass is null

    public Type(PsiType psiType, PsiClass aClass) {
        this.canonicalName = psiType.getCanonicalText();
        this.name = psiType.getPresentableText();
        this.isPrimitive = psiType instanceof PsiPrimitiveType;
        isFinal = isFinal(aClass);
    }
    private boolean isFinal(PsiClass aClass) {
        return aClass != null &&  aClass.getModifierList()!=null && !aClass.getModifierList().hasExplicitModifier(PsiModifier.FINAL);
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public boolean isFinal() {
        return isFinal;
    }
}
