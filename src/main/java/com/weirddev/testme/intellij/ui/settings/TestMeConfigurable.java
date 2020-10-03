package com.weirddev.testme.intellij.ui.settings;

import com.intellij.openapi.options.SearchableConfigurable;
import com.weirddev.testme.intellij.configuration.TestMeConfigPersistent;
import com.weirddev.testme.intellij.configuration.TestMeWebHelpProvider;
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

    private final TestMeConfigPersistent testMeConfigPersistent;
    private TestMeSettingsForm testMeSettingsForm;

    public TestMeConfigurable() {
        testMeConfigPersistent = TestMeConfigPersistent.getInstance();
    }

    @NotNull
    @Override
    public String getId() {
        return TestMeWebHelpProvider.PREFERENCES_TEST_ME_ID;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "TestMe";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return TestMeWebHelpProvider.settingsHelpId();
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
