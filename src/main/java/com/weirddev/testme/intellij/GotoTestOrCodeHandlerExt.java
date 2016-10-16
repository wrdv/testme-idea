package com.weirddev.testme.intellij;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testIntegration.GotoTestOrCodeHandler;
import com.intellij.testIntegration.TestFinderHelper;
import org.jetbrains.annotations.Nullable;

/**
 * Date: 10/15/2016
 * @author Yaron Yamin
 */
public class GotoTestOrCodeHandlerExt extends GotoTestOrCodeHandler {
    private static final Logger LOG = Logger.getInstance(GotoTestOrCodeHandlerExt.class.getName());
    private final boolean supportIconTokens;
    private final IconTokensReplacer iconTokensReplacer=new IconTokensReplacer();

    public GotoTestOrCodeHandlerExt(boolean supportIconTokens) {
        this.supportIconTokens = supportIconTokens;
    }

    @Nullable
    @Override
    protected GotoData getSourceAndTargetElements(Editor editor, PsiFile file) {
        GotoData sourceAndTargetElements = super.getSourceAndTargetElements(editor, file);
        if (sourceAndTargetElements == null) return null;
        PsiElement selectedElement = getSelectedElement(editor, file);
        if (!TestFinderHelper.isTest(selectedElement)) {
            String actionText = "TestMe with <JUnit>JUnit4 & <Mockito>Mockito";
            if (!supportIconTokens) {
                actionText=iconTokensReplacer.stripTokens(actionText);
            }
            sourceAndTargetElements.additionalActions.add(0, new TestMeAdditionalAction(actionText,Icons.TEST_ME) {
                @Override
                public void execute() {
                    LOG.debug("Executing TestMe generator");
                    //TODO Implement Action
                }
            });
        }
        return sourceAndTargetElements;
    }
}
