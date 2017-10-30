package com.weirddev.testme.intellij.template.context;

import java.util.List;
import java.util.Set;

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
    public static boolean isMethodCalled(Method method,Set<MethodCall> methodCalls){
        for (MethodCall methodCall : methodCalls) {
            if (methodCall.getMethod().getMethodId().equals(method.getMethodId())) {
                return true;
            }
        }
        return false;
    }
}
