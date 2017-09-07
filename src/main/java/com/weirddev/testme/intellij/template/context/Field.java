package com.weirddev.testme.intellij.template.context;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.weirddev.testme.intellij.utils.ClassNameUtils;

/**
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Field {
    private final Type type;
    private final boolean overridden;
    private final boolean isFinal;
    private final boolean isStatic;
    private final String ownerClassCanonicalName;
    private String name;

    public Field(PsiField psiField, PsiClass srcClass) {
        this.name = psiField.getName();
        type = new Type(psiField.getType(), null, 0);
        String canonicalText = srcClass.getQualifiedName();
        ownerClassCanonicalName = ClassNameUtils.stripArrayVarargsDesignator(canonicalText);
        overridden = isOverriddenInChild(psiField, srcClass);
        isFinal = psiField.getModifierList() != null && psiField.getModifierList().hasExplicitModifier(PsiModifier.FINAL);
        isStatic = psiField.getModifierList() != null && psiField.getModifierList().hasExplicitModifier(PsiModifier.STATIC);
    }
    private boolean isOverriddenInChild(PsiField psiField, PsiClass srcClass) {
        String srcQualifiedName = srcClass.getQualifiedName();
        String fieldClsQualifiedName = psiField.getContainingClass()==null?null:psiField.getContainingClass().getQualifiedName();
        return (srcQualifiedName!=null && fieldClsQualifiedName!=null &&  !srcQualifiedName.equals(fieldClsQualifiedName)) && srcClass.findFieldByName(psiField.getName(), false)!=null;
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

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public String getOwnerClassCanonicalName() {
        return ownerClassCanonicalName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;

        Field field = (Field) o;

        if (!type.equals(field.type)) return false;
        if (!ownerClassCanonicalName.equals(field.ownerClassCanonicalName)) return false;
        return name.equals(field.name);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + ownerClassCanonicalName.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
