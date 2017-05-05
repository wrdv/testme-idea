package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import com.weirddev.testme.intellij.utils.Node;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private static Type DEFAULT_TYPE = new Type("java.lang.String", "String", "java.lang", false, false, false, false, false, new ArrayList<Type>());
    protected final int maxRecursionDepth;

    public JavaTestBuilderImpl(int maxRecursionDepth) {
        this.maxRecursionDepth = maxRecursionDepth;
    }

    //TODO consider aggregating conf into context object and managing maps outside of template
    @Override
    public String renderJavaCallParams(List<Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) {
        final StringBuilder stringBuilder = new StringBuilder();
        buildCallParams(params, replacementTypes, defaultTypeValues, stringBuilder,new Node<Param>(null,null,0));
        return stringBuilder.toString();
    }

    @Override
    public String renderJavaCallParam(Type type, String strValue, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) {
        final StringBuilder stringBuilder = new StringBuilder();
        buildCallParam(replacementTypes, defaultTypeValues, stringBuilder,new Node<Param>(new SyntheticParam(type, strValue,false),null,0));
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
    //todo move to util class
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

    protected void buildCallParam(Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, StringBuilder testBuilder, Node<Param> paramNode) {
        final Type type = paramNode.getData().getType();
        if (type.isArray()) {
            testBuilder.append("new ").append(type.getCanonicalName()).append("[]{");
        }
        final Type parentContainerClass = type.getParentContainerClass();
        if (parentContainerClass != null && !type.isStatic()) {
            final Node<Param> parentContainerNode = new Node<Param>(new SyntheticParam(parentContainerClass, parentContainerClass.getName(), false), null, paramNode.getDepth());
            buildCallParam(replacementTypes, defaultTypeValues, testBuilder,parentContainerNode);
            testBuilder.append(".");
        }
        buildJavaParam(replacementTypes, defaultTypeValues, testBuilder,paramNode);
        if (type.isArray()) {
            testBuilder.append("}");
        }
    }

    protected void buildCallParams(List<? extends Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, StringBuilder testBuilder, Node<Param> ownerParamNode) {
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                if (i != 0) {
                    testBuilder.append(", ");
                }
                buildCallParam(replacementTypes, defaultTypeValues, testBuilder, new Node<Param>(params.get(i), ownerParamNode,ownerParamNode.getDepth()+1));
            }
        }
    }
    protected void buildJavaParam(Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, StringBuilder testBuilder, Node<Param> paramNode) {
        final Type type = paramNode.getData().getType();
        final String canonicalName = type.getCanonicalName();
        if (defaultTypeValues.get(canonicalName) != null) {

            testBuilder.append(defaultTypeValues.get(canonicalName));
        } else if (canonicalName.equals("java.lang.String")) {
            testBuilder.append("\"").append(paramNode.getData().getName()).append("\"");
        } else if (type.getEnumValues().size() > 0) {
            testBuilder.append(canonicalName).append(".").append(type.getEnumValues().get(0));
        } else {
            String typeName = resolveType(type, replacementTypes);
            if (!canonicalName.equals(typeName)) {
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
                    buildCallParam(replacementTypes, defaultTypeValues, testBuilder, new Node<Param>(new SyntheticParam(genericTypeParam, genericTypeParam.getName(), false),paramNode,paramNode.getDepth()));
                    if (isLooksLikeObjectKeyInGroovyMap(typeInitExp[i], genericTypeParam.getCanonicalName())) {
                        testBuilder.append(")");
                    }
                    testBuilder.append(typeInitExp[i]);
                }
            }
            else if (shouldContinueRecursion(paramNode)) {
                final boolean hasEmptyConstructor = hasEmptyConstructor(type);
                Method foundCtor = findValidConstructor(type, replacementTypes, hasEmptyConstructor);
                if (foundCtor == null && !hasEmptyConstructor || !type.isDependenciesResolved()) {
                    testBuilder.append("null");
                } else {
                    testBuilder.append("new ");
                    if (type.getParentContainerClass() != null && !type.isStatic()) {
                        typeName = resolveNestedClassTypeName(typeName);
                    }
                    testBuilder.append(typeName).append("(");
                    buildCallParams(foundCtor==null?new ArrayList<Param>():foundCtor.getMethodParams(), replacementTypes, defaultTypeValues, testBuilder, paramNode);
                    testBuilder.append(")");
                }

            } else {
                testBuilder.append("null");
            }
        }
    }

    protected String resolveNestedClassTypeName(String typeName) {
        return ClassNameUtils.extractClassName(typeName);
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

    private boolean shouldContinueRecursion(Node<Param> paramNode) {
        LOG.debug("recursionDepth:"+paramNode.getDepth() +". maxRecursionDepth "+maxRecursionDepth);
        return paramNode.getDepth() <= maxRecursionDepth && !paramNode.hasSameAncestor();
    }

    protected boolean isValidConstructor(Type type, Method constructor, boolean hasEmptyConstructor, Map<String, String> replacementTypes) {
        if (type.isInterface() || type.isAbstract()) return false;
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
        if (type.isInterface() || type.isAbstract()) {
            return false;
        }
        if (type.isHasDefaultConstructor()) {
            return true;
        }
        for (Method method : type.getConstructors()) {
            if (method.getMethodParams().size() == 0) {
                return true;
            }
        }
        return false;
    }
}
