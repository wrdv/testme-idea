package com.weirddev.testme.intellij;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testIntegration.GotoTestOrCodeHandler;
import com.intellij.testIntegration.TestFinderHelper;
import com.intellij.testIntegration.createTest.CreateTestAction;
import org.jetbrains.annotations.Nullable;

/**
 * Date: 10/15/2016
 * @author Yaron Yamin
 */
public class GotoTestOrCodeHandlerExt extends GotoTestOrCodeHandler {
    private final boolean supportIconTokens;
    private final IconTokensReplacer iconTokensReplacer;
    private final TestMeCreator testMeCreator;

    public GotoTestOrCodeHandlerExt(boolean supportIconTokens) {
        this(new IconTokensReplacerImpl(), new TestMeCreator(), supportIconTokens);
    }

    GotoTestOrCodeHandlerExt(IconTokensReplacer iconTokensReplacer, TestMeCreator testMeCreator, boolean supportIconTokens) {
        this.supportIconTokens = supportIconTokens;
        this.iconTokensReplacer = iconTokensReplacer;
        this.testMeCreator = testMeCreator;
    }

    @Nullable
    @Override
    protected GotoData getSourceAndTargetElements(final Editor editor, final PsiFile file) {
        GotoData sourceAndTargetElements = super.getSourceAndTargetElements(editor, file);
        if (sourceAndTargetElements == null) return null;
        PsiElement selectedElement = getSelectedElement(editor, file);
        if (!TestFinderHelper.isTest(selectedElement) && CreateTestAction.isAvailableForElement(selectedElement)) {
            String actionText = "TestMe with <JUnit>JUnit4 & <Mockito>Mockito";
            if (!supportIconTokens) {
                actionText=iconTokensReplacer.stripTokens(actionText);
            }
            sourceAndTargetElements.additionalActions.add(0, new TestMeAdditionalAction(actionText,Icons.TEST_ME) {
                @Override
                public void execute() {
                    testMeCreator.createTest(editor, file);
                }
            });
        }
        return sourceAndTargetElements;
    }
}
