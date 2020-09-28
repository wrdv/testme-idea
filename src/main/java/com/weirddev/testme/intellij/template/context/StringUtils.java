package com.weirddev.testme.intellij.template.context;

/**
 *
 * General string handling method utils, typically used in test templates.
 *
 * Date: 26/11/2016
 *
 * @author Yaron Yamin
 */
public class StringUtils extends org.apache.velocity.util.StringUtils {

    /**
     * @return the input data string where first letter converted to lower case
     */
    public static String deCapitalizeFirstLetter(String data) {
        return data.substring(0, 1).toLowerCase() + data.substring(1);
    }

    /**
     *
     * @param data string in camel case
     * @return input string separated to word from camel case format
     */
    public static String camelCaseToWords(String data) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (String w : data.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
            stringBuilder.append(w).append(' ');
        }
        return stringBuilder.toString().trim();
    }

    /**
     *
     * @param multiLineText text with possible new lines
     * @param toFind text to find
     * @return true - if toFind exists in text lines ( doesn't span in multiple lines)
     */
    public static boolean hasLine(String multiLineText, String toFind) {
        final String[] lines = multiLineText.split("\\n");
        for (String line : lines) {
            if (line.matches("\\s*"+toFind+"\\s*")) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param str input string, possibly containing input suffix
     * @param suffix possible suffix of input text
     * @return input str without the suffix if indeed is a suffix of input str
     */
    public static String removeSuffix(String str,String suffix){
        if (str.endsWith(suffix)) {
            return str.substring(0, str.length() - suffix.length());
        } else {
            return str;
        }
    }
}
