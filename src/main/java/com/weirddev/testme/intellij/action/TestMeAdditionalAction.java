package com.weirddev.testme.intellij.action;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.weirddev.testme.intellij.template.TemplateDescriptor;
import com.weirddev.testme.intellij.ui.popup.TestMePopUpHandler;
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
    private final PsiMethod selectMethod;
    private final String text;
    private final TestMeCreator testMeCreator;
    private final String tokenizedtext;

    public TestMeAdditionalAction(TemplateDescriptor templateDescriptor, Editor editor, PsiFile file,
        PsiMethod selectMethod) {
        this.templateDescriptor = templateDescriptor;
        this.editor = editor;
        this.file = file;
        this.text = templateDescriptor.getHtmlDisplayName();
        this.tokenizedtext = templateDescriptor.getTokenizedName();
        this.selectMethod = selectMethod;
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
    public void execute(Project project) {
        testMeCreator.createTest(editor, file, templateDescriptor, selectMethod);
    }

    public String getTokenizedtext() {
        return tokenizedtext;
    }
}
