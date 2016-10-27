package com.weirddev.testme.intellij;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
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

//    private static final String TEST_TEMPLATE = "TestMe with JUnit4 & Mockito.java";
    //TODO use actual template and replace Header template dynamically
    private static final String TEST_TEMPLATE = "Test Template - TestMe JUnit4 & Mockito.java";

    public void testGenerateTest() throws Exception {
        myFixture.copyDirectoryToProject("src", "");
        //PsiTestUtil.addSourceRoot(myFixture.getModule(), myFixture.copyDirectoryToProject("src", "src"));
        System.out.println("temp dir path:"+myFixture.getTempDirPath());
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
        PsiElement result = new TestMeGenerator().generateTest(new FileTemplateContext(new FileTemplateDescriptor(TEST_TEMPLATE), getProject(),
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
    //TODO test carent position with <caret>
    @Override
    public void setUp() throws Exception {
        super.setUp();
        System.out.println("TestDataPath:"+getTestDataPath());
        assertTrue(new File(getTestDataPath()).exists());
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