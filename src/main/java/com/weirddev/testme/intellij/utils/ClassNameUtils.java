package com.weirddev.testme.intellij.utils;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 15/11/2016
 *
 * @author Yaron Yamin
 */
public class ClassNameUtils {

    private static final Pattern GENERICS_PATTERN = Pattern.compile("(<.*>)");

    public static boolean isArray(String canonicalName) {
        return canonicalName.endsWith("[]");
    }
    public static int arrayDimensions(String canonicalName) {
        return canonicalName.split("\\[]", -1).length - 1;
    }
    public static String extractClassName(@NotNull String fqName) {
        fqName = extractContainerType(fqName);
        int i = fqName.lastIndexOf('.');
        return stripArrayVarargsDesignator((i == -1 ? fqName : fqName.substring(i + 1)));
    }

    public static String extractPackageName(String className) {
        if (className == null) {
            return null;
        } else {
            className = extractContainerType(className);
            int i = className.lastIndexOf('.');
            return stripArrayVarargsDesignator(i == -1 ? "" : className.substring(0, i));
        }
    }

    public static String stripArrayVarargsDesignator(String typeName) {
        return typeName.replace("[]", "").replace("...", "");
    }

    @NotNull
    public static String extractContainerType(String className) {
        int j = className.indexOf('<');
        className = j == -1 ? className : className.substring(0, j);
        return stripArrayVarargsDesignator(className);
    }

    public static String extractTargetPropertyName(String name, boolean isSetter, boolean isGetter) {
        if (isGetter && name.startsWith("get")) {
            return removeFromCamelCaseName(name, "get");
        } else if (isGetter && name.startsWith("is")) {
            return removeFromCamelCaseName(name, "is");
        } else if (isSetter && name.startsWith("set")) {
            return removeFromCamelCaseName(name, "set");
        } else return null;
    }

    public static String removeFromCamelCaseName(String name, String prefix) {
        final String removed = name.replaceFirst(prefix, "");
        return removed.length()==0 ? null:removed.substring(0, 1).toLowerCase() + removed.substring(1, removed.length());
    }

    public static boolean isVarargs(String canonicalText) {
        return canonicalText.endsWith("...");
    }

    public static final String stripGenerics(String canonicalName) {
        return canonicalName.replaceFirst("<.*", "");
    }

    public static final String extractGenerics(String canonicalName) {
        Matcher matcher = GENERICS_PATTERN.matcher(canonicalName);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }
}
