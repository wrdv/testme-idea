package com.weirddev.testme.intellij.template;

import com.weirddev.testme.intellij.template.context.*;
import org.jetbrains.annotations.NotNull;

public class LangTestBuilderFactory {
    private final boolean shouldIgnoreUnusedProperties;
    private Language language;
    private int maxRecursionDepth;

    public LangTestBuilderFactory(Language language, int maxRecursionDepth, boolean shouldIgnoreUnusedProperties) {
        this.language = language;
        this.maxRecursionDepth = maxRecursionDepth;
        this.shouldIgnoreUnusedProperties = shouldIgnoreUnusedProperties;
    }

    @NotNull
    public LangTestBuilder createTestBuilder(Method method, TestBuilder.ParamRole paramRole, int minPercentOfExcessiveSettersToPreferDefaultCtor) throws Exception {
        LangTestBuilder langTestBuilder;
        if ( language==Language.Groovy) {
            langTestBuilder = new GroovyTestBuilderImpl(maxRecursionDepth, method, shouldIgnoreUnusedProperties, paramRole, minPercentOfExcessiveSettersToPreferDefaultCtor, 66); //todo add replacementTypes, defaultTypeValues and testBuilder as members
        } else{
            langTestBuilder = new JavaTestBuilderImpl(maxRecursionDepth, method);
        }
        return langTestBuilder;
    }
}