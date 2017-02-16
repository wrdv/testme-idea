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
}
