package com.weirddev.testme.intellij.template.context.impl;

import com.weirddev.testme.intellij.template.context.Param;
import com.weirddev.testme.intellij.template.context.Type;

import java.util.List;
import java.util.Map;

/**
 * Date: 17/02/2017
 *
 * @author Yaron Yamin
 */
public interface LangTestBuilder {
    String PARAMS_SEPARATOR = ", ";
    String renderJavaCallParams(List<Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues);

    String renderJavaCallParam(Type type, String strValue, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues);
}
