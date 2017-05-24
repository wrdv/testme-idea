package com.weirddev.testme.intellij.generator;

import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.template.context.Language;

/**
 * Date: 23/05/2017
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorTestNgTest extends TestMeGeneratorTestBase {

    public TestMeGeneratorTestNgTest() {
        super(TemplateRegistry.TESTNG_MOCKITO_JAVA_TEMPLATE, "testTestNg", Language.Java);
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
}