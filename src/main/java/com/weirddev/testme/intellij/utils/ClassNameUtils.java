package com.weirddev.testme.intellij.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Date: 15/11/2016
 *
 * @author Yaron Yamin
 */
public class ClassNameUtils {

    public static  boolean isArray(String canonicalName) {
        return canonicalName.endsWith("[]");
    }

    public static String extractClassName(@NotNull String fqName) {
        fqName = extractContainerType(fqName);
        int i = fqName.lastIndexOf('.');
        return stripArrayDesignator((i == -1 ? fqName : fqName.substring(i + 1)));
    }

    public static String extractPackageName(String className) {
        if (className != null) {
            className = extractContainerType(className);
            int i = className.lastIndexOf('.');
            return stripArrayDesignator(i == -1 ? "" : className.substring(0, i));
        }
        return null;
    }

    public static String stripArrayDesignator(String typeName) {
        return typeName.replace("[]", "");
    }

    @NotNull
    public static String extractContainerType(String className) {
        int j = className.indexOf('<');
        className=j==-1?className:className.substring(0, j);
        return stripArrayDesignator(className);
    }

    public static String extractTargetPropertyName(String name, boolean isSetter, boolean isGetter) {
        if (isGetter) {
            if (name.startsWith("get")) {
                return removeFromCamelCaseName(name, "get");
            } else if (name.startsWith("is")) {
                return removeFromCamelCaseName(name, "is");
            } else {
                return null;
            }
        } else if (isSetter && name.startsWith("set")) {
            return removeFromCamelCaseName(name, "set");
        }
        return null;
    }

    @NotNull
    public static String removeFromCamelCaseName(String name, String set) {
        final String removed = name.replaceFirst(set, "");
        return removed.substring(0, 1).toLowerCase()+removed.substring(1,removed.length());
    }
}
