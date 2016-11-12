package com.weirddev.testme.intellij;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.testIntegration.GotoTestOrCodeAction;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * Date: 10/15/2016
 * @author Yaron Yamin
 */
public class GotoTestOrCodeActionExt extends GotoTestOrCodeAction {
    private static final Logger LOG = Logger.getInstance(GotoTestOrCodeActionExt.class.getName());
    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        GotoTestOrCodeHandlerExt gotoTestOrCodeHandlerExt = new GotoTestOrCodeHandlerExt(true);

        try {
            Field f = getAssignableFieldFor(TestMeActionCellRenderer.class);
            if (f == null) {
                LOG.warn("Unable to find field. TestMe plugin should get by without it, but some feature might not be available for this IDEA version");
                return new GotoTestOrCodeHandlerExt(false);
            } else {
                f.setAccessible(true);
                f.set(gotoTestOrCodeHandlerExt, new TestMeActionCellRenderer());
                return gotoTestOrCodeHandlerExt;
            }
        } catch (IllegalAccessException e) {
            LOG.warn(e);
            return new GotoTestOrCodeHandlerExt(false);
        }
    }
    private Field getAssignableFieldFor(Class cls) {
        Field[] declaredFields = GotoTargetHandler.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getType().isAssignableFrom(cls)) {
                return declaredField;
            }
        }
        return null;
    }
}
