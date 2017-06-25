package com.weirddev.testme.intellij.template.context;

import java.util.ArrayList;

/**
 * Date: 23/06/2017
 *
 * @author Yaron Yamin
 */
public class MethodCall {
    private final Method method;
    private final ArrayList<MethodCallArgument> methodCallArguments;


    public MethodCall(Method method, ArrayList<MethodCallArgument> methodCallArguments) {

        this.method = method;
        this.methodCallArguments = methodCallArguments;
    }

    public Method getMethod() {
        return method;
    }

    public ArrayList<MethodCallArgument> getMethodCallArguments() {
        return methodCallArguments;
    }
}
