package com.weirddev.testme.intellij.utils;

import com.weirddev.testme.intellij.template.context.MockitoMockBuilder;

import java.util.Set;

public class TypeUtils {

    private static final Set<String> BASIC_TYPES;

    static {
        BASIC_TYPES = MockitoMockBuilder.TYPE_TO_ARG_MATCHERS.keySet();
    }
    public static boolean isLanguageBaseClass(String typeCanonicalName) {
        return "java.lang.Object".equals(typeCanonicalName) || "java.lang.Class".equals(typeCanonicalName) || "groovy.lang.GroovyObjectSupport".equals(typeCanonicalName);
    }

    public static boolean isBasicType(String qualifiedName) {
        return BASIC_TYPES.contains(qualifiedName);
    }
}
