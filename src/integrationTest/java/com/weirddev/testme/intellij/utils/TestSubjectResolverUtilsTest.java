package com.weirddev.testme.intellij.utils;

import com.intellij.openapi.editor.VisualPosition;
import com.intellij.psi.PsiFile;
import com.weirddev.testme.intellij.BaseIJIntegrationTest;
import org.junit.Assert;

/**
 * Date: 01/01/2017
 *
 * @author Yaron Yamin
 */
public class TestSubjectResolverUtilsTest extends BaseIJIntegrationTest {

    public TestSubjectResolverUtilsTest() {
        super("testData/testSubjectResolverUtils/");
    }

    public void testIsValidForTesting() throws Exception {
        myFixture.copyDirectoryToProject("../../commonSrc", "");
        final PsiFile psiFile = myFixture.configureByFile(formatTestSourcePath("com.example.services.impl", "Foo"));
//        final PsiClass element = myFixture.findElementByText("", PsiClass.class);
        doTest(true, new VisualPosition(1,1), psiFile);
        doTest(true,new VisualPosition(9,35), psiFile);//PublicInnerClass
        doTest(true,new VisualPosition(11,46), psiFile);//methodOfInnerClass
        doTest(true,new VisualPosition(17,22), psiFile);//InnerClass
        doTest(true,new VisualPosition(19,1), psiFile);//InnerOfInnerClass
        doTest(false,new VisualPosition(27,1), psiFile);//anonymousPublicInnerClass
        doTest(true,new VisualPosition(30,42), psiFile);//InnerStaticClass
        doTest(false,new VisualPosition(36,1), psiFile);//PrivateInnerStaticClass
    }

    private void doTest(boolean expectedResult, VisualPosition visualPosition, PsiFile psiFile) {
        getEditor().getCaretModel().moveToVisualPosition(visualPosition);
        final boolean result = TestSubjectResolverUtils.isValidForTesting(getEditor(), psiFile);
        Assert.assertEquals(expectedResult, result);
    }

    private String formatTestSourcePath(String packageName, String className) {
        return (packageName.length() > 0 ? (packageName.replace(".", "/") + "/") : "") + className + ".java";
    }

}