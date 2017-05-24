package com.weirddev.testme.intellij.template.context;

import java.util.List;
import java.util.Map;

/**
 * Date: 17/02/2017
 *
 * @author Yaron Yamin
 */
public interface LangTestBuilder {
    String renderJavaCallParams(List<Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues);

    String renderJavaCallParam(Type type, String strValue, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues);
}
