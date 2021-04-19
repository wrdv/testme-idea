package com.weirddev.testme.intellij;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.weirddev.testme.intellij.utils.AccessLevelReflectionUtils;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 10/15/2016
 *
 * @author Yaron Yamin
 */
public class TestMePluginRegistration implements StartupActivity {

    private static final String GOTO_TEST_ACTION_ID = "GotoTest";

    private static final Logger LOG = Logger.getInstance(TestMePluginRegistration.class.getName());

    @Override
    public void runActivity(@NotNull Project project) {
        try {
            hackVelocity();
        } catch (Exception e) {
            LOG.error("couldn't initialize TestMe plugin",e);
            return;
        }
        Application application = ApplicationManager.getApplication();
        KeymapManager keymapManager = application == null ? null : application.getService(KeymapManager.class);
        if (keymapManager != null) {
            KeyboardShortcut shortcut = new KeyboardShortcut(KeyStroke.getKeyStroke("ctrl shift T"), null);
            Map<String, List<KeyboardShortcut>> conflicts = safeGetConflicts(keymapManager, shortcut);
            if (conflicts.size() == 0) {
                shortcut = new KeyboardShortcut(KeyStroke.getKeyStroke("meta shift T"), null);
                conflicts = safeGetConflicts(keymapManager, shortcut);
            }
            for (String actionId : conflicts.keySet()) {
                LOG.info("removing conflicting shortcut of action "+actionId);
                keymapManager.getActiveKeymap().removeShortcut(actionId,shortcut);
            }
        }
    }

    @NotNull
    private Map<String, List<KeyboardShortcut>> safeGetConflicts(KeymapManager keymapManager, KeyboardShortcut shortcut) {
        try {
            return keymapManager.getActiveKeymap().getConflicts(GOTO_TEST_ACTION_ID, shortcut);
        } catch (Exception ex) {
            LOG.warn("can't check for keyboard conflicts",ex);
            return new HashMap<>();
        }

    }

    private void hackVelocity() throws Exception {
        Field riField = RuntimeSingleton.class.getDeclaredField("ri");
        Object oldRI = AccessLevelReflectionUtils.getField(riField, new RuntimeSingleton());
        AccessLevelReflectionUtils.replaceField(riField, new HackedRuntimeInstance((RuntimeInstance)oldRI));
    }
}
