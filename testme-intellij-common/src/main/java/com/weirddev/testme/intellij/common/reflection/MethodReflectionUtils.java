package com.weirddev.testme.intellij.common.reflection;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

/**
 * Date: 30/12/2017
 *
 * @author Yaron Yamin
 */
public class MethodReflectionUtils {
    private static final Logger LOG = Logger.getInstance(MethodReflectionUtils.class.getName());
    @Nullable
    public static <U,T> T getReturnTypeReflectively(U object, Class<U> ownerClass, Class<T> returnClass, @Nullable String  methodName) {
        T returnInstance = null;
        try {
            Method delegateMethod = null;
            for (Method method : ownerClass.getDeclaredMethods()) {
                final Class<?>[] parameters = method.getParameterTypes();
                if (method.getReturnType()!=null && returnClass.isAssignableFrom( method.getReturnType()) && (parameters == null || parameters.length == 0) && (methodName == null || methodName.equals(method.getName()))) {
                    delegateMethod = method;
                }
            }
            if (delegateMethod != null) {
                delegateMethod.setAccessible(true);
                final Object obj = delegateMethod.invoke(object);
                if (obj != null && returnClass.isInstance(obj)) {
                    returnInstance = (T) obj;
                }
            }
        } catch (Exception e) {
            LOG.error("Failed to invoke a method returning "+ returnClass.getSimpleName()+" on type "+ownerClass.getSimpleName(), e);
        }
        if (returnInstance == null) {
            LOG.warn("Method returning "+ returnClass.getSimpleName()+" not found on type "+ownerClass.getSimpleName());
        }
        return returnInstance;
    }

    @Nullable
    public static  <T> T invokeMethodReflectivelyWithFallback(Object owner, Class<T> returnClass, String methodName, String fallbackMethodName) {
        Object resultObj = null;
        Method returnTypeMethod = null;
        try {
            returnTypeMethod = owner.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            LOG.info("first method search failed",e);
            try {
                returnTypeMethod = owner.getClass().getMethod(fallbackMethodName);
            } catch (NoSuchMethodException e1) {
                LOG.warn("second method search failed",e);
            }
        }
        Object returnObj = null;

        if (returnTypeMethod != null) {
            try {
                returnObj = returnTypeMethod.invoke(owner);
            } catch (Exception e) {
                LOG.error("method invocation failed",e);
            }
        }

        if(returnObj!=null && returnClass.isAssignableFrom(returnObj.getClass())){
            resultObj = returnObj;
        }
        return (T) resultObj;
    }
}
