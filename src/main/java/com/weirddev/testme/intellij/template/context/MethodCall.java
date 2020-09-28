package com.weirddev.testme.intellij.template.context;

import lombok.Getter;

import java.util.List;

/**
 * Represents a method call invocation.
 * Date: 23/06/2017
 *
 * @author Yaron Yamin
 */
public class MethodCall {
    /**
     * Method being invoked
     */
    @Getter private final Method method;
    /**
     * Method call arguments being passed
     */
    @Getter private final List<MethodCallArgument> methodCallArguments;


    public MethodCall(Method method, List<MethodCallArgument> methodCallArguments) {

        this.method = method;
        this.methodCallArguments = methodCallArguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodCall)) return false;

        MethodCall that = (MethodCall) o;

        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        return methodCallArguments != null ? methodCallArguments.equals(that.methodCallArguments) : that.methodCallArguments == null;
    }

    @Override
    public int hashCode() {
        int result = method != null ? method.hashCode() : 0;
        result = 31 * result + (methodCallArguments != null ? methodCallArguments.hashCode() : 0);
        return result;
    }
}
