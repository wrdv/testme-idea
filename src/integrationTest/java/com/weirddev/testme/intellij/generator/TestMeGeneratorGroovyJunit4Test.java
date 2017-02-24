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
    }
}
