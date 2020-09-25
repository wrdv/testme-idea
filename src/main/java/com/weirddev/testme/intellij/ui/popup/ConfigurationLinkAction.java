package com.weirddev.testme.intellij.ui.popup;

import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
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
        return IconLoader.getIcon("/general/Configure.png");
    }

    @Override
    public void execute(Project project) {
        ShowSettingsUtil.getInstance().showSettingsDialog(project, "TestMe Templates");
    }
}
