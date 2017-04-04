package com.weirddev.testme.intellij.template.context;

import java.util.List;

/**
 * Date: 31/03/2017
 *
 * @author Yaron Yamin
 */
@SuppressWarnings("unused")
public class TestSubjectUtils {
    public static boolean hasTestableInstanceMethod(List<Method> methods){
        for (Method method : methods) {
            if (method.isTestable() && !method.isStatic()) {
                return true;
            }
        }
        return false;
    }
}
