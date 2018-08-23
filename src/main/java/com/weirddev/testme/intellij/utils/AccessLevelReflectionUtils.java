package com.weirddev.testme.intellij.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
/**
 * Date: 2/13/2017
 *
 * @author Yaron Yamin
 */
public class AccessLevelReflectionUtils {
    public static void replaceFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }
}
