package com.weirddev.testme.intellij.template.context;

import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.weirddev.testme.intellij.common.utils.LanguageUtils;
import com.weirddev.testme.intellij.scala.resolvers.ScalaPsiTreeUtils;
import com.weirddev.testme.intellij.template.TypeDictionary;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Defined Method argument.
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Param {
    /**
     * argument type
     */
    @Getter final Type type;
    /**
     * argument name
     */
    @Getter private String name;
    /**
     * class field assigment to by this argument, if any.
     */
    @Getter private final ArrayList<Field> assignedToFields;

    public Param(PsiParameter psiParameter, Optional<PsiType> substitutedType, TypeDictionary typeDictionary, int maxRecursionDepth, ArrayList<Field> assignedToFields, boolean shouldResolveAllMethods) {
        this(resolveType(psiParameter, substitutedType,shouldResolveAllMethods, typeDictionary, maxRecursionDepth), psiParameter.getName(),assignedToFields);
    }

    public Param(Type type, String name, ArrayList<Field> assignedToFields) {
        this.type = type;
        this.name = name;
        this.assignedToFields = assignedToFields;
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

    @Override
    public String toString() {
        return "Param{" + "name='" + name + ", type=" + type + '\'' + ", assignedToFields=" + assignedToFields + '}';
    }

    private static Type resolveType(PsiParameter psiParameter, Optional<PsiType> substitutedType, boolean shouldResolveAllMethods, TypeDictionary typeDictionary, int maxRecursionDepth) {

        Object element = null;
        if (LanguageUtils.isScala(psiParameter.getLanguage())) {
            element = ScalaPsiTreeUtils.resolveRelatedTypeElement(psiParameter);
        }
        return typeDictionary.getType(substitutedType.orElse(psiParameter.getType()), maxRecursionDepth, shouldResolveAllMethods,element);
    }
}
