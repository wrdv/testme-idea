package com.weirddev.testme.intellij.template;

import com.intellij.psi.PsiParameter;

import java.util.Map;

/**
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Param {
    private final Type type;
    private String name;

    public Param(PsiParameter psiParameter, Map<String, Type> resolvedTypes, int maxRecursionDepth) {
        this.type = new Type(psiParameter.getType(), resolvedTypes, maxRecursionDepth);
        this.name = psiParameter.getName();
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
