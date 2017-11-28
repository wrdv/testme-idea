package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;

import java.util.List;
import java.util.Map;
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
    public String formatSpockParamNamesTitle(Map<String,String> paramsMap, boolean methodHasReturn){
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
    public String formatSpockDataParameters(Map<String,String> paramsMap, String linePrefix){//todo - should accept Map<String,List<String>> paramsMap instead
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

    private boolean hasInputParams(Set<String> paramNameKeys) {
        return paramNameKeys.size() > 1 || paramNameKeys.size() == 1 && !paramNameKeys.contains(TestBuilder.RESULT_VARIABLE_NAME);
    }
}
