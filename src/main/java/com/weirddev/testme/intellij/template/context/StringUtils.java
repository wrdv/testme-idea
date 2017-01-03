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

}
