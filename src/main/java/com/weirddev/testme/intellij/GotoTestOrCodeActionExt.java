package com.weirddev.testme.intellij;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.testIntegration.GotoTestOrCodeAction;
import org.jetbrains.annotations.NotNull;

/**
 * Date: 10/15/2016
 * @author Yaron Yamin
 */
public class GotoTestOrCodeActionExt extends GotoTestOrCodeAction {

    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        return new GotoTestOrCodeHandlerExt();
    }
}
