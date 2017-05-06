package com.weirddev.testme.intellij.generator;

import com.weirddev.testme.intellij.template.TemplateRegistry;

/**
 * Date: 24/02/2017
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorGroovyJunit4Test extends TestMeGeneratorJunit4Test {
    public TestMeGeneratorGroovyJunit4Test() {
        super(TemplateRegistry.JUNIT4_GROOVY_MOCKITO_JAVA_TEMPLATE, "testGroovy");
        expectedTestClassExtension = "groovy";
        skipTestIfGroovyPluginDisabled();
    }

    public void testBean() throws Exception{
        doTest();
    }
    public void testJavaCallsGroovy() throws Exception{
        doTest(true,true,true);
    }
    public void testCtorOverProps() throws Exception{
        doTest(true,true,true);
    }
    public void testIgnoreUnusedProperties() throws Exception{
        doTest(true); //todo fix identifying nested class getter call (or amend test if should not be supported)
    }
}
