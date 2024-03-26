package com.weirddev.testme.intellij.ui.settings;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;
import com.weirddev.testme.intellij.configuration.TestMeConfig;

import javax.swing.*;

/**
 * Date: 28/07/2018
 *
 * @author Yaron Yamin
 */
public class TestMeSettingsForm implements Disposable {
    private JCheckBox generateTestsForInheritedCheckBox;
    private JCheckBox optimizeImportsCheckBox;
    private JCheckBox reformatCodeCheckBox;
    private JCheckBox replaceFullyQualifiedNamesCheckBox;
    private JPanel rootPanel;
    private JCheckBox renderInternalMethodCallStubsBox;
    private JCheckBox throwSpecificExceptionTypesCheckBox;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    @Override
    public void dispose() {
        Disposer.dispose(this);
    }

    public void persistState(TestMeConfig testMeConfig) {
        if (testMeConfig != null) {
            testMeConfig.setGenerateTestsForInheritedMethods(generateTestsForInheritedCheckBox.isSelected());
            testMeConfig.setOptimizeImports(optimizeImportsCheckBox.isSelected());
            testMeConfig.setReformatCode(reformatCodeCheckBox.isSelected());
            testMeConfig.setReplaceFullyQualifiedNames(replaceFullyQualifiedNamesCheckBox.isSelected());
            testMeConfig.setRenderInternalMethodCallStubs(renderInternalMethodCallStubsBox.isSelected());
            testMeConfig.setThrowSpecificExceptionTypes(throwSpecificExceptionTypesCheckBox.isSelected());

        }
    }

    public void reset(TestMeConfig state) {
        if (state != null) {
            generateTestsForInheritedCheckBox.setSelected(state.getGenerateTestsForInheritedMethods());
            optimizeImportsCheckBox.setSelected(state.getOptimizeImports());
            reformatCodeCheckBox.setSelected(state.getReformatCode());
            replaceFullyQualifiedNamesCheckBox.setSelected(state.getReplaceFullyQualifiedNames());
            renderInternalMethodCallStubsBox.setSelected(state.isRenderInternalMethodCallStubs());
            throwSpecificExceptionTypesCheckBox.setSelected(state.isThrowSpecificExceptionTypes());

        }
    }

    public boolean isDirty(TestMeConfig state) {
        return state != null &&
                (generateTestsForInheritedCheckBox.isSelected() != state.getGenerateTestsForInheritedMethods() ||
                        optimizeImportsCheckBox.isSelected() != state.getOptimizeImports() ||
                        reformatCodeCheckBox.isSelected() != state.getReformatCode() ||
                        replaceFullyQualifiedNamesCheckBox.isSelected() != state.getReplaceFullyQualifiedNames() ||
                        renderInternalMethodCallStubsBox.isSelected() != state.isRenderInternalMethodCallStubs()||
                        throwSpecificExceptionTypesCheckBox.isSelected() != state.isThrowSpecificExceptionTypes()
                );
    }
}
