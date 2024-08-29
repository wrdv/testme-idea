package com.weirddev.testme.intellij.utils;

import com.weirddev.testme.intellij.template.context.MockitoMockBuilder;

import java.util.Set;

public class TypeUtils {

    private static final Set<String> BASIC_TYPES;
    private static final Set<String> LANGUAGE_BASE_CLASS = Set.of("java.lang.Object", "java.lang.Class", "groovy.lang.GroovyObjectSupport", "java.lang.Enum");

    static {
        BASIC_TYPES = MockitoMockBuilder.TYPE_TO_ARG_MATCHERS.keySet();
    }
    public static boolean isLanguageBaseClass(String typeCanonicalName) {
        return typeCanonicalName != null && LANGUAGE_BASE_CLASS.contains(typeCanonicalName);
    }

    public static boolean isBasicType(String qualifiedName) {
        return BASIC_TYPES.contains(qualifiedName);
    }
}
