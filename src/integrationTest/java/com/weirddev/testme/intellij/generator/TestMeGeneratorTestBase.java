package com.weirddev.testme.intellij.generator;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.psi.*;
import com.weirddev.testme.intellij.BaseIJIntegrationTest;
import com.weirddev.testme.intellij.template.FileTemplateContext;
import org.jetbrains.annotations.NotNull;

import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Properties;

/**
 * Date: 13/12/2016
 *
 * @author Yaron Yamin
 */
abstract public class TestMeGeneratorTestBase extends BaseIJIntegrationTest/*JavaCodeInsightFixtureTestCase */{
    protected final String templateFilename;
    protected final String testDirectory;
    private final TestTemplateContextBuilder testTemplateContextBuilder = mockTestTemplateContextBuilder();
    protected String expectedTestClassExtension = "java";
    protected boolean testEnabled = true;
    protected boolean ignoreTrailingWhitespaces;

    protected TestMeGeneratorTestBase(String templateFilename, String testDirectory) {
        super("testData/testMeGenerator/");
        this.templateFilename = templateFilename;
        this.testDirectory = testDirectory;
    }
    protected void skipTestIfGroovyPluginDisabled() {
        testEnabled = isGroovyPluginEnabled();
    }

    protected Boolean isGroovyPluginEnabled() {
        return Boolean.valueOf(System.getProperty("enableIdeaGroovyPlugin","true"));
    }

    protected void doTest() {
        doTest(true, false, false);
    }
    protected void doTest(final boolean ignoreUnusedProperties) {
        doTest("com.example.services.impl", "Foo", "FooTest", true, true, true, ignoreUnusedProperties);
    }

    protected void doTest(boolean reformatCode, boolean optimizeImports, boolean replaceFqn) {
        doTest("com.example.services.impl", "Foo", "FooTest", reformatCode, optimizeImports, replaceFqn, false);
    }

    protected void setTestModePropsForUI() {
        System.setProperty("testme.popoup.center", "true");//WA swing error when popup set relative to fake test editor
    }

    protected void doTest(final String packageName, String testSubjectClassName, final String expectedTestClassName, final boolean reformatCode, final boolean optimizeImports, final boolean replaceFqn, final boolean ignoreUnusedProperties) {
        if (!testEnabled) {
            System.out.println("Groovy idea plugin disabled. Skipping test");
            return;
        }
        final PsiClass fooClass = setupSourceFiles(packageName, testSubjectClassName);
        final PsiDirectory srcDir = fooClass.getContainingFile().getContainingDirectory();
        final PsiPackage targetPackage = JavaDirectoryService.getInstance().getPackage(srcDir);

        CommandProcessor.getInstance().executeCommand(getProject(), new Runnable() {
            @Override
            public void run() {
                myFixture.openFileInEditor(fooClass.getContainingFile().getVirtualFile());
                PsiElement result = new TestMeGenerator(new TestClassElementsLocator(), testTemplateContextBuilder,new CodeRefactorUtil()).generateTest(new FileTemplateContext(new FileTemplateDescriptor(templateFilename), getProject(),
                        expectedTestClassName,
                        targetPackage,
                        myModule,
                        srcDir,
                        fooClass,
                        reformatCode,
                        optimizeImports,
                        4, replaceFqn, ignoreUnusedProperties));
                System.out.println("result:"+result);
                verifyGeneratedTest(packageName, expectedTestClassName);
            }
        }, CodeInsightBundle.message("intention.create.test"), this);

    }
    protected void verifyGeneratedTest(String packageName, String expectedTestClassName) {
        String expectedTestClassFilePath = (packageName.length() > 0 ? (packageName.replace(".", "/") + "/") : "") + expectedTestClassName + "."+expectedTestClassExtension;
        myFixture.checkResultByFile(/*"src/"+*/expectedTestClassFilePath, testDirectory + "/" +expectedTestClassFilePath, ignoreTrailingWhitespaces);
    }
    @NotNull
    protected PsiClass setupSourceFiles(String packageName, String testSubjectClassName) {
        myFixture.copyDirectoryToProject("src", "");
        myFixture.copyDirectoryToProject("../../commonSrc", "");
        return myFixture.findClass(packageName+(packageName.length()>0?".":"") + testSubjectClassName);
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

    public void setUp() throws Exception {
        super.setUp();
        ignoreTrailingWhitespaces = false;
    }
    //    @Override //relevant when JavaCodeInsightFixtureTestCase is used
//    protected void tuneFixture(JavaModuleFixtureBuilder moduleBuilder) throws Exception {
//        moduleBuilder.addJdk(new File(System.getProperty("java.home")).getParentContainerClass());
//    }
}
