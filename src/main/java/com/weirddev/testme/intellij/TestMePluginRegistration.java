package com.weirddev.testme.intellij;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.components.ApplicationComponent;
import com.weirddev.testme.intellij.action.GotoTestOrCodeActionExt;
import org.jetbrains.annotations.NotNull;

/**
 * Date: 10/15/2016
 *
 * @author Yaron Yamin
 */
public class TestMePluginRegistration implements ApplicationComponent {

    private static final String GOTO_TEST_ACTION_ID = "GotoTest";

    @Override
    public void initComponent() {
        ActionManager am = ActionManager.getInstance();
        AnAction action = new GotoTestOrCodeActionExt();
        am.unregisterAction(GOTO_TEST_ACTION_ID);
        am.registerAction(GOTO_TEST_ACTION_ID, action);
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "TestMe";
    }
}
