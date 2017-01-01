package com.weirddev.testme.intellij.utils;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.testIntegration.TestFinderHelper;
import com.intellij.testIntegration.TestFramework;
import com.weirddev.testme.intellij.CreateTestMeAction;
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
        return !TestFinderHelper.isTest(selectedElement) && isAvailableForElement(selectedElement);
    }
    static boolean isAvailableForElement(PsiElement element) {
        if (Extensions.getExtensions(TestFramework.EXTENSION_NAME).length == 0) return false;

        if (element == null) return false;

        PsiClass psiClass = CreateTestMeAction.getContainingClass(element);

        if (psiClass == null) return false;

        Module srcModule = ModuleUtilCore.findModuleForPsiElement(psiClass);
        if (srcModule == null) return false;

        return !(psiClass.isAnnotationType() || psiClass instanceof PsiAnonymousClass || psiClass.getModifierList()!=null&&psiClass.getModifierList().hasExplicitModifier(PsiModifier.PRIVATE) );
    }

}
