package com.weirddev.testme.intellij.action;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.weirddev.testme.intellij.GotoTestOrCodeHandlerExt;
import com.weirddev.testme.intellij.GotoTestOrCodeHandlerFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Date: 16/12/2016
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorsAction extends BaseGenerateAction {
    public TestMeGeneratorsAction() {
        super(GotoTestOrCodeHandlerFactory.create(true));
    }

    @Override
    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        return GotoTestOrCodeHandlerExt.isValidForTesting(editor, file);
    }
}
