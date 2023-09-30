package com.weirddev.testme.intellij.common.utils;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.lang.Language;
import com.intellij.openapi.extensions.PluginId;

/**
 * Date: 14/05/2017
 *
 * @author Yaron Yamin
 */
public class LanguageUtils {
    public static final String GROOVY_PLUGIN_ID = "org.intellij.groovy";
    public static final String SCALA_PLUGIN_ID = "org.intellij.scala";
    private static final String GROOVY_LANGUAGE_ID = "Groovy";
    private static final String SCALA_LANGUAGE_ID = "Scala";

    public static boolean isPluginEnabled(String pluginId){
        PluginId id = PluginId.getId(pluginId);
        return PluginManager.isPluginInstalled(id) && !PluginManagerCore.isDisabled(id);
    }

    public static boolean isGroovy(Language language) {
        return language == Language.findLanguageByID(GROOVY_LANGUAGE_ID) && isPluginEnabled(GROOVY_PLUGIN_ID);
    }
    public static boolean isScala(Language language) {
        return language == getScalaLang() && isPluginEnabled(SCALA_PLUGIN_ID);
    }

    public static Language getScalaLang() {
        return Language.findLanguageByID(SCALA_LANGUAGE_ID);
    }

    public static boolean isScalaPluginObject(Object typeElement) {
        return typeElement.getClass().getName().startsWith("org.jetbrains.plugins.scala.");
    }
}
