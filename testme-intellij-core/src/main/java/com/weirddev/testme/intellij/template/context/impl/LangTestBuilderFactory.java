package com.weirddev.testme.intellij.template.context.impl;

import com.intellij.openapi.module.Module;
import com.intellij.util.lang.JavaVersion;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.template.context.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class LangTestBuilderFactory {
    private final Language language;
    private final FileTemplateConfig fileTemplateConfig;
    private final Module srcModule;
    private final TypeDictionary typeDictionary;
    @Nullable
    private final JavaVersion javaVersion;

    public LangTestBuilderFactory(Language language, Module srcModule, FileTemplateConfig fileTemplateConfig, TypeDictionary typeDictionary, @Nullable JavaVersion javaVersion) {
        this.language = language;
        this.fileTemplateConfig = fileTemplateConfig;
        this.srcModule = srcModule;
        this.typeDictionary = typeDictionary;
        this.javaVersion = javaVersion;
    }

    @NotNull
    public LangTestBuilder createTestBuilder(Method method, TestBuilder.ParamRole paramRole, Map<String, String> defaultTypeValues, Map<String, String> typesOverrides) {
        LangTestBuilder langTestBuilder;
        if (language == Language.Scala) {
            langTestBuilder = new ScalaTestBuilder(method, paramRole, fileTemplateConfig, srcModule, typeDictionary, javaVersion, defaultTypeValues, typesOverrides);
        } else if (language == Language.Groovy) {
            langTestBuilder = new GroovyTestBuilderImpl(method, paramRole, fileTemplateConfig, srcModule, typeDictionary, javaVersion, defaultTypeValues, typesOverrides);
        } else {
            langTestBuilder = new JavaTestBuilderImpl(method, paramRole, fileTemplateConfig, srcModule, typeDictionary, javaVersion, defaultTypeValues, typesOverrides);
        }
        return langTestBuilder;
    }
}