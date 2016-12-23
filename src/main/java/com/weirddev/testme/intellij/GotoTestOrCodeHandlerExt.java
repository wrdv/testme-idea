package com.weirddev.testme.intellij;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.testIntegration.GotoTestOrCodeHandler;
import com.weirddev.testme.intellij.icon.Icons;
import com.weirddev.testme.intellij.utils.TestSubjectResolverUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Date: 10/15/2016
 * @author Yaron Yamin
 */
public class GotoTestOrCodeHandlerExt extends GotoTestOrCodeHandler {

    @Nullable
    @Override
    protected GotoData getSourceAndTargetElements(final Editor editor, final PsiFile file) {
        GotoData sourceAndTargetElements = super.getSourceAndTargetElements(editor, file);
        if (sourceAndTargetElements == null) return null;
        if (TestSubjectResolverUtils.isValidForTesting(editor, file)) {
            sourceAndTargetElements.additionalActions.add(0, new AdditionalAction() {
                @NotNull
                @Override
                public String getText() {
                    return "TestMe...";
                }

                @Override
                public Icon getIcon() {
                    return Icons.TEST_ME;
                }

                @Override
                public void execute() {
                    TestMeActionHandlerFactory.create().invoke(file.getProject(),editor,file);
                }
            } );
        }
        return sourceAndTargetElements;
    }
}
