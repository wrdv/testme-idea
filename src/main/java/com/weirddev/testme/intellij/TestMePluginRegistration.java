package com.weirddev.testme.intellij;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.keymap.KeymapManager;
import com.weirddev.testme.intellij.utils.AccessLevelReflectionUtils;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.Map;

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
        Application application = ApplicationManager.getApplication();
        KeymapManager keymapManager = application == null ? null : application.getComponent(KeymapManager.class);
        if (keymapManager != null) {
            final KeyboardShortcut shortcut = new KeyboardShortcut(KeyStroke.getKeyStroke("ctrl shift T"), null);
            final Map<String, List<KeyboardShortcut>> conflicts = keymapManager.getActiveKeymap().getConflicts(GOTO_TEST_ACTION_ID, shortcut);
            for (String actionId : conflicts.keySet()) {
                LOG.info("removing conflicting shortcut of action "+actionId);
                keymapManager.getActiveKeymap().removeShortcut(actionId,shortcut);
            }
        }
    }
    private void hackVelocity() throws Exception {
        AccessLevelReflectionUtils.replaceField(RuntimeSingleton.class.getDeclaredField("ri"), new HackedRuntimeInstance());
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
