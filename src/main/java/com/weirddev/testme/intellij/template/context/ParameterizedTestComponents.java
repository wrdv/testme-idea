package com.weirddev.testme.intellij.template.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 10/11/2017
 *
 * @author Yaron Yamin
 */
public class ParameterizedTestComponents {
    private Map<String, String> paramsMap=new HashMap<String, String>();
    private String methodClassParamsStr;

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public String getMethodClassParamsStr() {
        return methodClassParamsStr;
    }

    public void setMethodClassParamsStr(String methodClassParamsStr) {
        this.methodClassParamsStr = methodClassParamsStr;
    }
}
