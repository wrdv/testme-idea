package com.weirddev.testme.intellij.template.context.impl;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.util.lang.JavaVersion;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.template.context.*;
import com.weirddev.testme.intellij.utils.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 24/02/2017
 *
 * @author Yaron Yamin
 */
public class GroovyTestBuilderImpl extends JavaTestBuilderImpl {
    private static final Logger LOG = Logger.getInstance(GroovyTestBuilderImpl.class.getName());

    public GroovyTestBuilderImpl(Method method, TestBuilder.ParamRole paramRole, FileTemplateConfig fileTemplateConfig, Module srcModule, TypeDictionary typeDictionary, JavaVersion javaVersion, Map<String, String> defaultTypeValues, Map<String, String> typesOverrides) {
        super(method, paramRole, fileTemplateConfig, srcModule, typeDictionary, javaVersion, defaultTypeValues, typesOverrides);
    }

    @Override
    protected void buildCallParam(StringBuilder testCodeString, Node<Param> paramNode) {
        final Type type = paramNode.getData().getType();
        if (isPropertyParam(paramNode.getData())) {
            testCodeString.append(paramNode.getData().getName()).append(" : ");
        }
        int arrayDimensions = type.getArrayDimensions();
        if (type.isArray()) {
            testCodeString.append("[".repeat(arrayDimensions));
        }
        buildJavaParam(testCodeString, paramNode);
        if (type.isArray()) {
            testCodeString.append("]".repeat(arrayDimensions)).append(" as ").append(type.getCanonicalName()).append("[]".repeat(arrayDimensions));
        }
    }

    @Override
    protected void buildCallParams(Method constructor, List<? extends Param> params, StringBuilder testBuilder, Node<Param> ownerParamNode) {
        final Type parentContainerClass = ownerParamNode.getData()!=null?ownerParamNode.getData().getType().getParentContainerClass():null;
        final boolean isNonStaticNestedClass = parentContainerClass != null && !ownerParamNode.getData().getType().isStatic();
        if (params != null && params.size()>0 || isNonStaticNestedClass) {
            if (isNonStaticNestedClass) {
                final Node<Param> parentContainerNode = new Node<Param>(new SyntheticParam(parentContainerClass, parentContainerClass.getName()), null, ownerParamNode.getDepth());
                buildCallParam(testBuilder,parentContainerNode);
                testBuilder.append(LangTestBuilder.PARAMS_SEPARATOR);
            }
            final int origLength = testBuilder.length();
            super.buildCallParams(constructor,params, testBuilder, ownerParamNode);
            if (isNonStaticNestedClass) {
                if (origLength == testBuilder.length()) {
                    testBuilder.delete(testBuilder.length() - LangTestBuilder.PARAMS_SEPARATOR.length(),testBuilder.length());
                }
            }
        } else if(ownerParamNode.getData()!=null){
            List<SyntheticParam> syntheticParams = findProperties(ownerParamNode.getData().getType());
            if (syntheticParams.size() > 0) {
                buildCallParams(null, syntheticParams, testBuilder, ownerParamNode);
            }
        }
    }

    @NotNull
    private List<SyntheticParam> findProperties(Type type) {
        final List<Method> methods = type.getMethods();
        Map<String,SyntheticParam> syntheticParams=new LinkedHashMap<String,SyntheticParam>();
        for (Method method : methods) {
            if (method.isSetter()&&  method.getMethodParams().size()>0 &&method.getPropertyName()!=null) {
                final SyntheticParam syntheticParam = syntheticParams.get(method.getPropertyName());
                if (syntheticParam == null || !propertyMatchesField(type,syntheticParam)) {
                    syntheticParams.put(method.getPropertyName(),new SyntheticParam(method.getMethodParams().get(0).getType(), method.getPropertyName(), SyntheticParam.UsageContext.Property));
                }
            }
        }
        return new ArrayList<SyntheticParam>(syntheticParams.values());
    }

    private boolean propertyMatchesField(Type type, SyntheticParam syntheticParam) {
        Field field = findFieldByName(type, syntheticParam.getName());
        return field != null && field.getType().getCanonicalName().equals(syntheticParam.getType().getCanonicalName());
    }

    @Nullable
    private Field findFieldByName(Type type, String propertyName) {
        for (Field field : type.getFields()) {
            if (propertyName.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    @Override
    protected String resolveNestedClassTypeName(String typeName) {
        return typeName;
    }

    @Nullable
    @Override
    protected Method findValidConstructor(Type type, boolean hasEmptyConstructor) {
        final Method constructor = super.findValidConstructor(type, hasEmptyConstructor);
        if (constructor == null) {
            return null;
        } else if (hasEmptyConstructor) {
            int noOfCtorArgs = constructor.getMethodParams().size();
            int noOfSetters = countSetters(type);
            if (shouldPreferSettersOverCtor(noOfCtorArgs, noOfSetters)) {
                return null; //Prefer default ctor and use setters instead
            } else {
                return constructor;
            }
        } else {
            return constructor;
        }
    }

    boolean shouldPreferSettersOverCtor(int noOfCtorArgs, int noOfSetters) {
        return 0 < noOfSetters && (noOfCtorArgs * ((fileTemplateConfig.getMinPercentOfExcessiveSettersToPreferMapCtor() + 100f) / 100f) <= ((float)noOfSetters) );
    }

    private int countSetters(Type type) {
        int noOfSetters = 0;
        for (Method method : type.getMethods()) {
            if (method.isSetter()) {
                noOfSetters++;
            }
        }
        return noOfSetters;
    }
}
