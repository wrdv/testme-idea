package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Date: 22/04/2017
 *
 * @author Yaron Yamin
 */
public enum Language
{
    Scala, Groovy, Java;

    @NotNull
    private static final Logger LOG = Logger.getInstance(Language.class.getName());

    public static Language safeValueOf(String language) {
        Language languageEnum;
        try {
            languageEnum = Language.valueOf(language);
        }
        catch (IllegalArgumentException e) {
            LOG.warn("Illegal language selected for mock builder:" + language + ". valid values:" + Arrays.toString(Language.values()), e);
            languageEnum = Language.Java;
        }
        return languageEnum;
    }
}
