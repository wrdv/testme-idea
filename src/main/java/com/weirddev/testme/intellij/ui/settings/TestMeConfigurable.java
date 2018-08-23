package com.weirddev.testme.intellij.ui.settings;

import com.intellij.openapi.options.SearchableConfigurable;
import com.weirddev.testme.intellij.configuration.TestMeConfigPersistent;
import com.weirddev.testme.intellij.configuration.TestMeHelpManager;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Date: 28/07/2018
 *
 * @author Yaron Yamin
 */
public class TestMeConfigurable implements SearchableConfigurable {

    public static final String PREFERENCES_TEST_ME_HELP_ID = "Settings";
    public static final String PREFERENCES_TEST_ME_HELP_PREFIX = "com.weirddev.testme";
    private final TestMeConfigPersistent testMeConfigPersistent;
    private final TestMeHelpManager testMeHelpManager;
    private TestMeSettingsForm testMeSettingsForm;

    public TestMeConfigurable() {
        testMeConfigPersistent = TestMeConfigPersistent.getInstance();
        testMeHelpManager = new TestMeHelpManager();
    }

    @NotNull
    @Override
    public String getId() {
        return PREFERENCES_TEST_ME_HELP_ID;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "TestMe";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        Class<?> webHelpProviderClass = null;
        try {
            webHelpProviderClass = Class.forName("com.intellij.openapi.help.WebHelpProvider");
        } catch (ClassNotFoundException ignore) {
        }
        if (webHelpProviderClass == null) {
            testMeHelpManager.invokeHelp();
            return "http://invalid.url.please.ignore";
        } else {
            return settingsHelpId();
        }
    }

    public static String settingsHelpId() {
        return PREFERENCES_TEST_ME_HELP_PREFIX + "."+PREFERENCES_TEST_ME_HELP_ID;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        testMeSettingsForm = new TestMeSettingsForm();
        return testMeSettingsForm.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return testMeSettingsForm.isDirty(testMeConfigPersistent.getState());
    }

    @Override
    public void apply() {
        testMeSettingsForm.persistState(testMeConfigPersistent.getState());
    }

    @Override
    public void reset() {
        testMeSettingsForm.reset(testMeConfigPersistent.getState());
    }

    @Override
    public void disposeUIResources() {
        testMeSettingsForm.dispose();
        testMeSettingsForm = null;
    }
}
