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

    public TestBuilderImpl(Language language, int maxRecursionDepth, boolean shouldIgnoreUnusedProperties, int minPercentOfExcessiveSettersToPreferDefaultCtor) {
        langTestBuilderFactory = new LangTestBuilderFactory(language,maxRecursionDepth, shouldIgnoreUnusedProperties);
        this.minPercentOfExcessiveSettersToPreferDefaultCtor = minPercentOfExcessiveSettersToPreferDefaultCtor;
    }

    @Override
    public String renderMethodParams(Method method, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception {
        return langTestBuilderFactory.createTestBuilder(method,true, minPercentOfExcessiveSettersToPreferDefaultCtor).renderJavaCallParams(method.getMethodParams(), replacementTypes, defaultTypeValues);
    }
    @Override
    public String renderReturnParam(Method method, String defaultName, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception {
        return langTestBuilderFactory.createTestBuilder(method,false, minPercentOfExcessiveSettersToPreferDefaultCtor).renderJavaCallParam(method.getReturnType(),defaultName,replacementTypes, defaultTypeValues);
    }
}
