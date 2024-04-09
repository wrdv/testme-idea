package com.weirddev.testme.intellij.generator;


import com.weirddev.testme.intellij.configuration.TestMeConfig;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.template.context.Language;

/**
 * Date: 24/02/2017
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorGroovyJunit4Test extends TestMeGeneratorJunit4Test {
    public TestMeGeneratorGroovyJunit4Test() {
        super(TemplateRegistry.JUNIT4_MOCKITO_GROOVY_TEMPLATE, "testGroovy", Language.Groovy);
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
        doTest(true); //todo nested class getter call not identified - property someLongerNum
    }
    public void testIgnoreUnusedPropertiesWhenInherited() throws Exception{
        doTest(true);
    }
    public void testIgnoreUnusedPropertiesInGroovy() throws Exception {
        doTest(true);
    }
    public void testDirectlyReferencedPropertiesNotIgnored() throws Exception{
        doTest("com.example.beans", "Foo", "FooTest", true, true, true, true, 50, false);
    }
    public void testSettersOverCtor() throws Exception{
        doTest(true,true,true);
    }
    public void testCtorOverSetters() throws Exception{
        doTest(true,true,true,67, false, false);
    }
    public void testDeclareSpecificTestMethodThrownExceptionTypes() {
        final TestMeConfig testMeConfig = new TestMeConfig();
        testMeConfig.setThrowSpecificExceptionTypes(false);
        doTest(new FileTemplateConfig(testMeConfig));
    }
}
