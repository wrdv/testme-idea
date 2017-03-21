package com.weirddev.testme.intellij.utils;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.testIntegration.TestFinderHelper;
import com.intellij.testIntegration.TestFramework;
import com.weirddev.testme.intellij.action.CreateTestMeAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Date: 17/12/2016
 *
 * @author Yaron Yamin
 */
public class TestSubjectResolverUtils {
    @Nullable
    public static PsiElement getTestableElement(@NotNull Editor editor, @NotNull PsiFile file) {
        PsiElement selectedElement = PsiUtilCore.getElementAtOffset(file, editor.getCaretModel().getOffset());
        final PsiElement testableElement = findTestableElement(selectedElement);
        return testableElement == null ? file.findElementAt(0) : testableElement;
    }

    public static boolean isValidForTesting(Editor editor, PsiFile file) {
        PsiElement selectedElement = PsiUtilCore.getElementAtOffset(file, editor.getCaretModel().getOffset());
        return findTestableElement(selectedElement)!=null;
    }

    private static PsiElement findTestableElement(PsiElement selectedElement) {
        if (selectedElement == null) {
            return null;
        }
        else if (canBeTested(selectedElement)) {
            return selectedElement;
        } else {
            return findTestableElement(selectedElement.getParent());
        }
    }

    private static boolean canBeTested(PsiElement element) {
        if (TestFinderHelper.isTest(element)) return false;
        if (Extensions.getExtensions(TestFramework.EXTENSION_NAME).length == 0) return false;
        if (element == null) return false;

        PsiClass psiClass = CreateTestMeAction.getContainingClass(element);

        if (psiClass == null) return false;

        Module srcModule = ModuleUtilCore.findModuleForPsiElement(psiClass);
        return srcModule != null && !(psiClass.isAnnotationType() || psiClass instanceof PsiAnonymousClass || psiClass.getModifierList() != null && psiClass.getModifierList().hasExplicitModifier(PsiModifier.PRIVATE));

    }

}
