package com.weirddev.testme.intellij.template;

import com.intellij.psi.PsiParameter;

/**
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Param {
    private final Type type;
    private String name;

    public Param(PsiParameter psiParameter) {
        this.type = new Type(psiParameter.getType());
        this.name = psiParameter.getName();
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
