package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 2/16/2017
 *
 * @author Yaron Yamin
 */
public class JavaTestBuilderImpl implements TestBuilder {
    private static final Logger LOG = Logger.getInstance(JavaTestBuilderImpl.class.getName());
    private static final Pattern GENERICS_PATTERN = Pattern.compile("(<.*>)");
    private static Type DEFAULT_TYPE = new Type("java.lang.String", "String", "java.lang", false, false, new ArrayList<Type>());
    private final int maxRecursionDepth;

    public JavaTestBuilderImpl(int maxRecursionDepth) {
        this.maxRecursionDepth = maxRecursionDepth;
    }

    //TODO consider aggregating conf into context object and managing maps outside of template
    @Override
    public String renderJavaCallParams(List<Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth) {
        final StringBuilder stringBuilder = new StringBuilder();
        buildJavaCallParams(params, replacementTypes, defaultTypeValues, recursionDepth, stringBuilder);
        return stringBuilder.toString();
    }

    @Override
    public String renderJavaCallParam(Type type, String strValue, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth) {
        final StringBuilder stringBuilder = new StringBuilder();
        buildCallParam(new Param(type, strValue), replacementTypes, defaultTypeValues, recursionDepth, stringBuilder);
        return stringBuilder.toString();
    }

    String resolveType(Type type, Map<String, String> replacementTypes) {
        String canonicalName = type.getCanonicalName();
        String sanitizedCanonicalName = stripGenerics(canonicalName);
        if (replacementTypes != null && replacementTypes.get(sanitizedCanonicalName) != null) {
            String replacedSanitizedCanonicalName = replacementTypes.get(sanitizedCanonicalName);
            canonicalName = replacedSanitizedCanonicalName.replace("<TYPES>", extractGenerics(canonicalName));
        }
        return canonicalName;
    }

    String stripGenerics(String canonicalName) {
        return canonicalName.replaceFirst("<.*", "");
    }

    String extractGenerics(String canonicalName) {
        Matcher matcher = GENERICS_PATTERN.matcher(canonicalName);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    protected void buildCallParam(Param param, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, StringBuilder testBuilder) {
        final Type type = param.getType();
        if (type.isArray()) {
            testBuilder.append("new ").append(type.getCanonicalName()).append("[]{");
        }
        buildJavaParam(param, replacementTypes, defaultTypeValues, recursionDepth, testBuilder);
        if (type.isArray()) {
            testBuilder.append("}");
        }
    }

    private void buildJavaCallParams(List<Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, StringBuilder testBuilder) {
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                if (i != 0) {
                    testBuilder.append(", ");
                }
                buildCallParam(params.get(i), replacementTypes, defaultTypeValues, recursionDepth+1, testBuilder);
            }
        }
    }

    protected void buildJavaParam(Param param, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, StringBuilder testBuilder) {
        final Type type = param.getType();
        final String canonicalName = type.getCanonicalName();
        if (defaultTypeValues.get(canonicalName) != null) {

            testBuilder.append(defaultTypeValues.get(canonicalName));
        } else if (canonicalName.equals("java.lang.String")) {
            testBuilder.append("\"").append(param.getName()).append("\"");
        } else if (type.getEnumValues().size() > 0) {
            testBuilder.append(canonicalName).append(".").append(type.getEnumValues().get(0));
        } else {
            String typeName = resolveType(type, replacementTypes);
            boolean isReplaced = false;
            if (!canonicalName.equals(typeName)) {
                isReplaced = true;
            }
            if (isReplaced) {
                final String[] typeInitExp = typeName.split("<VAL>");
                testBuilder.append(typeInitExp[0]);
                for (int i = 1; i < typeInitExp.length; i++) {
                    Type genericTypeParam;
                    if (type.getComposedTypes().size() >= i) {
                        genericTypeParam = type.getComposedTypes().get(i - 1);
                    } else {
                        genericTypeParam = DEFAULT_TYPE;
                    }
                    buildCtorParams(genericTypeParam, genericTypeParam.getName(), replacementTypes, defaultTypeValues, recursionDepth, true, testBuilder);
                    testBuilder.append(typeInitExp[i]);
                }
            } else {
                testBuilder.append("new ");
                testBuilder.append(typeName).append("(");
                buildCtorParams(type, typeName, replacementTypes, defaultTypeValues, recursionDepth, false, testBuilder);
                testBuilder.append(")");
            }
        }
    }

    private void buildCtorParams(Type type, String typeName, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, boolean isReplaced, StringBuilder testBuilder) {
        LOG.debug("recursionDepth:"+recursionDepth+". maxRecursionDepth "+maxRecursionDepth);
        if (recursionDepth <= maxRecursionDepth && (typeName.equals(type.getName()) || typeName.equals(type.getCanonicalName())) && type.getConstructors().size() > 0 || isReplaced) {
            buildJavaCallParams(isReplaced ? Collections.singletonList(new Param(type, typeName)) : type.getConstructors().get(0).getMethodParams(), replacementTypes, defaultTypeValues, recursionDepth, testBuilder);
        }
    }
}
