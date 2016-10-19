package com.weirddev.testme.intellij;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testIntegration.JavaTestCreator;
import com.intellij.testIntegration.createTest.CreateTestAction;
import com.intellij.util.IncorrectOperationException;

/**
 * Date: 10/18/2016
 *
 * @author Yaron Yamin
 * @see JavaTestCreator
 */
public class TestMeCreator {
    private static final Logger LOG = Logger.getInstance(TestMeCreator.class.getName());
    private final IntentionAction action;

    public TestMeCreator() {
        this(new CreateTestMeAction());
    }

    TestMeCreator(IntentionAction action) {
        this.action = action;
    }

    public void createTest(Editor editor, PsiFile file) {
        try {
            PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
            if (CreateTestAction.isAvailableForElement(element)) {
                action.invoke(file.getProject(), editor, file.getContainingFile());
            }
        }
        catch (IncorrectOperationException e) {
            LOG.warn(e);
        }
    }
}
