package com.weirddev.testme.intellij.template.context;

import java.util.Map;

/**
 * Date: 24/02/2017
 *
 * @author Yaron Yamin
 */
public class GroovyTestBuilderImpl extends JavaTestBuilderImpl {
    public GroovyTestBuilderImpl(int maxRecursionDepth) {
        super(maxRecursionDepth);
    }

    @Override
    protected void buildCallParam(Param param, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, StringBuilder testBuilder) {
        final Type type = param.getType();
        if (type.isArray()) {
            testBuilder.append("[");
        }
        buildJavaParam(param, replacementTypes, defaultTypeValues, recursionDepth, testBuilder);
        if (type.isArray()) {
            testBuilder.append("] as ").append(type.getCanonicalName()).append("[]");
        }
    }
}
