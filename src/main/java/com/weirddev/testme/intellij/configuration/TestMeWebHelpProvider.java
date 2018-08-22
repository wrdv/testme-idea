package com.weirddev.testme.intellij.configuration;

import com.intellij.openapi.application.ex.ApplicationInfoEx;
import com.intellij.openapi.help.WebHelpProvider;
import com.weirddev.testme.intellij.ui.settings.TestMeConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Date: 18/08/2018
 *
 * @author Yaron Yamin
 */
public class TestMeWebHelpProvider extends WebHelpProvider {
    static final String SETTINGS_DOCS_URL = "http://weirddev.com/testme/settings/?utm_source=jetbrains&utm_medium=idea&utm_content=help-settings&utm_campaign=";

    @Nullable
    public String getHelpPageUrl(@NotNull String helpTopicId) {
        if (TestMeConfigurable.settingsHelpId().equals(helpTopicId)) {
            return SETTINGS_DOCS_URL + ideaVersion();
        } else {
            return null;
        }
    }

    @NotNull
    public String getHelpTopicPrefix() {
        return TestMeConfigurable.PREFERENCES_TEST_ME_HELP_PREFIX;
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
