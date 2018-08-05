package com.weirddev.testme.intellij.configuration;

import com.intellij.openapi.components.*;
import org.jetbrains.annotations.Nullable;

/**
 * Date: 29/07/2018
 *
 * @author Yaron Yamin
 */
@State(
        name="TestMeConfig",
        storages = {
                @Storage("TestMeConfig.xml")
        }
)
public class TestMeConfigPersistent implements PersistentStateComponent<TestMeConfig> {
    private TestMeConfig state;

    public static TestMeConfigPersistent getInstance() {
        return ServiceManager.getService(TestMeConfigPersistent.class);
    }

    @Nullable
    @Override
    public TestMeConfig getState() {
        synchronized (this) {
            if (state == null) {
                state = new TestMeConfig();
            }
        }
        return state;
    }

    @Override
    public void loadState(TestMeConfig state) {

        this.state = state;
    }
}
