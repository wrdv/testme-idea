package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.Nullable;

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
    private static Type DEFAULT_TYPE = new Type("java.lang.String", "String", "java.lang", false, false, false, false, new ArrayList<Type>());
    protected final int maxRecursionDepth;

    public JavaTestBuilderImpl(int maxRecursionDepth) {
        this.maxRecursionDepth = maxRecursionDepth;
    }

    //TODO consider aggregating conf into context object and managing maps outside of template
    @Override
    public String renderJavaCallParams(List<Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth) {
        final StringBuilder stringBuilder = new StringBuilder();
        buildJavaCallParams(null, params, replacementTypes, defaultTypeValues, recursionDepth, stringBuilder);
        return stringBuilder.toString();
    }

    @Override
    public String renderJavaCallParam(Type type, String strValue, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth) {
        final StringBuilder stringBuilder = new StringBuilder();
        buildCallParam(new SyntheticParam(type, strValue,false), replacementTypes, defaultTypeValues, recursionDepth, stringBuilder);
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

    protected void buildJavaCallParams(Type ownerType, List<? extends Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, StringBuilder testBuilder) {
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
                    if (isLooksLikeObjectKeyInGroovyMap(typeInitExp[i], genericTypeParam.getCanonicalName())) {
                        testBuilder.append("(");
                    }
                    buildJavaCallParams(type,Collections.singletonList(new SyntheticParam(genericTypeParam, genericTypeParam.getName(), false)), replacementTypes, defaultTypeValues, recursionDepth, testBuilder);
                    if (isLooksLikeObjectKeyInGroovyMap(typeInitExp[i], genericTypeParam.getCanonicalName())) {
                        testBuilder.append(")");
                    }
                    testBuilder.append(typeInitExp[i]);
                }
            } else if (shouldContinueRecursion(type, typeName, recursionDepth)) {
                final boolean hasEmptyConstructor = hasEmptyConstructor(type);
                Method foundCtor = findValidConstructor(type, replacementTypes, hasEmptyConstructor);
                if (foundCtor == null && !hasEmptyConstructor) {
                    testBuilder.append("null");
                } else {
                    testBuilder.append("new ");
                    testBuilder.append(typeName).append("(");
                    buildJavaCallParams(type,foundCtor==null?new ArrayList<Param>():foundCtor.getMethodParams(), replacementTypes, defaultTypeValues, recursionDepth, testBuilder);
                    testBuilder.append(")");
                }

            } else {
                testBuilder.append("null");
            }
        }
    }

    private boolean isLooksLikeObjectKeyInGroovyMap(String expFragment, String canonicalTypeName) {
        return ":".equals(expFragment) && !"java.lang.String".equals(canonicalTypeName);
    }

    @Nullable
    protected Method findValidConstructor(Type type, Map<String, String> replacementTypes, boolean hasEmptyConstructor) {
        Method foundCtor = null;
        for (Method method : type.getConstructors()) {
            if (isValidConstructor(type, method,hasEmptyConstructor,replacementTypes)) {
                foundCtor = method;
                break;
            }
        }
        return foundCtor;
    }

    private boolean shouldContinueRecursion(Type type, String typeName, int recursionDepth) {
        LOG.debug("recursionDepth:"+recursionDepth+". maxRecursionDepth "+maxRecursionDepth);
        return recursionDepth <= maxRecursionDepth && (typeName.equals(type.getName()) || typeName.equals(type.getCanonicalName()));
    }

    protected boolean isValidConstructor(Type type, Method constructor, boolean hasEmptyConstructor, Map<String, String> replacementTypes) {
        if (constructor.isPrivate()) {
            return false;
        }
        final List<Param> methodParams = constructor.getMethodParams();
        for (Param methodParam : methodParams) {
            final Type methodParamType = methodParam.getType();
            if (methodParamType.equals(type) && hasEmptyConstructor) {
                return false;
            }
            if ((methodParamType.isInterface() || methodParamType.isAbstract()) && (replacementTypes == null || replacementTypes.get(stripGenerics(methodParamType.getCanonicalName())) == null) && hasEmptyConstructor) {
                return false;
            }
          //todo consider prioritizing inline properties initialization if groovy + hasEmptyConstructor + more setters than ctor params
        }
        return true;
    }

    protected boolean hasEmptyConstructor(Type type) {
        if (type.getConstructors().size() == 0) {
            return true;
        }
        for (Method method : type.getConstructors()) {
            if (method.getMethodParams().size() == 0 && !method.isPrivate() && !method.isProtected()) {
                return true;
            }
        }
        return false;
    }
}
