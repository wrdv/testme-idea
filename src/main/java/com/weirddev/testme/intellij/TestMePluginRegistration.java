package com.weirddev.testme.intellij;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.action.GotoTestOrCodeActionExt;
import com.weirddev.testme.intellij.utils.AccessLevelReflectionUtils;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.jetbrains.annotations.NotNull;

/**
 * Date: 10/15/2016
 *
 * @author Yaron Yamin
 */
public class TestMePluginRegistration implements ApplicationComponent {

    private static final String GOTO_TEST_ACTION_ID = "GotoTest";

    private static final Logger LOG = Logger.getInstance(TestMePluginRegistration.class.getName());

    @Override
    public void initComponent() {
        try {
            hackVelocity();
        } catch (Exception e) {
            LOG.error("couldn't initialize TestMe plugin",e);
            return;
        }
        ActionManager am = ActionManager.getInstance();
        AnAction action = new GotoTestOrCodeActionExt();
        final int baselineVersion = ApplicationInfo.getInstance().getBuild().getBaselineVersion();
        LOG.info("registering action on idea version:" + baselineVersion);
        if ("Y".equalsIgnoreCase(System.getProperty("IN_TEST_MODE")) /*|| baselineVersion < 192*/) {
            am.unregisterAction(GOTO_TEST_ACTION_ID);
        }
        am.registerAction(GOTO_TEST_ACTION_ID, action);
    }

    private void hackVelocity() throws Exception {
        AccessLevelReflectionUtils.replaceFinalStatic(RuntimeSingleton.class.getDeclaredField("ri"), new HackedRuntimeInstance());
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
