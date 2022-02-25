package com.weirddev.testme.intellij.template.context.impl;

import com.intellij.openapi.module.Module;
import com.intellij.util.lang.JavaVersion;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.template.context.*;

import java.util.Map;

/**
 * Date: 22/04/2017
 *
 * @author Yaron Yamin
 */
public class TestBuilderImpl implements TestBuilder {
    
    private final LangTestBuilderFactory langTestBuilderFactory;

    public TestBuilderImpl(Language language, Module srcModule, TypeDictionary typeDictionary, FileTemplateConfig fileTemplateConfig, JavaVersion javaVersion) {
        langTestBuilderFactory = new LangTestBuilderFactory(language, srcModule, fileTemplateConfig,typeDictionary, javaVersion);
    }

    @Override
    public String renderMethodParams(Method method, Map<String, String> typesOverrides, Map<String, String> defaultTypeValues) throws Exception {
        return langTestBuilderFactory.createTestBuilder(method, ParamRole.Input, defaultTypeValues, typesOverrides).renderJavaCallParams(method.getMethodParams());
    }

    @Override
    public ParameterizedTestComponents buildPrameterizedTestComponents(Method method, Map<String, String> typesOverridesForReturn, Map<String, String> typesOverrides, Map<String, String> defaultTypeValues) throws Exception {
        //a temp solution for single dimension parameters
        final LangTestBuilder testBuilder = langTestBuilderFactory.createTestBuilder(method, ParamRole.Input, defaultTypeValues, typesOverrides);
        final LangTestBuilder testBuilderForReturn = langTestBuilderFactory.createTestBuilder(method, ParamRole.Output, defaultTypeValues, typesOverridesForReturn);
        final ParameterizedTestComponents parameterizedTestComponents = new ParameterizedTestComponents();
        StringBuilder sb=new StringBuilder();
        for (Param param : method.getMethodParams()) {
            final String value = testBuilder.renderJavaCallParam(param.getType(), param.getName());
            sb.append(param.getName()).append(", ");
            parameterizedTestComponents.getParamsMap().put(param.getName(), value);
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - LangTestBuilder.PARAMS_SEPARATOR.length(),sb.length());
        }
        if (method.hasReturn()) {
            final String value = testBuilderForReturn.renderJavaCallParam(method.getReturnType(), RESULT_VARIABLE_NAME);
            parameterizedTestComponents.getParamsMap().put(RESULT_VARIABLE_NAME, value);
        }
        parameterizedTestComponents.setMethodClassParamsStr(sb.toString());
        return parameterizedTestComponents;
    }

    @Override
    public String renderReturnParam(Method testedMethod, Type type, String defaultName, Map<String, String> typesOverrides, Map<String, String> defaultTypeValues) throws Exception {
        return langTestBuilderFactory.createTestBuilder(testedMethod, ParamRole.Output, defaultTypeValues, typesOverrides).renderJavaCallParam(type,defaultName);
    }
    @Override
    public String renderInitType(Type type, String defaultName, Map<String, String> typesOverrides, Map<String, String> defaultTypeValues) throws Exception {
        return langTestBuilderFactory.createTestBuilder(null,ParamRole.Input, defaultTypeValues, typesOverrides).renderJavaCallParam(type,defaultName);
    }
}
