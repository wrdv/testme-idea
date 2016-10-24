package com.weirddev.testme.intellij;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.mock.MockPsiDirectory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.psi.impl.file.PsiDirectoryImpl;
import com.intellij.psi.impl.file.PsiJavaDirectoryFactory;
import com.intellij.psi.impl.file.PsiPackageImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.*;
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl;
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl;

import java.io.File;

/**
 * Date: 10/20/2016
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorTest extends LightCodeInsightFixtureTestCase {

//    private static final String TEST_TEMPLATE = "TestMe with JUnit4 & Mockito.java";
    private static final String TEST_TEMPLATE = "Test Template - TestMe JUnit4 & Mockito.java";

    public void testGenerateTest() throws Exception {
        myFixture.copyDirectoryToProject("src","");
        final VirtualFile testDir = myFixture.getTempDirFixture().findOrCreateDir("test");
        System.out.println("project test dir:"+testDir);
        assertNotNull("ref file not found", testDir);
        String packageName = "com.example.services.impl";
        String dirPath = packageName.replace(".", "/") + "/";
//        myFixture.configureByFile(dirPath + "Fire.java");
        PsiClass fooClass = myFixture.findClass("com.example.services.impl.Foo");
        myFixture.openFileInEditor(fooClass.getContainingFile().getVirtualFile());

        PsiPackageImpl targetPackage = new PsiPackageImpl(getPsiManager(), packageName);
        PsiElement result = new TestMeGenerator().generateTest(new FileTemplateContext(new FileTemplateDescriptor(TEST_TEMPLATE), getProject(),
                "FooTest",
//                new PsiPackageImpl(getPsiManager(), "im.the.generator"),
                targetPackage,
                myModule,
//                getProject().getProjectFile().createChildDirectory(this,"test"),
//                targetPackage.getDirectories()[0],
//                PsiJavaDirectoryFactory.getInstance(getProject()).createDirectory(myFixture.getTempDirFixture().getFile(".")),
                fooClass.getContainingFile().getContainingDirectory(),
//                PsiJavaDirectoryFactory.getInstance(getProject()).createDirectory(myFixture.getTempDirFixture().getFile(".").createChildDirectory(this,"com/example/services/impl")),
//
//               PsiJavaDirectoryFactory.getInstance(getProject()).createDirectory(testDir),
//                new MockPsiDirectory(getProject(),myTestRootDisposable),
//                PsiJavaDirectoryFactory.getInstance(getProject()).createDirectory(,
//                new PsiDirectoryImpl((PsiManagerImpl) getPsiManager(),myFixture.getEditor().
//                myFixture.getElementAtCaret().getContainingFile().getVirtualFile() /*getFile().getVirtualFile()*/),
                fooClass
        ));
//        myFixture.findFileInTempDir("test/FooTest.java");

        System.out.println("result:"+result);
        myFixture.checkResultByFile(dirPath+"FooTest.java","test/"+dirPath+"FooTest.java",false);

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

    @Override
    public void setUp() throws Exception {
        super.setUp();

        System.out.println("TestDataPath:"+getTestDataPath());
        assertTrue(new File(getTestDataPath()).exists());

//        final TestFixtureBuilder<IdeaProjectTestFixture> projectBuilder = IdeaTestFixtureFactory.getFixtureFactory().createFixtureBuilder(getName());
//        // Repeat the following line for each module
//        final JavaModuleFixtureBuilder moduleFixtureBuilder = projectBuilder.addModule(JavaModuleFixtureBuilder.class);
//        moduleFixtureBuilder.addSourceContentRoot("src");
//        moduleFixtureBuilder.addSourceContentRoot("test");
//        myModule = moduleFixtureBuilder.getFixture().getModule();
//        myFixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(projectBuilder.getFixture());



    }

    @Override
    protected String getTestDataPath() {
//        return "src/integrationTest/resources/"+getTestName(true).replace('$', '/');
        return "testData/"+getTestName(true).replace('$', '/');
    }
}