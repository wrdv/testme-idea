package com.weirddev.testme.intellij.template;

/**
 * Created by Admin on 24/10/2016.
 */
public class Field {
    private String name;
    private String canonicalType;
    private String shortType;
    private boolean isPrimitive;
    private boolean isFinal;

    public Field(String name, String canonicalType, String shortType, boolean isPrimitive, boolean isFinal) {
        this.name = name;
        this.canonicalType = canonicalType;
        this.shortType = shortType;
        this.isPrimitive = isPrimitive;
        this.isFinal = isFinal;
    }

    public String getName() {
        return name;
    }

    public String getCanonicalType() {
        return canonicalType;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public String getShortType() {
        return shortType;
    }
}
