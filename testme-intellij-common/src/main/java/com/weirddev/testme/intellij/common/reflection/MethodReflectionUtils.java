package com.weirddev.testme.intellij.common.reflection;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Date: 30/12/2017
 *
 * @author Yaron Yamin
 */
public class MethodReflectionUtils {
    private static final Logger LOG = Logger.getInstance(MethodReflectionUtils.class.getName());
    @Nullable
    public static <T> T getReturnTypeReflectively(Object object, Class ownerClass, Class<T> returnClass, @Nullable String  methodName,Object... params) {
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
                final Object obj = delegateMethod.invoke(object,params);
                if (obj != null && returnClass.isAssignableFrom(obj.getClass()) /*returnClass.isInstance(obj)*/) {
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
//    @Nullable
//    public static Object callConstructorReflectively(Class clazz, Object... args) {
//        try {
//            final Constructor[] constructors = clazz.getConstructors();
//            LOG.debug("ctors by reflection:"+ Arrays.toString(constructors));
//            for (Constructor ctor : constructors) {
//                final Class<?>[] parameters = ctor.getParameterTypes();
//                final int paramsLength = args == null ? 0 : args.length;
//                if(paramsLength == parameters.length){
//                    //should find matching arg type, but this is good enough for now.
//                    return ctor.newInstance(args);
//                }
//            }
//        } catch (Exception e) {
//            LOG.error("Failed to invoke ctor for  "+ clazz.getSimpleName(), e);
//        }
//        return null;
//    }


    @Nullable
    public static  <T> T invokeMethodReflectivelyWithFallback(Object owner, Class<T> returnClass, String methodName, @Nullable String fallbackMethodName) {
        Object resultObj = null;
        Method returnTypeMethod = null;
        try {
            returnTypeMethod = owner.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            LOG.info("first method search failed",e);
            if (fallbackMethodName != null) {
                try {
                    returnTypeMethod = owner.getClass().getMethod(fallbackMethodName);
                } catch (NoSuchMethodException e1) {
                    LOG.warn("second method search failed",e);
                }
            }
        }
        Object returnObj = null;

        if (returnTypeMethod != null) {
            try {
                returnObj = returnTypeMethod.invoke(owner);
            } catch (Throwable e) {
                LOG.warn("method invocation failed",e);
            }
        }

        if(returnObj!=null && returnClass.isAssignableFrom(returnObj.getClass())){
            resultObj = returnObj;
        }
        return (T) resultObj;
    }
}
