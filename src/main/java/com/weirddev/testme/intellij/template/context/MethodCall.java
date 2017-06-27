package com.weirddev.testme.intellij.template.context;

import java.util.List;

/**
 * Date: 23/06/2017
 *
 * @author Yaron Yamin
 */
public class MethodCall {
    private final Method method;
    private final List<MethodCallArgument> methodCallArguments;


    public MethodCall(Method method, List<MethodCallArgument> methodCallArguments) {

        this.method = method;
        this.methodCallArguments = methodCallArguments;
    }

    public Method getMethod() {
        return method;
    }

    public List<MethodCallArgument> getMethodCallArguments() {
        return methodCallArguments;
    }
}
