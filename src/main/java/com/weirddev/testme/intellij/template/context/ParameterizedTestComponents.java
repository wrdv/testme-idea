package com.weirddev.testme.intellij.template.context;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds string constructs for composing a parameterized test call.
 * Date: 10/11/2017
 *
 * @author Yaron Yamin
 */
@Data
public class ParameterizedTestComponents {
    /**
     * map of argument name - value for method call
     */
    private Map<String, String> paramsMap=new HashMap<String, String>();
    /**
     * parameterized method invocation expression
     */
    private String methodClassParamsStr;

}
