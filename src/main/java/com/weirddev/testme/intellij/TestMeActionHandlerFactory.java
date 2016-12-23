package com.weirddev.testme.intellij;

import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.diagnostic.Logger;

import java.lang.reflect.Field;

/**
 * Date: 16/12/2016
 *
 * @author Yaron Yamin
 */
public class TestMeActionHandlerFactory {
    private static final Logger LOG = Logger.getInstance(TestMeActionHandlerFactory.class.getName());
    public static TestMeActionHandler create() {
        TestMeActionHandler testMeActionHandler = new TestMeActionHandler();

        try {
            Field f = getAssignableFieldFor(TestMeActionCellRenderer.class);
            if (f == null) {
                LOG.warn("Unable to find field. TestMe plugin should get by without it, but some features might not be available for this IDEA version");
                return new TestMeActionHandler();
            } else {
                f.setAccessible(true);
                f.set(testMeActionHandler, new TestMeActionCellRenderer());
                return testMeActionHandler;
            }
        } catch (IllegalAccessException e) {
            LOG.warn(e);
            return new TestMeActionHandler();
        }
    }
    private static Field getAssignableFieldFor(Class cls) {
        Field[] declaredFields = GotoTargetHandler.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getType().isAssignableFrom(cls)) {
                return declaredField;
            }
        }
        return null;
    }
}
