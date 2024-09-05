package com.weirddev.testme.intellij.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author : Yaron Yamin
 * @since : 9/9/20
 **/
public class TemplateFileNameFormatter {
    public static String templateNameToFile(String name) {
        try {
            return URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("filename char not supported",e);
        }
    }
    public static String filenameToTemplateName(String name) {
        try {
            return URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("filename char not supported",e);
        }
    }
}
