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
import com.weirddev.testme.intellij.FileTemplateContext;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Properties;

/**
 * Date: 13/12/2016
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorTestBase extends LightCodeInsightFixtureTestCase /*JavaCodeInsightFixtureTestCase */{
    private static final String FILE_HEADER_TEMPLATE = "File Header.java";
    private static final String HEADER_TEMPLATE_REPLACEMENT_TEXT = "/** created by TestMe integration test on MMXVI */\n";
    private static boolean isHeaderTemplateReplaced=false;
    private final String templateFilename;
    private final String testDirectory;
    private final TestTemplateContextBuilder testTemplateContextBuilder = mockTestTemplateContextBuilder();

    TestMeGeneratorTestBase(String templateFilename, String testDirectory) {
        this.templateFilename = templateFilename;
        this.testDirectory = testDirectory;
    }


    protected void doTest() {
        doTest(true, false, false);
    }

    protected void doTest(boolean reformatCode, boolean optimizeImports, boolean replaceFqn) {
        doTest("com.example.services.impl", "Foo", "FooTest", reformatCode, optimizeImports, replaceFqn);
    }

    protected void doTest(final String packageName, String testSubjectClassName, final String expectedTestClassName, final boolean reformatCode, final boolean optimizeImports, final boolean replaceFqn) {
        myFixture.copyDirectoryToProject("src", "");
        myFixture.copyDirectoryToProject("../commonSrc", "");
        final PsiClass fooClass = myFixture.findClass(packageName+(packageName.length()>0?".":"") + testSubjectClassName);
        final PsiDirectory srcDir = fooClass.getContainingFile().getContainingDirectory();
        final PsiPackage targetPackage = JavaDirectoryService.getInstance().getPackage(srcDir);

        CommandProcessor.getInstance().executeCommand(getProject(), new Runnable() {
            @Override
            public void run() {
                myFixture.openFileInEditor(fooClass.getContainingFile().getVirtualFile());
                PsiElement result = new TestMeGenerator(new TestClassElementsLocator(), testTemplateContextBuilder).generateTest(new FileTemplateContext(new FileTemplateDescriptor(templateFilename), getProject(),
                        expectedTestClassName,
                        targetPackage,
                        myModule,
                        srcDir,
                        fooClass,
                        reformatCode,
                        optimizeImports,
                        5, replaceFqn));
                System.out.println("result:"+result);
                String expectedTestClassFilePath = (packageName.length() > 0 ? (packageName.replace(".", "/") + "/") : "") + expectedTestClassName + ".java";
                myFixture.checkResultByFile(/*"src/"+*/expectedTestClassFilePath, testDirectory + "/" +expectedTestClassFilePath, false);
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
                Map<String, Object> contextMap = super.build(context, mockedDefaultProperties);
                contextMap.put("MONTH_NAME_EN", "JANUARY");
                contextMap.put("DAY_NUMERIC", 11);
                contextMap.put("HOUR_NUMERIC", 22);
                contextMap.put("MINUTE_NUMERIC", 45);
                contextMap.put("SECOND_NUMERIC", 55);
                return contextMap;
            }
        };
    }
    //    @Override //relevant when JavaCodeInsightFixtureTestCase is used
//    protected void tuneFixture(JavaModuleFixtureBuilder moduleBuilder) throws Exception {
//        moduleBuilder.addJdk(new File(System.getProperty("java.home")).getParent());
//    }
}
