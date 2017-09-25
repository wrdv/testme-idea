package com.weirddev.testme.intellij.template;

import com.intellij.openapi.module.Module;
import com.weirddev.testme.intellij.template.context.*;
import org.jetbrains.annotations.NotNull;

public class LangTestBuilderFactory {
    private Language language;
    private FileTemplateConfig fileTemplateConfig;
    private Module srcModule;
    private TypeDictionary typeDictionary;

    public LangTestBuilderFactory(Language language, Module srcModule, FileTemplateConfig fileTemplateConfig, TypeDictionary typeDictionary) {
        this.language = language;
        this.fileTemplateConfig = fileTemplateConfig;
        this.srcModule = srcModule;
        this.typeDictionary = typeDictionary;
    }

    @NotNull
    public LangTestBuilder createTestBuilder(Method method, TestBuilder.ParamRole paramRole) throws Exception {
        LangTestBuilder langTestBuilder;
        if ( language==Language.Groovy) {
            //todo add replacementTypes, defaultTypeValues and testBuilder as members
            langTestBuilder = new GroovyTestBuilderImpl(method, paramRole,fileTemplateConfig, srcModule,typeDictionary);
        } else{
            langTestBuilder = new JavaTestBuilderImpl(method, paramRole, fileTemplateConfig, srcModule,typeDictionary);
        }
        return langTestBuilder;
    }
}