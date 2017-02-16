package com.weirddev.testme.intellij.template.context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 2/16/2017
 *
 * @author Yaron Yamin
 */
public class TestBuilder {
    private static final Pattern GENERICS_PATTERN = Pattern.compile("(<.*>)");
    public String stripGenerics(String canonicalName) {
        return canonicalName.replaceFirst("<.*","");
    }
    String extractGenerics(String canonicalName) {
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
