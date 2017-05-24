package com.weirddev.testme.intellij.groovy;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;

/**
 * Date: 14/05/2017
 *
 * @author Yaron Yamin
 */
public class LanguageUtils {//todo move to a common module
    public static final String GROOVY_PLUGIN_ID = "org.intellij.groovy";
    public static boolean isPluginEnabled(String pluginId){
        return PluginManager.isPluginInstalled(PluginId.getId(pluginId)) && !PluginManager.getDisabledPlugins().contains(pluginId);
    }
}
