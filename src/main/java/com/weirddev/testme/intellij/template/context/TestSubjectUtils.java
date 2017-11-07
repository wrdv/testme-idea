package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;

import java.util.List;
import java.util.Set;

/**
 * Date: 31/03/2017
 *
 * @author Yaron Yamin
 */
@SuppressWarnings("unused")
public class TestSubjectUtils {
    private static final Logger LOG = Logger.getInstance(TestSubjectUtils.class.getName());
    public static boolean hasTestableInstanceMethod(List<Method> methods){
        for (Method method : methods) {
            if (method.isTestable() && !method.isStatic()) {
                return true;
            }
        }
        return false;
    }
    public static boolean isMethodCalled(Method method, Method byTestedMethod){
        Set<MethodCall> methodCalls = byTestedMethod.getMethodCalls();
        boolean isMethodCalled = false;
        for (MethodCall methodCall : methodCalls) {
            if (methodCall.getMethod().getMethodId().equals(method.getMethodId())) {
                isMethodCalled = true;
                break;
            }
        }
        LOG.debug("method "+method.getMethodId()+" searched in "+methodCalls.size()+" method calls by tested method "+byTestedMethod.getMethodId()+" - is found:"+isMethodCalled);
        return isMethodCalled;
    }
}
