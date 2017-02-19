package com.weirddev.testme.intellij.template;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import com.weirddev.testme.intellij.icon.IconTokensReplacerImpl;

/**
 * Date: 10/12/2016
 *
 * @author Yaron Yamin
 */
public class TemplateDescriptor {
    private String tokenizedDisplayName;
    private String displayName;
    private String filename;
    private String[] dependantPlugins;

    public TemplateDescriptor(String tokenizedDisplayName, String filename, String[] dependantPlugins) {
        this.tokenizedDisplayName = tokenizedDisplayName;
        this.displayName = IconTokensReplacerImpl.stripTokens(tokenizedDisplayName);
        this.filename = filename;
        this.dependantPlugins = dependantPlugins;
    }

    public String getTokenizedDisplayName() {
        return tokenizedDisplayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFilename() {
        return filename;
    }

    public String getTestClassFormat() {
        return "%sTest";
    }
    public boolean isEnabled(){
        if (dependantPlugins != null) {
            for (String pluginId : dependantPlugins) {
                if (!PluginManager.isPluginInstalled(PluginId.getId(pluginId))|| PluginManager.getDisabledPlugins().contains(pluginId)) {
                    return false;
                }
            }
        }
        return true;
    }
}
