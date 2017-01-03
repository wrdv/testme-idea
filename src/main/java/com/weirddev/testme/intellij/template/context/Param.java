package com.weirddev.testme.intellij.template.context;

import com.intellij.psi.PsiParameter;
import com.weirddev.testme.intellij.template.TypeDictionary;

/**
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Param {
    private final Type type;
    private String name;

    public Param(PsiParameter psiParameter, TypeDictionary typeDictionary, int maxRecursionDepth) {
        this.type = typeDictionary.getType(psiParameter.getType(), maxRecursionDepth);
        this.name = psiParameter.getName();
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
