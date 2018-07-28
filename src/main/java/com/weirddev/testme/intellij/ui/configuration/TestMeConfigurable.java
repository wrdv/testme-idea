package com.weirddev.testme.intellij.ui.configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.weirddev.testme.intellij.ui.popup.TestMeActionCellRenderer;
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

    private TestMeSettings testMeSettings;

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
        testMeSettings = new TestMeSettings();
        return testMeSettings.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return false;//todo check if conf UI is dirty
    }

    @Override
    public void apply() throws ConfigurationException {
        //todo update/save conf
    }

    @Override
    public void reset() {
        //todo reset conf
    }

    @Override
    public void disposeUIResources() {
        testMeSettings = null;
    }
}
