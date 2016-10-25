package com.weirddev.testme.intellij.template;

import com.intellij.psi.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Method {
    private final Type returnType;
    private final String name;
    private final String ownerClassCanonicalType;
    private final List<Param> methodParams;
    private final boolean isPrivate;
    private final boolean isProtected;
    private final boolean isDefault;
    private final boolean isPublic;
    private final boolean isAbstract;
    private final boolean isNative;
    private final boolean isStatic;

    public Method(PsiMethod psiMethod) {
        isPrivate=psiMethod.hasModifierProperty(PsiModifier.PRIVATE);
        isProtected=psiMethod.hasModifierProperty(PsiModifier.PROTECTED);
        isDefault=psiMethod.hasModifierProperty(PsiModifier.DEFAULT);         //TODO research usage PACKAGE_LOCAL vs. DEFAULT
        isPublic=psiMethod.hasModifierProperty(PsiModifier.PUBLIC);
        isAbstract=psiMethod.hasModifierProperty(PsiModifier.ABSTRACT);
        isNative=psiMethod.hasModifierProperty(PsiModifier.NATIVE);
        isStatic=psiMethod.hasModifierProperty(PsiModifier.STATIC);
        returnType = psiMethod.getReturnType()==null?null:new Type(psiMethod.getReturnType(), null);
        name = psiMethod.getName();
        ownerClassCanonicalType = psiMethod.getContainingClass()==null?null:psiMethod.getContainingClass().getQualifiedName();
        methodParams = extractMethodParams(psiMethod.getParameterList());
    }

    private List<Param> extractMethodParams(PsiParameterList parameterList) {
        ArrayList<Param> params = new ArrayList<Param>();
        for (PsiParameter psiParameter : parameterList.getParameters()) {
            params.add(new Param(psiParameter));
        }
        return params;
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<Param> getMethodParams() {
        return methodParams;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public boolean isNative() {
        return isNative;
    }

    public boolean isStatic() {
        return isStatic;
    }
    @Nullable
    public String getOwnerClassCanonicalType() {
        return ownerClassCanonicalType;
    }
}
