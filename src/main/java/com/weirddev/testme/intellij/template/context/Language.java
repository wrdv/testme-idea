package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

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
        Optional<Language> optLang = Stream.of(Language.values()).filter(lang -> lang.name().toLowerCase().equals(language.toLowerCase())).findAny();
        return optLang.orElseGet(() -> {
                LOG.warn("Illegal language selected for mock builder:" + language + ". valid values:" + Arrays.toString(Language.values()));
                    return Language.Java;
        });
    }
}
