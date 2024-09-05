package com.weirddev.testme.intellij.template;

import com.weirddev.testme.intellij.common.utils.LanguageUtils;
import com.weirddev.testme.intellij.icon.IconTokensReplacerImpl;
import com.weirddev.testme.intellij.template.context.Language;

import java.util.Arrays;
import java.util.Objects;

/**
 * Date: 10/12/2016
 *
 * @author Yaron Yamin
 */
public class TemplateDescriptor {
    private Language language;
    private String htmlDisplayName;
    private String displayName;
    private String tokenizedName;
    private String filename;
    private String[] dependantPlugins;
    private TemplateRole templateRole;

    TemplateDescriptor() { }

    public TemplateDescriptor(String htmlDisplayName, String tokenizedName, String filename, Language language, TemplateRole templateRole) {
        this.htmlDisplayName = htmlDisplayName;
        this.displayName = IconTokensReplacerImpl.stripTokens(htmlDisplayName);
        this.tokenizedName = tokenizedName;
        this.filename = filename;
        this.language = language;
        this.templateRole = templateRole;
        if (language == Language.Groovy) {
            this.dependantPlugins = new String[]{LanguageUtils.GROOVY_PLUGIN_ID};
        } else if (language == Language.Scala) {
            this.dependantPlugins = new String[]{LanguageUtils.SCALA_PLUGIN_ID};
        }
    }

    public String getHtmlDisplayName() {
        return htmlDisplayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFilename() {
        return filename;
    }
    public boolean isEnabled() {
        if (dependantPlugins != null) {
            for (String pluginId : dependantPlugins) {
                if (!LanguageUtils.isPluginEnabled(pluginId)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getTokenizedName() {
        return tokenizedName;
    }

    public Language getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "TemplateDescriptor{" +
                "language=" + language +
                ", htmlDisplayName='" + htmlDisplayName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", tokenizedName='" + tokenizedName + '\'' +
                ", filename='" + filename + '\'' +
                ", dependantPlugins=" + Arrays.toString(dependantPlugins) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateDescriptor that = (TemplateDescriptor) o;
        return language == that.language &&
                Objects.equals(htmlDisplayName, that.htmlDisplayName) &&
                Objects.equals(filename, that.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language, htmlDisplayName, filename);
    }

    public TemplateRole getTemplateRole() {
        return templateRole;
    }
}
