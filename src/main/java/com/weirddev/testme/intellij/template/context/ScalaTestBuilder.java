package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.TypeDictionary;
import org.jetbrains.annotations.NotNull;

/**
 * Date: 17/11/2017
 *
 * @author Yaron Yamin
 */
public class ScalaTestBuilder extends JavaTestBuilderImpl {

    private static final Logger LOG = Logger.getInstance(ScalaTestBuilder.class.getName());

    public ScalaTestBuilder(Method testedMethod, TestBuilder.ParamRole paramRole, FileTemplateConfig fileTemplateConfig, Module srcModule, TypeDictionary typeDictionary) {
        super(testedMethod, paramRole, fileTemplateConfig, srcModule, typeDictionary);
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
}
