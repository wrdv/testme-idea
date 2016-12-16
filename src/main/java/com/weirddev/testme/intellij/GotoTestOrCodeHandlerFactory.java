package com.weirddev.testme.intellij;

import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.diagnostic.Logger;

import java.lang.reflect.Field;

/**
 * Date: 16/12/2016
 *
 * @author Yaron Yamin
 */
public class GotoTestOrCodeHandlerFactory {
    private static final Logger LOG = Logger.getInstance(GotoTestOrCodeActionExt.class.getName());
    public static GotoTestOrCodeHandlerExt create(boolean testMeOnlyMode) {
        GotoTestOrCodeHandlerExt gotoTestOrCodeHandlerExt = new GotoTestOrCodeHandlerExt(true,testMeOnlyMode);

        try {
            Field f = getAssignableFieldFor(TestMeActionCellRenderer.class);
            if (f == null) {
                LOG.warn("Unable to find field. TestMe plugin should get by without it, but some features might not be available for this IDEA version");
                return new GotoTestOrCodeHandlerExt(false,testMeOnlyMode);
            } else {
                f.setAccessible(true);
                f.set(gotoTestOrCodeHandlerExt, new TestMeActionCellRenderer());
                return gotoTestOrCodeHandlerExt;
            }
        } catch (IllegalAccessException e) {
            LOG.warn(e);
            return new GotoTestOrCodeHandlerExt(false,testMeOnlyMode);
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
