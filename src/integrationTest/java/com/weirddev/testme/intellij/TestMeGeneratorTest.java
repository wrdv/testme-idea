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
        final VirtualFile testDir = myFixture.getTempDirFixture().findOrCreateDir("test");
//        myFixture.getFile()
        System.out.println("project test dir:"+testDir);
        assertNotNull("ref file not found", testDir);
        PsiFile psiFile = myFixture.configureByFile("Foo.java");
        PsiClass psiClass = myFixture.findClass("Foo");

        PsiElement result = new TestMeGenerator().generateTest(new FileTemplateContext(new FileTemplateDescriptor(TEST_TEMPLATE), getProject(),
                "FooTest",
//                new PsiPackageImpl(getPsiManager(), "im.the.generator"),
                new PsiPackageImpl(getPsiManager(), ""),
                myModule,
//                getProject().getProjectFile().createChildDirectory(this,"tset"),
                PsiJavaDirectoryFactory.getInstance(getProject()).createDirectory(myFixture.getTempDirFixture().getFile(".")),
//                PsiJavaDirectoryFactory.getInstance(getProject()).createDirectory(testDir),
//                new MockPsiDirectory(getProject(),myTestRootDisposable),
//                PsiJavaDirectoryFactory.getInstance(getProject()).createDirectory(,
//                new PsiDirectoryImpl((PsiManagerImpl) getPsiManager(),myFixture.getEditor().
//                myFixture.getElementAtCaret().getContainingFile().getVirtualFile() /*getFile().getVirtualFile()*/),
                psiClass
//                PsiTreeUtil.getParentOfType(myFixture.getElementAtCaret(), PsiClass.class, false) /*getFile().getOriginalElement()*/)
        ));
//        myFixture.findFileInTempDir("test/FooTest.java");

        myFixture.checkResultByFile("FooTest.java","FooTest.java",false);


        System.out.println("result:"+result);
    }

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
//        VirtualFile virtualFile = myFixture.copyFileToProject(getTestDataPath()+"/Foo.java","src");


    }

    @Override
    protected String getTestDataPath() {
//        return "src/integrationTest/resources/"+getTestName(true).replace('$', '/');
        return "testData/"+getTestName(true).replace('$', '/');
    }
}