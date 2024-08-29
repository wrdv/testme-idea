package com.weirddev.testme.intellij.template.context.impl;

import com.weirddev.testme.intellij.template.context.Param;
import com.weirddev.testme.intellij.template.context.Type;

import java.util.List;

/**
 * Date: 17/02/2017
 *
 * @author Yaron Yamin
 */
public interface LangTestBuilder {
    String PARAMS_SEPARATOR = ", ";
    String renderJavaCallParams(List<Param> params);

    String renderJavaCallParam(Type type, String strValue);
}
