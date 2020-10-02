package com.weirddev.testme.intellij.configuration;

import com.intellij.openapi.application.ex.ApplicationInfoEx;
import com.intellij.openapi.help.WebHelpProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 18/08/2018
 *
 * @author Yaron Yamin
 */
public class TestMeWebHelpProvider extends WebHelpProvider {

    static final String SETTINGS_DOCS_URL = "https://weirddev.com/testme/%s/?utm_source=jetbrains&utm_medium=idea&utm_content=help-settings&utm_campaign=%s";

    public static final String PREFERENCES_TEST_ME_HELP_PREFIX = "com.weirddev.testme"; //plugin id
    public static final String PREFERENCES_TEST_ME_ID = "preferences.TestMe";
    public static final String TEMPLATES_TEST_ME_ID = "preferences.TestMe.templates";

    private static final Map<String,String> helpId2Uri = new HashMap<>();

    static {
        helpId2Uri.put(settingsHelpId(),"settings");
        helpId2Uri.put(templatesHelpId(),"custom-templates");
    }

    @Nullable
    public String getHelpPageUrl(@NotNull String helpTopicId) {
        String topicUri = helpId2Uri.get(helpTopicId);
        return topicUri == null ? null : String.format(SETTINGS_DOCS_URL, topicUri, ideaVersion());
    }

    @NotNull
    public String getHelpTopicPrefix() {
        return PREFERENCES_TEST_ME_HELP_PREFIX;
    }

    public static String settingsHelpId() {
        return PREFERENCES_TEST_ME_HELP_PREFIX + "."+ PREFERENCES_TEST_ME_ID;
    }

    public static String templatesHelpId() {
        return PREFERENCES_TEST_ME_HELP_PREFIX + "."+ TEMPLATES_TEST_ME_ID;
    }

    @NotNull
    private String ideaVersion() {
        ApplicationInfoEx info = ApplicationInfoEx.getInstanceEx();
        String minorVersion = info.getMinorVersion();
        int dot = minorVersion.indexOf('.');
        if (dot != -1) {
            minorVersion = minorVersion.substring(0, dot);
        }
        return info.getMajorVersion() + "." + minorVersion;
    }

}
