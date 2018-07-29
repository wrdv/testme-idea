package com.weirddev.testme.intellij.ui.settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Date: 28/07/2018
 *
 * @author Yaron Yamin
 */
public class TestMeSettingsForm {
    private final ActionListener updateDirtyStateListener;
    private JCheckBox generateTestsForInheritedCheckBox;
    private JCheckBox reformatCodeCheckBox;
    private JCheckBox replaceFullyQualifiedNamesCheckBox;
    private JCheckBox optimizeImportsCheckBox;
    private JPanel rootPanel;
    private boolean isDirty = false;

    public TestMeSettingsForm() {
        updateDirtyStateListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDirty = true;
            }
        };
        generateTestsForInheritedCheckBox.addActionListener(updateDirtyStateListener);
        reformatCodeCheckBox.addActionListener(updateDirtyStateListener);
        replaceFullyQualifiedNamesCheckBox.addActionListener(updateDirtyStateListener);
        optimizeImportsCheckBox.addActionListener(updateDirtyStateListener);
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JCheckBox getGenerateTestsForInheritedCheckBox() {
        return generateTestsForInheritedCheckBox;
    }

    public JCheckBox getReformatCodeCheckBox() {
        return reformatCodeCheckBox;
    }

    public JCheckBox getReplaceFullyQualifiedNamesCheckBox() {
        return replaceFullyQualifiedNamesCheckBox;
    }

    public JCheckBox getOptimizeImportsCheckBox() {
        return optimizeImportsCheckBox;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }
}
