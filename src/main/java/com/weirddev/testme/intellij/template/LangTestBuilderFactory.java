package com.weirddev.testme.intellij.template;

import com.weirddev.testme.intellij.template.context.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LangTestBuilderFactory {
    private final boolean shouldIgnoreUnusedProperties;
    private int maxRecursionDepth;

    public LangTestBuilderFactory(int maxRecursionDepth, boolean shouldIgnoreUnusedProperties) {
        this.maxRecursionDepth = maxRecursionDepth;
        this.shouldIgnoreUnusedProperties = shouldIgnoreUnusedProperties;
    }

    @NotNull
    public LangTestBuilder createTestBuilder(String languageName, Method method) throws Exception {
        final Language language = Language.valueOf(languageName);
        LangTestBuilder langTestBuilder;
        if (language == Language.Groovy) {
            langTestBuilder = new GroovyTestBuilderImpl(maxRecursionDepth, method, shouldIgnoreUnusedProperties); //todo add replacementTypes, defaultTypeValues and testBuilder as members
        } else if (language == Language.Java) {
            langTestBuilder = new JavaTestBuilderImpl(maxRecursionDepth, method);
        } else {
            throw new Exception("Unsupported Language for this test builder. language should be one of:" + Arrays.toString(Language.values()));
        }
        return langTestBuilder;
    }
}