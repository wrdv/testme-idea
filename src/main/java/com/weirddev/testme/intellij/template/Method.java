package com.weirddev.testme.intellij.template;

import java.util.List;

/**
 * Created by Admin on 24/10/2016.
 */
public class Method {
    private String name;
    private String canonicalType;
    private String ownerClassCanonicalType;
    private List<Param> methodParams;
    private boolean isPrivate;
    private boolean isProtected;
    private boolean isDefault;
    private boolean isPublic;
    private boolean isAbstract;
    private boolean isNative;
    private boolean isStatic;

    public Method(String name, String canonicalType, String ownerClassCanonicalType, List<Param> params, boolean isPrivate, boolean isProtected, boolean isDefault, boolean isPublic, boolean isAbstract, boolean isNative, boolean isStatic) {
        this.name = name;
        this.canonicalType = canonicalType;
        this.ownerClassCanonicalType = ownerClassCanonicalType;
        this.methodParams = params;
        this.isPrivate = isPrivate;
        this.isProtected = isProtected;
        this.isDefault = isDefault;
        this.isPublic = isPublic;
        this.isAbstract = isAbstract;
        this.isNative = isNative;
        this.isStatic = isStatic;
    }

    public String getName() {
        return name;
    }

    public String getCanonicalType() {
        return canonicalType;
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

    public String getOwnerClassCanonicalType() {
        return ownerClassCanonicalType;
    }
}
