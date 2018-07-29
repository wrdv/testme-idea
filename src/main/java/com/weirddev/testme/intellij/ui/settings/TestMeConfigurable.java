package com.weirddev.testme.intellij.ui.settings;

import com.intellij.openapi.options.SearchableConfigurable;
import com.weirddev.testme.intellij.configuration.TestMeConfig;
import com.weirddev.testme.intellij.configuration.TestMeConfigPersistent;
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
        return "preferences.TestMe";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "TestMe";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preferences.TestMe";//todo impl help topic internal and/or external
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        testMeSettingsForm = new TestMeSettingsForm();
        return testMeSettingsForm.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return testMeSettingsForm.isDirty();
    }

    @Override
    public void apply() {
        final TestMeConfig testMeConfig = testMeConfigPersistent.getState();
        if (testMeConfig != null) {
            testMeConfig.setGenerateTestsForInherited(testMeSettingsForm.getGenerateTestsForInheritedCheckBox().isSelected());
            testMeConfig.setOptimizeImports(testMeSettingsForm.getOptimizeImportsCheckBox().isSelected());
            testMeConfig.setReformatCode(testMeSettingsForm.getReformatCodeCheckBox().isSelected());
            testMeConfig.setReplaceFullyQualifiedNames(testMeSettingsForm.getReplaceFullyQualifiedNamesCheckBox().isSelected());
        }
        testMeSettingsForm.setDirty(false);
    }

    @Override
    public void reset() {
        //todo reset UI
        testMeSettingsForm.setDirty(false);
    }

    @Override
    public void disposeUIResources() {
        testMeSettingsForm = null;
    }
}
