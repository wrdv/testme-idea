package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.module.Module;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.LangTestBuilderFactory;
import com.weirddev.testme.intellij.template.TypeDictionary;

import java.util.Map;

/**
 * Date: 22/04/2017
 *
 * @author Yaron Yamin
 */
public class TestBuilderImpl implements TestBuilder{
    private final LangTestBuilderFactory langTestBuilderFactory;

    public TestBuilderImpl(Language language, Module srcModule, TypeDictionary typeDictionary, FileTemplateConfig fileTemplateConfig) {
        langTestBuilderFactory = new LangTestBuilderFactory(language, srcModule, fileTemplateConfig,typeDictionary);
    }

    @Override
    public String renderMethodParams(Method method, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception {
        return langTestBuilderFactory.createTestBuilder(method, ParamRole.Input).renderJavaCallParams(method.getMethodParams(), replacementTypes, defaultTypeValues);
    }
    @Override
    public String renderReturnParam(Method testedMethod, Type type, String defaultName, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception {
        return langTestBuilderFactory.createTestBuilder(testedMethod, ParamRole.Output).renderJavaCallParam(type,defaultName,replacementTypes, defaultTypeValues);
    }
    @Override
    public String renderInitType(Type type, String defaultName, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception {
        return langTestBuilderFactory.createTestBuilder(null,null).renderJavaCallParam(type,defaultName,replacementTypes, defaultTypeValues);
    }
}
