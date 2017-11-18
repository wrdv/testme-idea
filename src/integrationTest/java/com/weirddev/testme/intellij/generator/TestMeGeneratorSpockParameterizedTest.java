package com.weirddev.testme.intellij.generator;

import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.template.context.Language;

/**
 * Date: 09/11/2017
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorSpockParameterizedTest extends TestMeGeneratorTestBase  {

    public TestMeGeneratorSpockParameterizedTest() {
        super(TemplateRegistry.SPOCK_PARAMETERIZED_MOCKITO_GROOVY_TEMPLATE, "testSpockParameterized", Language.Groovy);
        expectedTestClassExtension = "groovy";
        skipTestIfGroovyPluginDisabled();
    }
//    public void testBean() throws Exception{
//        doTest();
//    }
//    public void testCtorOverProps() throws Exception{
//        doTest(true,true,true);
//    }

    public void testGenerics() throws Exception{
        doTest(true,true,true);
    }
//    public void testMockReturned() throws Exception {
//        doTest(FileTemplateConfig.getInstance());
//    }
}
