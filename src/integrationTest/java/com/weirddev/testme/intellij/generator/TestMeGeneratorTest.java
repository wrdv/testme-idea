package com.weirddev.testme.intellij.generator;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.psi.*;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.weirddev.testme.intellij.CreateTestMeAction;
import com.weirddev.testme.intellij.FileTemplateContext;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Properties;

/**
 * Date: 10/20/2016
 * @author Yaron Yamin
 */
public class TestMeGeneratorTest extends LightCodeInsightFixtureTestCase /*JavaCodeInsightFixtureTestCase */{

    private static final String FILE_HEADER_TEMPLATE = "File Header.java";
    private static final String HEADER_TEMPLATE_REPLACEMENT_TEXT = "/** created by TestMe integration test on MMXVI */\n";
    private static boolean isHeaderTemplateReplaced=false;

    private final TestTemplateContextBuilder testTemplateContextBuilder = mockTestTemplateContextBuilder();

    public void testSimpleClass() throws Exception {
        doTest();
    }
    public void testDefaultPackage() throws Exception {
        doTest("", "Foo", "FooTest", true, false);
    }
    public void testVariousFieldTypes() throws Exception {
        doTest();
    }
    public void testWithSetters() throws Exception {
        doTest();
    }
    public void testConstructors() throws Exception {
        doTest(); //TODO template should initialize test subject directly with c'tor in this use case
    }
    public void testOverloading() throws Exception {
        doTest();
    }
    public void testNoFormatting() throws Exception {
        doTest(false, false);
    }
    public void testTypeNameCollision() throws Exception {
        doTest();
    }
    public void testTypeInDefaultPackageCollision() throws Exception {
        doTest("", "Foo", "FooTest", true, false);
    }
    public void testInheritance() throws Exception {
        doTest();
    }
    public void testGenerics() throws Exception {
        doTest(false, false);
    }
    public void testPrimitiveCallTypes() throws Exception {
        doTest(false, false);
    }
    public void testArrays() throws Exception {
        doTest(false, false);
    }
    public void testConstants() throws Exception {
        doTest();
    }
    public void testCollections() throws Exception {
        doTest(false, false);
    }
//    public void testGenericsTypeCollision() throws Exception {
//        doTest(false); //TODO implement scenario
//    }
    public void testEnum() throws Exception {
        doTest(false, false);
    }
    public void testStatic() throws Exception {
        doTest(false, false);
    }
    public void testDate() throws Exception {
        doTest(false, true);  //TODO - possible naming collision with GregorianCalendar not handled
    }
    public void testDateAssertion() throws Exception {
        doTest(false, true);
    }
    public void testParamsConstructors() throws Exception {
        doTest(true, true); //todo optimize type objects creation with reuse form shared map
        //todo refactor when done with intention of "Replace qualified name with 'import'" on all non static calls
    }
    //todo replace created macros with Velocity StringUtils

    // TODO assert caret position with <caret>

    // TODO TC different test target dir

    private void doTest() {
        doTest(true, false);
    }

    private void doTest(boolean reformatCode, boolean optimizeImports) {
        doTest("com.example.services.impl", "Foo", "FooTest", reformatCode, optimizeImports);
    }

    private void doTest(final String packageName, String testSubjectClassName, final String expectedTestClassName, final boolean reformatCode, final boolean optimizeImports) {
        myFixture.copyDirectoryToProject("src", "");
        myFixture.copyDirectoryToProject("../commonSrc", "");
        final PsiClass fooClass = myFixture.findClass(packageName+(packageName.length()>0?".":"") + testSubjectClassName);
        final PsiDirectory srcDir = fooClass.getContainingFile().getContainingDirectory();
        final PsiPackage targetPackage = JavaDirectoryService.getInstance().getPackage(srcDir);

        CommandProcessor.getInstance().executeCommand(getProject(), new Runnable() {
            @Override
            public void run() {
                myFixture.openFileInEditor(fooClass.getContainingFile().getVirtualFile());
                PsiElement result = new TestMeGenerator(new TestClassElementsLocator(), testTemplateContextBuilder).generateTest(new FileTemplateContext(new FileTemplateDescriptor(CreateTestMeAction.TESTME_WITH_JUNIT4_MOCKITO_JAVA), getProject(),
                        expectedTestClassName,
                        targetPackage,
                        myModule,
                        srcDir,
                        fooClass,
                        reformatCode,
                        optimizeImports,
                        5));
                System.out.println("result:"+result);
                String expectedTestClassFilePath = (packageName.length() > 0 ? (packageName.replace(".", "/") + "/") : "") + expectedTestClassName + ".java";
                myFixture.checkResultByFile(/*"src/"+*/expectedTestClassFilePath,"test/"+expectedTestClassFilePath, false);
            }
        }, CodeInsightBundle.message("intention.create.test"), this);

    }
    @Override
    public void setUp() throws Exception {
        super.setUp();
        System.out.println("TestDataPath:"+getTestDataPath());
        assertTrue(new File(getTestDataPath()).exists());
        System.out.println("temp dir path:"+myFixture.getTempDirPath());
        replacePatternTemplateText(FILE_HEADER_TEMPLATE, HEADER_TEMPLATE_REPLACEMENT_TEXT);
    }

    private void replacePatternTemplateText(String templateName, String templateText) {
        if(isHeaderTemplateReplaced){
            return;
        }
        FileTemplateManager fileTemplateManager = FileTemplateManager.getInstance(getProject());
        FileTemplate headerTemplate = fileTemplateManager.getPattern(templateName);
        System.out.println("headerTemplate:"+headerTemplate);
        System.out.println("Existing header Template text:\n"+headerTemplate.getText());
        System.out.println("Replacing header Template text with:\n"+ templateText);
        headerTemplate.setText(templateText);
        isHeaderTemplateReplaced = true;
    }

    @Override
    protected String getTestDataPath() {
        return "testData/testMeGenerator/"+getTestName(true).replace('$', '/');
    }

    @NotNull
    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return new DefaultLightProjectDescriptor() {
            @Override
            public Sdk getSdk() {
                return JavaSdk.getInstance().createJdk("java 1.7", new File(System.getProperty("java.home")).getParent(), false);
            }
        };
    }
    @NotNull
    private TestTemplateContextBuilder mockTestTemplateContextBuilder() {
        return new TestTemplateContextBuilder(){
            @Override
            public Map<String, Object> build(FileTemplateContext context, Properties defaultProperties) {
                Properties mockedDefaultProperties = new Properties();
                new GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime();
                mockedDefaultProperties.put("YEAR", 2016);
                mockedDefaultProperties.put("DAY", 11);
                mockedDefaultProperties.put("HOUR", 22);
                mockedDefaultProperties.put("MINUTE", 45);
                Map<String, Object> contextMap = super.build(context, mockedDefaultProperties);
                contextMap.put("MONTH_NAME_EN", "JANUARY");
                return contextMap;
            }
        };
    }

//    @Override //relevant when JavaCodeInsightFixtureTestCase is used
//    protected void tuneFixture(JavaModuleFixtureBuilder moduleBuilder) throws Exception {
//        moduleBuilder.addJdk(new File(System.getProperty("java.home")).getParent());
//    }
}