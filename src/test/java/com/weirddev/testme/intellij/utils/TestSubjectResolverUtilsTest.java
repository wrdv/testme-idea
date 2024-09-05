package com.weirddev.testme.intellij.utils;

import com.intellij.openapi.editor.VisualPosition;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.weirddev.testme.intellij.BaseIJIntegrationTest;
import com.weirddev.testme.intellij.action.CreateTestMeAction;
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
        doTest("com.example.services.impl.Foo", new VisualPosition(1,1), psiFile);
        doTest("com.example.services.impl.Foo.PublicInnerClass.InnerOfPublicInnerClass",new VisualPosition(9,35), psiFile);//PublicInnerClass
        doTest("com.example.services.impl.Foo.PublicInnerClass.InnerOfPublicInnerClass",new VisualPosition(11,46), psiFile);//methodOfInnerClass
        doTest("com.example.services.impl.Foo.InnerClass.InnerOfInnerClass",new VisualPosition(19,1), psiFile);//InnerOfInnerClass
        doTest("com.example.services.impl.Foo.InnerClass",new VisualPosition(22,42), psiFile);//InnerClass
        doTest("com.example.services.impl.Foo",new VisualPosition(27,1), psiFile);//anonymousPublicInnerClass - parent containing class should be selected
        doTest("com.example.services.impl.Foo.InnerStaticClass",new VisualPosition(30,42), psiFile);//InnerStaticClass
        doTest("com.example.services.impl.Foo",new VisualPosition(36,1), psiFile);//PrivateInnerStaticClass
    }

    private void doTest(String expectedResult, VisualPosition visualPosition, PsiFile psiFile) {
        getEditor().getCaretModel().moveToVisualPosition(visualPosition);
        final PsiElement testableElement = TestSubjectResolverUtils.getTestableElement(getEditor(), psiFile);
        Assert.assertNotNull(testableElement);
        final PsiClass containingClass = CreateTestMeAction.getContainingClass(testableElement);
        Assert.assertNotNull(containingClass);
        Assert.assertEquals(expectedResult, containingClass.getQualifiedName());
    }

    private String formatTestSourcePath(String packageName, String className) {
        return (packageName.length() > 0 ? (packageName.replace(".", "/") + "/") : "") + className + ".java";
    }

}