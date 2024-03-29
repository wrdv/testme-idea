package com.weirddev.testme.intellij.generator;

import com.weirddev.testme.intellij.configuration.TestMeConfig;
import com.weirddev.testme.intellij.configuration.TestMeConfigPersistent;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.template.context.Language;
import com.weirddev.testme.intellij.ui.customizedialog.FileTemplateCustomization;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 24/02/2017
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorSpockTest extends TestMeGeneratorTestBase {
    public TestMeGeneratorSpockTest() {
        super(TemplateRegistry.SPOCK_MOCKITO_GROOVY_TEMPLATE, "testSpock", Language.Groovy);
        expectedTestClassExtension = "groovy";
        skipTestIfGroovyPluginDisabled();
    }
    public void testBean() throws Exception{
        doTest();
    }
    public void testCtorOverProps() throws Exception{
        doTest(true,true,true);
    }

    public void testGenerics() throws Exception{
        doTest(true,true,true);
    }
    public void testMockReturned() throws Exception {
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
        List<String> selectedFieldNameList = new ArrayList<>();
        selectedFieldNameList.add("result");
        selectedFieldNameList.add("techFighter");
        List<String> selectedMethodIdList = new ArrayList<>();
        selectedMethodIdList.add("com.example.services.impl.Foo#fight(com.example.foes.Fire,java.lang.String)");
        final FileTemplateCustomization customization =
            new FileTemplateCustomization(selectedFieldNameList, selectedMethodIdList, true);
        doTest(fileTemplateConfig, customization);
    }
}
