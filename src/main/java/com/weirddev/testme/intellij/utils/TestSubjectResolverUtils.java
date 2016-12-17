package com.weirddev.testme.intellij.utils;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.testIntegration.TestFinderHelper;
import com.intellij.testIntegration.createTest.CreateTestAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Date: 17/12/2016
 *
 * @author Yaron Yamin
 */
public class TestSubjectResolverUtils {
    @Nullable
    public static PsiElement getElement(@NotNull Editor editor, @NotNull PsiFile file) {
        PsiElement elementAtCaret = file.findElementAt(editor.getCaretModel().getOffset());
        return elementAtCaret == null ? file.findElementAt(0) : elementAtCaret;
    }

    public static boolean isValidForTesting(Editor editor, PsiFile file) {
        PsiElement selectedElement = PsiUtilCore.getElementAtOffset(file, editor.getCaretModel().getOffset());
        return !TestFinderHelper.isTest(selectedElement) && CreateTestAction.isAvailableForElement(selectedElement);
    }
}
