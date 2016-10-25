package com.weirddev.testme.intellij.template;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

/**
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Field {
    private final Type type;
    private String name;

    public Field(PsiField psiField, PsiClass aClass) {
        this.name = psiField.getName();
        type = new Type(psiField.getType(),aClass);
    }

    public String getName() {
        return name;
    }
    public Type getType() {
        return type;
    }
}
