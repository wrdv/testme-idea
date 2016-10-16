package com.weirddev.testme.intellij;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.components.ApplicationComponent;
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
    public void disposeComponent() {//TODO check if OOB Action should be registered back here. test un-install scenarios
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "TestMe";
    }
}
