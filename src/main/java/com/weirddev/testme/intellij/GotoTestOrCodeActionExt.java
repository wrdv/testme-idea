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
        Field f;
        try {
            f = GotoTargetHandler.class.getDeclaredField("myActionElementRenderer");
            f.setAccessible(true);
            f.set(gotoTestOrCodeHandlerExt, new TestMeActionCellRenderer());
            return gotoTestOrCodeHandlerExt;
        } catch (NoSuchFieldException e) {
            LOG.warn("Unable to find field. TestMe plugin should get by without it, but looks like it should be updated for this IDEA version",e);
        } catch (IllegalAccessException e) {
            LOG.warn(e);
        }
        return new GotoTestOrCodeHandlerExt(false);
    }
}
