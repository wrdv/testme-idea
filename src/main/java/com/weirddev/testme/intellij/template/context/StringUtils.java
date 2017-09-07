package com.weirddev.testme.intellij.template.context;

/**
 * Date: 26/11/2016
 *
 * @author Yaron Yamin
 */
public class StringUtils extends org.apache.velocity.util.StringUtils {

    public static String deCapitalizeFirstLetter(String data) {
        return data.substring(0, 1).toLowerCase() + data.substring(1);
    }
    public static String camelCaseToWords(String data) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (String w : data.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
            stringBuilder.append(w).append(' ');
        }
        return stringBuilder.toString().trim();
    }

    public static boolean hasLine(String multiLineText, String toFind) {
        final String[] lines = multiLineText.split("\\n");
        for (String line : lines) {
            if (line.matches("\\s*"+toFind+"\\s*")) {
                return true;
            }
        }
        return false;
    }
}
