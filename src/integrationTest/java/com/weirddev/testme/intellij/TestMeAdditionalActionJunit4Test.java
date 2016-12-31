package com.weirddev.testme.intellij;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.psi.PsiFile;
import org.junit.Assert;

/**
 * Date: 18/12/2016
 *
 * @author Yaron Yamin
 */
public class TestMeAdditionalActionJunit4Test extends BaseIJIntegrationTest {

    public TestMeAdditionalActionJunit4Test(){
        super(TemplateRegistry.JUNIT4_MOCKITO_JAVA_TEMPLATE, "test", "testData/testMeAdditionalAction/");
    }

    public void testInnerStaticClassWithMember() throws Exception {
        doTest("InnerStaticClassWithMemberTest", new VisualPosition(28, 28));
    }
    public void testInnerStaticClass() throws Exception {
        doTest("InnerStaticClassTest", new VisualPosition(13, 44));
    }
/*
    todo - implement feature - test subject is a non static nested class
    public void testInnerClass() throws Exception {
        doTest("PublicInnerClassTest", new VisualPosition(13, 44));
    }
*/

    private void doTest(String expectedTestClassName, VisualPosition expectedCaretPosition) {
        doTest("com.example.services.impl", "Foo", expectedTestClassName, expectedCaretPosition);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        PropertiesComponent.getInstance().setValue("create.test.in.the.same.root", String.valueOf(true));
    }

    protected void doTest(final String packageName, String testSubjectClassName, final String expectedTestClassName, VisualPosition expectedCaretPosition) {
        myFixture.copyDirectoryToProject("../../commonSrc", "");
        final PsiFile psiFile = myFixture.configureByFile(formatTestSourcePath(packageName, testSubjectClassName));
        new TestMeAdditionalAction(new TemplateDescriptor("", templateFilename), myFixture.getEditor(), psiFile).execute();
        String expectedTestClassFilePath = formatTestSourcePath(packageName, expectedTestClassName);
        myFixture.checkResultByFile(expectedTestClassFilePath, testDirectory + "/" +expectedTestClassFilePath, false);
        Editor selectedTextEditor = FileEditorManager.getInstance(getProject()).getSelectedTextEditor();
        Assert.assertEquals(expectedCaretPosition,selectedTextEditor.getCaretModel().getCurrentCaret().getVisualPosition());
    }

    private String formatTestSourcePath(String packageName, String className) {
        return (packageName.length() > 0 ? (packageName.replace(".", "/") + "/") : "") + className + ".java";
    }

}