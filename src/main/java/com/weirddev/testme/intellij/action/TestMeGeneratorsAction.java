package com.weirddev.testme.intellij.action;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.weirddev.testme.intellij.utils.TestSubjectResolverUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Date: 16/12/2016
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorsAction extends BaseGenerateAction {
    public TestMeGeneratorsAction() {
        super(new TestMeActionHandler());
    }

    @Override
    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        return TestSubjectResolverUtils.isValidForTesting(editor, file);
    }
}
