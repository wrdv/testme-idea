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
        this(typeDictionary.getType(psiParameter.getType(), maxRecursionDepth), psiParameter.getName());
    }

    public Param(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Param)) return false;

        Param param = (Param) o;

        return type != null ? type.equals(param.type) : param.type == null;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
