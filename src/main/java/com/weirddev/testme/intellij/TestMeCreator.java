package com.weirddev.testme.intellij;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testIntegration.GotoTestOrCodeHandler;
import com.intellij.testIntegration.JavaTestCreator;
import com.intellij.testIntegration.createTest.CreateTestAction;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Date: 10/18/2016
 *
 * @author Yaron Yamin
 * @see JavaTestCreator
 */
public class TestMeCreator {
    private static final Logger LOG = Logger.getInstance(TestMeCreator.class.getName());

    public void createTest(Editor editor, PsiFile file, String templateFilename) {
        try {
            PsiElement element = GotoTestOrCodeHandler.getSelectedElement(editor, file);
            if (CreateTestAction.isAvailableForElement(element)) {
                invoke(file.getProject(), editor, file.getContainingFile(),templateFilename);
            }
        }
        catch (IncorrectOperationException e) {
            LOG.warn(e);
        }
    }
    private void invoke(@NotNull Project project, Editor editor, PsiFile file, String templateFilename) throws IncorrectOperationException {
        if (!file.getManager().isInProject(file)) return;
        final PsiElement element = getElement(editor, file);
        if (element != null) {
            new CreateTestMeAction(templateFilename).invoke(project, editor, element);
        }
    }

    @Nullable
    private PsiElement getElement(@NotNull Editor editor, @NotNull PsiFile file) {
        PsiElement elementAtCaret = file.findElementAt(editor.getCaretModel().getOffset());
        return elementAtCaret == null ? file.findElementAt(0) : elementAtCaret;
    }
}
