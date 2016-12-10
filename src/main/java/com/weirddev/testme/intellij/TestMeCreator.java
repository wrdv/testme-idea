package com.weirddev.testme.intellij;

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

    public void createTest(Editor editor, PsiFile file, String templateFilename) {
        try {
            PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
            if (CreateTestAction.isAvailableForElement(element)) {
                new CreateTestMeAction(templateFilename).invoke(file.getProject(), editor, file.getContainingFile());
            }
        }
        catch (IncorrectOperationException e) {
            LOG.warn(e);
        }
    }
}
