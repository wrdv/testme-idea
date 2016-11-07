package com.weirddev.testme.intellij.template;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;

/**
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Field {
    private final Type type;
    private final boolean overridden;
    private final boolean finalType;
    private String name;

    public Field(PsiField psiField, PsiClass fieldTypeClass, PsiClass srcClass) {
        this.name = psiField.getName();
        type = new Type(psiField.getType());
        finalType = isFinal(fieldTypeClass);
        overridden = isOverriddenInChild(psiField, srcClass);
    }
    private boolean isOverriddenInChild(PsiField psiField, PsiClass srcClass) {
        String srcQualifiedName = srcClass.getQualifiedName();
        String fieldClsQualifiedName = psiField.getContainingClass()==null?null:psiField.getContainingClass().getQualifiedName();
        return (srcQualifiedName!=null && fieldClsQualifiedName!=null &&  !srcQualifiedName.equals(fieldClsQualifiedName)) && srcClass.findFieldByName(psiField.getName(), false)!=null;
    }
    private boolean isFinal(PsiClass aClass) {
        return aClass != null &&  aClass.getModifierList()!=null && aClass.getModifierList().hasExplicitModifier(PsiModifier.FINAL);
    }

    public boolean isOverridden() {
        return overridden;
    }

    public String getName() {
        return name;
    }
    public Type getType() {
        return type;
    }

    public boolean isFinalType() {
        return finalType;
    }
}
