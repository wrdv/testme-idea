package com.weirddev.testme.intellij.utils;

import java.lang.reflect.Field;

/**
 * Date: 2/13/2017
 *
 * @author Yaron Yamin
 */
public class AccessLevelReflectionUtils {
    public static void replaceField(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        field.set(null, newValue);
    }
    public static Object getField(Field field, Object obj) throws Exception {
        field.setAccessible(true);
        return field.get(obj);
    }
}
