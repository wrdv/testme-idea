package com.weirddev.testme.intellij.template.context.impl;

import com.intellij.openapi.module.Module;
import com.intellij.util.lang.JavaVersion;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.template.context.*;
import com.weirddev.testme.intellij.utils.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Date: 17/11/2017
 *
 * @author Yaron Yamin
 */
public class ScalaTestBuilder extends JavaTestBuilderImpl {

    public static final String VALUE_TYPE_SUFFIX = ".Value";

    public ScalaTestBuilder(Method testedMethod, TestBuilder.ParamRole paramRole, FileTemplateConfig fileTemplateConfig, Module srcModule, TypeDictionary typeDictionary, JavaVersion javaVersion, Map<String, String> defaultTypeValues, Map<String, String> typesOverrides) {
        super(testedMethod, paramRole, fileTemplateConfig, srcModule, typeDictionary, javaVersion, defaultTypeValues, typesOverrides);
    }

    @NotNull
    @Override
    protected String resolveInitializerKeyword(Type type, Method foundCtor) {
//        if (type.isCaseClass() && foundCtor.isPrimaryConstructor()) {
//            return "";
//        } else {
//            return NEW_INITIALIZER;
//        }
        return NEW_INITIALIZER;
    }
    @Override
    protected void buildCallParam(StringBuilder testCodeString, Node<Param> paramNode) {
        final Type type = paramNode.getData().getType();
        if (type.isArray()) {
            testCodeString.append("Array(");
        }
        final Type parentContainerClass = type.getParentContainerClass();
        if (parentContainerClass != null && !type.isStatic()) {
            final Node<Param> parentContainerNode = new Node<Param>(new SyntheticParam(parentContainerClass, parentContainerClass.getName(), false), null, paramNode.getDepth());
            buildCallParam(testCodeString,parentContainerNode);
            testCodeString.append(".");
        }
        buildJavaParam(testCodeString,paramNode);
        if (type.isArray()) {
            testCodeString.append(")");
        }
    }

    @Override
    protected void renderEnumValue(StringBuilder testBuilder, Type type) {
        final String canonicalName = type.getCanonicalName();
        if (type.getEnumValues().size() > 0) {
            testBuilder.append(StringUtils.removeSuffix(canonicalName,VALUE_TYPE_SUFFIX)).append(".").append(type.getEnumValues().get(0));
        } else if (type.getChildObjectsQualifiedNames().size() > 0) {
            testBuilder.append(type.getChildObjectsQualifiedNames().get(0));
        }
    }

    @Override
    protected boolean hasEnumValues(Type type) {
        return type.getEnumValues().size() > 0 || type.getChildObjectsQualifiedNames().size() > 0 ;
    }
}
