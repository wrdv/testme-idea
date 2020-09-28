package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Classifies test subject type
 * Date: 31/03/2017
 *
 * @author Yaron Yamin
 */
@SuppressWarnings("unused")
public class TestSubjectInspector
{
    private static final Logger LOG = Logger.getInstance(TestSubjectInspector.class.getName());

    private static final Set<String> JAVA_FUTURE_TYPES = new HashSet<>(Arrays.asList("java.util.concurrent.Future", "java.util.concurrent.CompletableFuture", "java.util.concurrent.RunnableFuture",
            "java.util.concurrent.ForkJoinTask.AdaptedRunnableAction", "java.util.concurrent.RunnableScheduledFuture", "java.util.concurrent.ScheduledThreadPoolExecutor.ScheduledFutureTask", "java.util.concurrent.FutureTask",
            "java.util.concurrent.ExecutorCompletionService.QueueingFuture", "java.util.concurrent.ForkJoinTask.AdaptedRunnable", "java.util.concurrent.ForkJoinTask.AdaptedCallable", "java.util.concurrent.ForkJoinTask",
            "java.util.concurrent.ForkJoinTask.AdaptedRunnableAction", "java.util.concurrent.CountedCompleter", "java.util.concurrent.RecursiveTask", "java.util.concurrent.ForkJoinTask.RunnableExecuteAction",
            "java.util.concurrent.CompletableFuture.AsyncSupply", "java.util.concurrent.RecursiveAction", "java.util.concurrent.CompletableFuture.Completion", "java.util.concurrent.ScheduledFuture", "java.util.concurrent.RunnableScheduledFuture"));
    private static final Set<String> SCALA_FUTURE_TYPES = new HashSet<String>(Arrays.asList("scala.concurrent.Future","scala.concurrent.impl.Promise"));
    private boolean generateTestsForInheritedMethods;

    public TestSubjectInspector(boolean generateTestsForInheritedMethods) {

        this.generateTestsForInheritedMethods = generateTestsForInheritedMethods;
    }

    public boolean hasTestableInstanceMethod(List<Method> methods) {
        for (Method method : methods) {
            if (shouldBeTested(method) && !method.isStatic()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true - if method should is testable according to it's access modifiers and TestMe configuration
     */
    public boolean shouldBeTested(Method method) {
        return method.isTestable() && ( generateTestsForInheritedMethods || !method.isInherited());
    }

    /**
     *
     * @param calledMethod a calledMethod, possible being called
     * @param callerMethod a calledMethod, possibly calling another
     * @return true - if callerMethod implementation invokes calledMethod
     */
    public boolean isMethodCalled(Method calledMethod, Method callerMethod) {
        Set<MethodCall> methodCalls = callerMethod.getMethodCalls();
        boolean isMethodCalled = false;
        for (MethodCall methodCall : methodCalls) {
            if (methodCall.getMethod().getMethodId().equals(calledMethod.getMethodId())) {
                isMethodCalled = true;
                break;
            }
        }
        LOG.debug("calledMethod " + calledMethod.getMethodId() + " searched in " + methodCalls.size() + " calledMethod calls by tested calledMethod " + callerMethod.getMethodId() + " - is found:" + isMethodCalled);
        return isMethodCalled;
    }

    /**
     *
     * @param paramsMap test params for spock parameterized test
     * @param methodHasReturn true - if test method returns anything
     * @return formatted string of spock test method name with tested parameters
     */
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

    /**
     * Consutrcuts a formatted string of parameterized params table for spock test. should probably be deprecated in the future, in favor of a method accepting paramsMap of Map<String,List<String>> for multiple values per input argument
     * @param paramsMap map of test arguments. for constructing a single parameterized row.
     * @param linePrefix prefix add to resulting test params, typically used for indentation when passing the required preceding white spaces
     * @return formatted string of parameterized params table for spock test.
     */
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

    /**
     * @return true - if type is a Java future
     */
    public boolean isJavaFuture(Type type) {
        for (String javaFutureType : JAVA_FUTURE_TYPES) {
            if (isSameGenericType(type, javaFutureType)) {
                return true;
            }
        }
        return isImplements(type, "java.util.concurrent.Future");
    }

    /**
     * @return true - if type is a Scala future
     */
    public boolean isScalaFuture(Type type) {
        for (String javaFutureType : SCALA_FUTURE_TYPES) {
            if (isSameGenericType(type, javaFutureType)) {
                return true;
            }
        }
        return isImplements(type, "scala.concurrent.Future");
    }

    /**
     * @return true - if any method has a Scala future type being returned
     */
    public boolean hasScalaFutureReturn(List<Method> methods) {
        for (Method method : methods) {
            if (shouldBeTested(method) && method.hasReturn() && isScalaFuture(method.getReturnType())) {
                return true;
            }
        }
        return false;
    }

    /**
     *  find an optimal constructor in type declaration. a constructor that seems best suited to initialize the type.
     * @param type a Type that has constructors
     * @return the optimal constructor found
     */
    public @Nullable Method findOptimalConstructor(Type type){
        final Optional<Method> optPrimaryCtor = Optional.of(type.getMethods()).flatMap(methods -> methods.stream().filter(Method::isPrimaryConstructor).findAny());
        return optPrimaryCtor.orElse(findBiggestValidConstructor(type));
    }

    /**
     * @return list of Java SDK types that represent a future. as fully qualified class names
     */
    public Set<String> getJavaFutureTypes() {
        return JAVA_FUTURE_TYPES;
    }

    /**
     * @return @return list of Scala SDK types that represent a future. as fully qualified class names
     */
    public Set<String> getScalaFutureTypes() {
        return SCALA_FUTURE_TYPES;
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


}
