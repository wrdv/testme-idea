package com.weirddev.testme.intellij;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Date: 10/15/2016
 *
 * @author Yaron Yamin
 */
public class TestMeAdditionalAction implements TestMePopUpHandler.AdditionalAction {

    private final TemplateDescriptor templateDescriptor;
    private final Editor editor;
    private final PsiFile file;
    private final String text;
    private final TestMeCreator testMeCreator;

    public TestMeAdditionalAction(TemplateDescriptor templateDescriptor, Editor editor, PsiFile file) {
        this.templateDescriptor = templateDescriptor;
        this.editor = editor;
        this.file = file;
        this.text = templateDescriptor.getTokenizedDisplayName();
        testMeCreator = new TestMeCreator();
    }

    @NotNull
    @Override
    public String getText() {
        return text;
    }

    @Override
    public Icon getIcon() {
        return null;//Icons.TEST_ME;
    }

    @Override
    public void execute() {
        testMeCreator.createTest(editor, file, templateDescriptor.getFilename());
    }
}
