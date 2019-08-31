package com.weirddev.testme.intellij.generator;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.psi.*;
import com.weirddev.testme.intellij.BaseIJIntegrationTest;
import com.weirddev.testme.intellij.configuration.TestMeConfig;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.FileTemplateContext;
import com.weirddev.testme.intellij.template.context.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

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
    private final Language language;
    protected String expectedTestClassExtension = "java";
    protected boolean testEnabled = true;
    protected boolean ignoreTrailingWhitespaces;

    protected TestMeGeneratorTestBase(String templateFilename, String testDirectory, Language language) {
        super("testData/testMeGenerator/");
        this.templateFilename = templateFilename;
        this.testDirectory = testDirectory;
        this.language = language;
    }
    protected void skipTestIfGroovyPluginDisabled() {
        skipTestIfPluginDisabled(OptionalPluginTestDependency.Groovy);
    }
    protected void skipTestIfScalaPluginDisabled() {
        skipTestIfPluginDisabled(OptionalPluginTestDependency.Scala);
    }
    private void skipTestIfPluginDisabled(OptionalPluginTestDependency optionalPluginTestDependency) {
        testEnabled = pluginShouldBeEnabled(optionalPluginTestDependency);
        Assert.assertEquals(testEnabled, isPluginEnabled(optionalPluginTestDependency));
    }

    private boolean isPluginEnabled(OptionalPluginTestDependency optionalPluginTestDependency) {
        boolean pluginEnabled = false;
        try {
            Class.forName(optionalPluginTestDependency.getClassId());
            pluginEnabled = true;
        } catch (ClassNotFoundException e) { }
        return pluginEnabled;
    }

    protected Boolean pluginShouldBeEnabled(OptionalPluginTestDependency optionalPluginTestDependency) {
        return Boolean.valueOf(System.getProperty(optionalPluginTestDependency.getBuildProperty(),"true"));
    }

    protected void doTest() {
        doTest(true, false, false);
    }
    protected void doTest(final boolean ignoreUnusedProperties) {
        doTest(true, true, true, 50, ignoreUnusedProperties, false);
    }

    protected void doTest(boolean reformatCode, boolean optimizeImports, boolean replaceFqn) {
        doTest(reformatCode, optimizeImports, replaceFqn, 50, false, false);
    }
    protected void doTest(boolean reformatCode, boolean optimizeImports, boolean replaceFqn, int minPercentOfExcessiveSettersToPreferDefaultCtor, boolean ignoreUnusedProperties, boolean stubMockMethodCallsReturnValues) {
        doTest("com.example.services.impl", "Foo", "FooTest", reformatCode, optimizeImports, replaceFqn, ignoreUnusedProperties, minPercentOfExcessiveSettersToPreferDefaultCtor, stubMockMethodCallsReturnValues);
    }
    protected void doTest(FileTemplateConfig fileTemplateConfig) {
        doTest("com.example.services.impl", "Foo", "FooTest", fileTemplateConfig);
    }

    protected void doTest(final String packageName, String testSubjectClassName, final String expectedTestClassName, final boolean reformatCode, final boolean optimizeImports, final boolean replaceFqn, final boolean ignoreUnusedProperties, final int minPercentOfExcessiveSettersToPreferDefaultCtor, boolean stubMockMethodCallsReturnValues) {
        final TestMeConfig testMeConfig = new TestMeConfig();
        testMeConfig.setGenerateTestsForInheritedMethods(true);
        testMeConfig.setOptimizeImports(optimizeImports);
        testMeConfig.setReformatCode(reformatCode);
        testMeConfig.setReplaceFullyQualifiedNames(replaceFqn);

        final FileTemplateConfig fileTemplateConfig = new FileTemplateConfig(testMeConfig);
        fileTemplateConfig.setMaxRecursionDepth(4);
        fileTemplateConfig.setIgnoreUnusedProperties(ignoreUnusedProperties);
        fileTemplateConfig.setStubMockMethodCallsReturnValues(stubMockMethodCallsReturnValues);
        fileTemplateConfig.setMaxNumOfConcreteCandidatesToReplaceInterfaceParam(2);
        fileTemplateConfig.setMinPercentOfExcessiveSettersToPreferMapCtor(minPercentOfExcessiveSettersToPreferDefaultCtor);
        fileTemplateConfig.setMinPercentOfInteractionWithPropertiesToTriggerConstructorOptimization(66);
        fileTemplateConfig.setIgnoreUnusedProperties(ignoreUnusedProperties);

        doTest(packageName, testSubjectClassName, expectedTestClassName, fileTemplateConfig);
    }

    protected void doTest(final String packageName, String testSubjectClassName, final String expectedTestClassName, final FileTemplateConfig fileTemplateConfig) {
        if (!testEnabled) {
            System.out.println(expectedTestClassExtension+ " idea plugin disabled. Skipping test");
            return;
        }
        final PsiClass fooClass = setupSourceFiles(packageName, testSubjectClassName);
        final PsiDirectory srcDir = fooClass.getContainingFile().getContainingDirectory();
        final PsiPackage targetPackage = JavaDirectoryService.getInstance().getPackage(srcDir);

        CommandProcessor.getInstance().executeCommand(getProject(), () -> {
            myFixture.openFileInEditor(fooClass.getContainingFile().getVirtualFile());

            PsiElement result = new TestMeGenerator(new TestClassElementsLocator(), testTemplateContextBuilder,new CodeRefactorUtil()).generateTest(new FileTemplateContext(new FileTemplateDescriptor(templateFilename), language, getProject(),
                    expectedTestClassName,
                    targetPackage,
                    getModule(),
                    getModule(),
                    srcDir,
                    fooClass,
                    fileTemplateConfig));
            System.out.println("result:"+result);
            verifyGeneratedTest(packageName, expectedTestClassName);
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
