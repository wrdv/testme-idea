package com.weirddev.testme.intellij.template.context;

import com.weirddev.testme.intellij.template.LangTestBuilderFactory;

import java.util.Map;

/**
 * Date: 22/04/2017
 *
 * @author Yaron Yamin
 */
public class TestBuilderImpl implements TestBuilder{
    private final LangTestBuilderFactory langTestBuilderFactory;
    private final int minPercentOfExcessiveSettersToPreferDefaultCtor;
    private int maxRecursionDepth;

    public TestBuilderImpl(int maxRecursionDepth, boolean shouldIgnoreUnusedProperties, int minPercentOfExcessiveSettersToPreferDefaultCtor) {
        this.maxRecursionDepth = maxRecursionDepth;
        langTestBuilderFactory = new LangTestBuilderFactory(maxRecursionDepth, shouldIgnoreUnusedProperties);
        this.minPercentOfExcessiveSettersToPreferDefaultCtor = minPercentOfExcessiveSettersToPreferDefaultCtor;
    }

    @Override
    public String renderMethodParams(String language /*todo should accept enum?*/, Method method, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception {
        return langTestBuilderFactory.createTestBuilder(language, method,true, minPercentOfExcessiveSettersToPreferDefaultCtor).renderJavaCallParams(method.getMethodParams(), replacementTypes, defaultTypeValues);
    }
    @Override
    public String renderReturnParam(String language /*todo should accept enum?*/, Method method,String defaultName, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception {
        return langTestBuilderFactory.createTestBuilder(language, method,false, minPercentOfExcessiveSettersToPreferDefaultCtor).renderJavaCallParam(method.getReturnType(),defaultName,replacementTypes, defaultTypeValues);
    }
}
