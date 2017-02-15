package com.weirddev.testme.intellij.template.context;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 15/11/2016
 *
 * @author Yaron Yamin
 */
public class ClassUtils {

    private static final Pattern GENERICS_PATTERN = Pattern.compile("(<.*>)");

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
    public String stripGenerics(String canonicalName) {
        return canonicalName.replaceFirst("<.*","");
    }
    public String extractGenerics(String canonicalName) {
        Matcher matcher = GENERICS_PATTERN.matcher(canonicalName);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }
    public String replaceType(String type,String replacementType){
       return String.format(replacementType, extractGenerics(type));
    }
}
