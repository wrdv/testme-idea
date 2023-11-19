package com.weirddev.testme.intellij.ui.popup;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author : Yaron Yamin
 * @since : 9/25/20
 **/
public class ConfigurationLinkAction implements TestMePopUpHandler.AdditionalAction {
    @NotNull
    @Override
    public String getText() {
        return "Configure...";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.General.Settings;
    }

    @Override
    public void execute(Project project) {
        ShowSettingsUtil.getInstance().showSettingsDialog(project, "TestMe Templates");
    }
}
