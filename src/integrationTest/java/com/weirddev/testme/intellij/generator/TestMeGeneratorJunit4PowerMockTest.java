package com.weirddev.testme.intellij.generator;

import com.weirddev.testme.intellij.configuration.TestMeConfig;
import com.weirddev.testme.intellij.configuration.TestMeConfigPersistent;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.template.context.Language;
import com.weirddev.testme.intellij.ui.customizedialog.FileTemplateCustomization;

import java.util.ArrayList;
import java.util.List;

public class TestMeGeneratorJunit4PowerMockTest extends TestMeGeneratorTestBase {

    public TestMeGeneratorJunit4PowerMockTest() {
        super(TemplateRegistry.JUNIT4_POWERMOCK_JAVA_TEMPLATE, "testPowerMock", Language.Java);
    }

    public void testSimpleClass() {
        doTest();
    }

    public void testVariousFieldTypes() {
        final TestMeConfig testMeConfig = new TestMeConfig();
        testMeConfig.setOptimizeImports(false);
        testMeConfig.setReplaceFullyQualifiedNames(false);
        final FileTemplateConfig fileTemplateConfig = new FileTemplateConfig(testMeConfig);
        doTest(fileTemplateConfig);
    }

    public void testMockFieldsInDependencyInjection() {
        doTest(new FileTemplateConfig(TestMeConfigPersistent.getInstance().getState()));
    }

    public void testRenderInternalMethodCallStubs() {
        final TestMeConfig testMeConfig = new TestMeConfig();
        testMeConfig.setRenderInternalMethodCallStubs(true);
        final FileTemplateConfig fileTemplateConfig = new FileTemplateConfig(testMeConfig);
        doTest(fileTemplateConfig);
    }

    public void testRenderInternalMethodCallStubsIgnored() {
        doTest(new FileTemplateConfig(TestMeConfigPersistent.getInstance().getState()));
    }

    public void testFileTemplateCustomization() {
        final TestMeConfig testMeConfig = new TestMeConfig();
        testMeConfig.setOpenCustomizeTestDialog(true);
        final FileTemplateConfig fileTemplateConfig = new FileTemplateConfig(testMeConfig);
        List<String> selectedFieldNameList = List.of("result", "techFighter");
        List<String> selectedMethodIdList = List.of("com.example.services.impl.Foo#fight(com.example.foes.Fire,java.lang.String)");
        final FileTemplateCustomization customization =
            new FileTemplateCustomization(selectedFieldNameList, selectedMethodIdList, true);
        doTest(fileTemplateConfig, customization);
    }

    public void testDeclareSpecificTestMethodThrownExceptionTypes() {
        final TestMeConfig testMeConfig = new TestMeConfig();
        testMeConfig.setThrowSpecificExceptionTypes(true);
        doTest(new FileTemplateConfig(testMeConfig));
    }
}
