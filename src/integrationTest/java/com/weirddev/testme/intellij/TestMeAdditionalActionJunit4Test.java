package com.weirddev.testme.intellij;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.psi.PsiFile;
import com.weirddev.testme.intellij.action.TestMeAdditionalAction;
import com.weirddev.testme.intellij.template.TemplateDescriptor;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import org.junit.Assert;

/**
 * Date: 18/12/2016
 *
 * @author Yaron Yamin
 */
public class TestMeAdditionalActionJunit4Test extends BaseIJIntegrationTest {

    final String templateFilename = TemplateRegistry.JUNIT4_MOCKITO_JAVA_TEMPLATE;
    final String testDirectory = "test";

    public TestMeAdditionalActionJunit4Test(){
        super("testData/testMeAdditionalAction/");
    }

    public void testInnerStaticClassWithMember() throws Exception {
        doTest("InnerStaticClassWithMemberTest", new VisualPosition(30, 28));
    }
    public void testInnerStaticClass() throws Exception {
        doTest("InnerStaticClassTest", new VisualPosition(12, 44));
    }
    public void testInnerClass() throws Exception {
        doTest("PublicInnerClassTest", new VisualPosition(12, 44));
    }
    public void testInnerOfInnerClass() throws Exception {
        doTest("InnerOfInnerClassTest", new VisualPosition(12, 45));
    }
    public void testInnerStaticOfInnerStaticClass() throws Exception {
        doTest("InnerStaticOfInnerStaticClassTest", new VisualPosition(12, 64));
    }
    public void testInnerOfInnerStaticClass() throws Exception {
        doTest("InnerOfInnerStaticClassTest", new VisualPosition(12, 58));
    }

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
        new TestMeAdditionalAction(new TemplateDescriptor("", templateFilename, null), myFixture.getEditor(), psiFile).execute();
        String expectedTestClassFilePath = formatTestSourcePath(packageName, expectedTestClassName);
        myFixture.checkResultByFile(expectedTestClassFilePath, testDirectory + "/" +expectedTestClassFilePath, false);
        Editor selectedTextEditor = FileEditorManager.getInstance(getProject()).getSelectedTextEditor();
        Assert.assertEquals(expectedCaretPosition,selectedTextEditor.getCaretModel().getCurrentCaret().getVisualPosition());
    }

    private String formatTestSourcePath(String packageName, String className) {
        return (packageName.length() > 0 ? (packageName.replace(".", "/") + "/") : "") + className + ".java";
    }

}