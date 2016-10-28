package com.weirddev.testme.intellij;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.file.PsiPackageImpl;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.JavaCodeInsightFixtureTestCase;

import java.io.File;

/**
 * Date: 10/20/2016
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorTest extends JavaCodeInsightFixtureTestCase {

    private static final String FILE_HEADER_TEMPLATE = "File Header.java";
    public static final String HEADER_TEMPLATE_TEXT = "/** created by TestMe integration test on MMXVI */";

    public void testGenerateTest() throws Exception {
        myFixture.copyDirectoryToProject("src", "");
        //PsiTestUtil.addSourceRoot(myFixture.getModule(), myFixture.copyDirectoryToProject("src", "src"));

//        final VirtualFile testDir = myFixture.getTempDirFixture().findOrCreateDir("test");
//        assertNotNull("ref file not found", testDir);
//        System.out.println("project test dir:"+testDir);
//        PsiDirectory testDirectory = PsiJavaDirectoryFactory.getInstance(getProject()).createDirectory(testDir);
        String packageName = "com.example.services.impl";
        String dirPath = packageName.replace(".", "/") + "/";
        PsiClass fooClass = myFixture.findClass("com.example.services.impl.Foo");
//        LightCodeInsightTestCase.getJavaFacade().findClass("src.com.example.services.impl.Foo", myFixture.getModule().getModuleScope());
        myFixture.openFileInEditor(fooClass.getContainingFile().getVirtualFile());
        PsiPackageImpl targetPackage = new PsiPackageImpl(getPsiManager(), packageName);
        PsiElement result = new TestMeGenerator().generateTest(new FileTemplateContext(new FileTemplateDescriptor(CreateTestMeAction.TESTME_WITH_JUNIT4_MOCKITO_JAVA), getProject(),
                "FooTest",
//                new PsiPackageImpl(getPsiManager(), "im.the.generator"),
                targetPackage,
                myModule,
//                PsiJavaDirectoryFactory.getInstance(getProject()).createDirectory(myFixture.getTempDirFixture().getFile(".")),
                fooClass.getContainingFile().getContainingDirectory(),
//                testDirectory,
//                PsiJavaDirectoryFactory.getInstance(getProject()).createDirectory(myFixture.getTempDirFixture().getFile(".").createChildDirectory(this,"com/example/services/impl")),
//               PsiJavaDirectoryFactory.getInstance(getProject()).createDirectory(testDir),
                fooClass
        ));
        System.out.println("result:"+result);
        myFixture.checkResultByFile(/*"src/"+*/dirPath+"FooTest.java","test/"+dirPath+"FooTest.java",false);
    }
  //TODO test with default package
  //TODO test w/out formatting
  //TODO test with static, final, primitive, primitive wrapper objects fields.
    // TODO fields,params and return types that have generics. lambda params?
  // TODO test no default c'tor
    // TODO test overloaded methods
    // TODO test class inheritance
    //TODO test field/param types with same short names from different packages
    //TODO test multiple field/param types from the same package
    //TODO test caret position with <caret>
    @Override
    public void setUp() throws Exception {
        super.setUp();
        System.out.println("TestDataPath:"+getTestDataPath());
        assertTrue(new File(getTestDataPath()).exists());
        System.out.println("temp dir path:"+myFixture.getTempDirPath());
        replacePatternTemplateText(FILE_HEADER_TEMPLATE, HEADER_TEMPLATE_TEXT);
    }

    private void replacePatternTemplateText(String templateName, String templateText) {
        FileTemplateManager fileTemplateManager = FileTemplateManager.getInstance(getProject());
        FileTemplate headerTemplate = fileTemplateManager.getPattern(templateName);
        System.out.println("headerTemplate:"+headerTemplate);
        System.out.println("Existing header Template text:\n"+headerTemplate.getText());
        System.out.println("Replacing header Template text with:\n"+ templateText);
        headerTemplate.setText(templateText);
    }

    @Override
    protected String getTestDataPath() {
//        return "src/integrationTest/resources/"+getTestName(true).replace('$', '/');
        return "testData/"+getTestName(true).replace('$', '/');
    }

    @Override
    protected void tuneFixture(JavaModuleFixtureBuilder moduleBuilder) throws Exception {
        moduleBuilder.addJdk(new File(System.getProperty("java.home")).getParent());
    }
}