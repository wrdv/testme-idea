package com.weirddev.testme.intellij.scala.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 15/12/2017
 *
 * @author Yaron Yamin
 */
public class GenericsExpressionParser { // todo consolidate with other generics util methods and move to common module
    public static final char GENERICS_START = '<';
    public static final char GENERICS_END = '>';
    private static final Pattern SCALA_GENERICS_CONTENT_PATTERN = Pattern.compile(GENERICS_START + "(.*)" + GENERICS_END);

    public static ArrayList<String> extractGenericTypes(String canonicalName) {
            final ArrayList<String> types = new ArrayList<String>();
            String genericTypesContent = extractGenericTypesContent(canonicalName);
            if (genericTypesContent != null) {
                int startIndex = 0;
                while (genericTypesContent.length() > 0) {
                    final int typeSeparatorIndex = genericTypesContent.indexOf(",",startIndex);
                    final int typesStartIndex = genericTypesContent.indexOf(GENERICS_START,startIndex);
                    startIndex = 0;
                    if (typeSeparatorIndex == -1) {
                        types.add(genericTypesContent);
                        genericTypesContent = "";
                    }
                    else if (typesStartIndex == -1 || typeSeparatorIndex < typesStartIndex) {
                        final String type = genericTypesContent.substring(0, typeSeparatorIndex);
                        genericTypesContent = genericTypesContent.substring(typeSeparatorIndex+1);
                        types.add(type);
                    }
                    else if (typeSeparatorIndex > typesStartIndex) {
                        startIndex = findTypeParametersSkipIndex(genericTypesContent);
                    }
                }
            }
            return types;
        }

    private static int findTypeParametersSkipIndex(@NotNull String genericTypesContent) {
        int counter = 0;
        boolean found = false;
        for (int i = 0; i < genericTypesContent.length(); i++) {
            if (counter == 0 && found) {
                return i;
            }
            else if (genericTypesContent.charAt(i) == GENERICS_START) {
                found = true;
                counter++;
            } else if (genericTypesContent.charAt(i) == GENERICS_END) {
                counter--;
            }
        }
        return genericTypesContent.length()-1;
    }

    private static String extractGenericTypesContent(String canonicalName) {
                Matcher matcher = SCALA_GENERICS_CONTENT_PATTERN.matcher(canonicalName);
                if (matcher.find()) {
                    return matcher.group(1);
                } else {
                    return null;
                }
            }
}
