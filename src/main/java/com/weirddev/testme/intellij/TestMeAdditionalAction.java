package com.weirddev.testme.intellij;

import com.intellij.codeInsight.navigation.GotoTargetHandler;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Date: 10/15/2016
 *
 * @author Yaron Yamin
 */
public abstract class TestMeAdditionalAction implements GotoTargetHandler.AdditionalAction {

    private String text;
    private Icon icon;

    public TestMeAdditionalAction(String text, Icon icon) {
        this.text = text;
        this.icon = icon;
    }

    @NotNull
    @Override
    public String getText() {
        return text;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }
}
