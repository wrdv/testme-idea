package com.weirddev.testme.intellij.generator;

import com.weirddev.testme.intellij.configuration.TestMeConfig;
import com.weirddev.testme.intellij.configuration.TestMeConfigPersistent;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.template.context.Language;
import com.weirddev.testme.intellij.ui.customizedialog.FileTemplateCustomization;

import java.util.List;

/**
 * Date: 13/12/2016
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorJunit5Test extends TestMeGeneratorTestBase {

    public TestMeGeneratorJunit5Test() {
        super(TemplateRegistry.JUNIT5_MOCKITO_JAVA_TEMPLATE, "testJunit5", Language.Java);
    }

    public void testSimpleClass() throws Exception {
        doTest();
    }
    public void testVariousFieldTypes() throws Exception {
        doTest();
    }
    public void testArrays() throws Exception {
        doTest(false, false, true);
    }
    public void testMockReturned() throws Exception {
        doTest(new FileTemplateConfig(TestMeConfigPersistent.getInstance().getState()));
    }

    public void testUtilWithoutAccessableCtor() {
        doTest(true, true, true);
    }

    public void testVerifyMethodCall() {
        doTest(new FileTemplateConfig(TestMeConfigPersistent.getInstance().getState()));
    }

    public void testMockFieldsInDependencyInjection() {
        doTest(new FileTemplateConfig(TestMeConfigPersistent.getInstance().getState()));
    }

    public void testMockFieldsInDiWithSetter() {
        doTest(new FileTemplateConfig(TestMeConfigPersistent.getInstance().getState()));
    }

    public void testMockFieldsInDiWithCtor() {
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
}
