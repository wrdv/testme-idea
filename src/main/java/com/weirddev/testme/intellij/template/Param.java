package com.weirddev.testme.intellij.template;

/**
 * Created by Admin on 24/10/2016.
 */
public class Param {
    private String name;
    private String shortType;
    private String canonicalType;
    public Param(String name, String shortType, String canonicalType) {
        this.name = name;
        this.shortType = shortType;
        this.canonicalType = canonicalType;
    }

    public String getName() {
        return name;
    }

    public String getCanonicalType() {
        return canonicalType;
    }

    public String getShortType() {
        return shortType;
    }
}
