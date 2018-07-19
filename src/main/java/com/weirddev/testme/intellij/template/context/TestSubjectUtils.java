package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Date: 31/03/2017
 *
 * @author Yaron Yamin
 */
@SuppressWarnings("unused")
public class TestSubjectUtils
{
    private static final Logger LOG = Logger.getInstance(TestSubjectUtils.class.getName());

    private static final Set<String> JAVA_FUTURE_TYPES = new HashSet<String>(Arrays.asList("java.util.concurrent.Future", "java.util.concurrent.CompletableFuture", "java.util.concurrent.RunnableFuture",
            "java.util.concurrent.ForkJoinTask.AdaptedRunnableAction", "java.util.concurrent.RunnableScheduledFuture", "java.util.concurrent.ScheduledThreadPoolExecutor.ScheduledFutureTask", "java.util.concurrent.FutureTask",
            "java.util.concurrent.ExecutorCompletionService.QueueingFuture", "java.util.concurrent.ForkJoinTask.AdaptedRunnable", "java.util.concurrent.ForkJoinTask.AdaptedCallable","java.util.concurrent.ForkJoinTask",
            "java.util.concurrent.ForkJoinTask.AdaptedRunnableAction", "java.util.concurrent.CountedCompleter","java.util.concurrent.RecursiveTask", "java.util.concurrent.ForkJoinTask.RunnableExecuteAction",
            "java.util.concurrent.CompletableFuture.AsyncSupply","java.util.concurrent.RecursiveAction","java.util.concurrent.CompletableFuture.Completion","java.util.concurrent.ScheduledFuture", "java.util.concurrent.RunnableScheduledFuture"));
    private static final Set<String> SCALA_FUTURE_TYPES = new HashSet<String>(Arrays.asList("scala.concurrent.Future","scala.concurrent.impl.Promise"));

    public static boolean hasTestableInstanceMethod(List<Method> methods) {
        for (Method method : methods) {
            if (method.isTestable() && !method.isStatic()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMethodCalled(Method method, Method byTestedMethod) {
        Set<MethodCall> methodCalls = byTestedMethod.getMethodCalls();
        boolean isMethodCalled = false;
        for (MethodCall methodCall : methodCalls) {
            if (methodCall.getMethod().getMethodId().equals(method.getMethodId())) {
                isMethodCalled = true;
                break;
            }
        }
        LOG.debug("method " + method.getMethodId() + " searched in " + methodCalls.size() + " method calls by tested method " + byTestedMethod.getMethodId() + " - is found:" + isMethodCalled);
        return isMethodCalled;
    }

    public String formatSpockParamNamesTitle(Map<String, String> paramsMap, boolean methodHasReturn) {
        StringBuilder sb = new StringBuilder();
        final Set<String> paramNameKeys = paramsMap.keySet();
        final String[] paramNames = paramNameKeys.toArray(new String[]{});
        for (String param : paramNames) {
            if (!TestBuilder.RESULT_VARIABLE_NAME.equals(param)) {
                if (sb.length() == 0) {
                    sb.append(" where ");
                }
                else if (sb.length() > 0) {
                    sb.append(" and ");
                }
                sb.append(param).append("=#").append(param);
            }
        }
        if (paramNameKeys.contains(TestBuilder.RESULT_VARIABLE_NAME) && sb.length() > 0) {
            sb.append(" then expect: #").append(TestBuilder.RESULT_VARIABLE_NAME);
        }
        return sb.toString();
    }

    public String formatSpockDataParameters(Map<String, String> paramsMap, String linePrefix) {//todo - should accept Map<String,List<String>> paramsMap instead
        StringBuilder sb = new StringBuilder();
        final Set<String> paramNameKeys = paramsMap.keySet();
        final boolean hasInputParams = hasInputParams(paramNameKeys);
        for (String param : paramNameKeys) {
            if (!TestBuilder.RESULT_VARIABLE_NAME.equals(param)) {
                if (sb.length() > 0) {
                    sb.append(" | ");
                }
                sb.append(param);
            }
        }
        if (hasInputParams) {
            sb.append(" || ").append(TestBuilder.RESULT_VARIABLE_NAME).append("\n");
        }
        else {
            sb.append(TestBuilder.RESULT_VARIABLE_NAME).append(" << ");
        }
        final int headerLength = sb.length();
        for (String param : paramNameKeys) {
            if (!TestBuilder.RESULT_VARIABLE_NAME.equals(param)) {
                if (headerLength < sb.length()) {
                    sb.append(" | ");
                }
                sb.append(paramsMap.get(param));
            }
        }
        String resultVal = paramsMap.get(TestBuilder.RESULT_VARIABLE_NAME);
        resultVal = resultVal == null ? "true" : resultVal;
        if (hasInputParams) {
            sb.append(" || ");
        }
        sb.append(resultVal);
        return sb.toString();
    }

    public boolean isJavaFuture(Type type) {
        for (String javaFutureType : JAVA_FUTURE_TYPES) {
            if (isSameGenericType(type, javaFutureType)) {
                return true;
            }
        }
        return isImplements(type, "java.util.concurrent.Future");
    }
    public static boolean isScalaFuture(Type type) {
        for (String javaFutureType : SCALA_FUTURE_TYPES) {
            if (isSameGenericType(type, javaFutureType)) {
                return true;
            }
        }
        return isImplements(type, "scala.concurrent.Future");
    }
    public boolean hasScalaFutureReturn(List<Method> methods) {
        for (Method method : methods) {
            if (method.isTestable() && method.hasReturn() && isScalaFuture(method.getReturnType())) {
                return true;
            }
        }
        return false;
    }
    public static @Nullable Method findOptimalConstructor(Type type){
        final Optional<Method> optPrimaryCtor = Optional.of(type.getMethods()).flatMap(methods -> methods.stream().filter(Method::isPrimaryConstructor).findAny());
        return optPrimaryCtor.orElse(findBiggestValidConstructor(type));
    }

    private static @Nullable Method findBiggestValidConstructor(Type type) {
        return type.findConstructors().stream().filter(Method::isAccessible).findFirst().orElse(null);
    }

    private static boolean isImplements(Type type, String classCanonicalName) {
        for (Type interfaceType : type.getImplementedInterfaces()) {
            if (isSameGenericType(interfaceType, classCanonicalName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSameGenericType(Type type, String classCanonicalName) {
        return classCanonicalName.equals(ClassNameUtils.stripGenerics(type.getCanonicalName()));
    }

    private boolean hasInputParams(Set<String> paramNameKeys) {
        return paramNameKeys.size() > 1 || paramNameKeys.size() == 1 && !paramNameKeys.contains(TestBuilder.RESULT_VARIABLE_NAME);
    }

    public static Set<String> getJavaFutureTypes() {
        return JAVA_FUTURE_TYPES;
    }
    public static Set<String> getScalaFutureTypes() {
        return SCALA_FUTURE_TYPES;
    }
}
