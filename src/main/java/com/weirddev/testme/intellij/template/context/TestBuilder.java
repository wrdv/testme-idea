package com.weirddev.testme.intellij.template.context;

import java.util.Map;

/**
 * Date: 22/04/2017
 *
 * @author Yaron Yamin
 */
@SuppressWarnings("unused")
public interface TestBuilder {
    String renderMethodParams(String language, Method method, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception;

    String renderReturnParam(String language, Method method, String defaultName, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception;
}
