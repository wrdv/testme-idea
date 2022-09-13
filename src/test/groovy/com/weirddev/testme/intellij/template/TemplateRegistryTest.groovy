package com.weirddev.testme.intellij.template

import org.junit.Test

/**
 * Date: 01/05/2017
 * @author Yaron Yamin
 */
class TemplateRegistryTest {
    @Test
    void testTemplateRegistry() throws Exception {
        def descriptors = new TemplateRegistry().getTemplateDescriptors()
        assert descriptors.size() ==7
        assert descriptors.find({it.htmlDisplayName.contains('JUnit4')&& it.htmlDisplayName.contains('Groovy')}).filename == TemplateRegistry.JUNIT4_MOCKITO_GROOVY_TEMPLATE
        assert descriptors.find({it.htmlDisplayName.contains('<i>Spock</i>')}).filename == TemplateRegistry.SPOCK_MOCKITO_GROOVY_TEMPLATE
        assert descriptors.find({it.htmlDisplayName.contains('JUnit5')&& it.htmlDisplayName.contains('Mockito')}).filename == TemplateRegistry.JUNIT5_MOCKITO_JAVA_TEMPLATE
        assert descriptors.find({it.htmlDisplayName.contains('TestNG')&& it.htmlDisplayName.contains('Mockito')}).filename == TemplateRegistry.TESTNG_MOCKITO_JAVA_TEMPLATE
        assert descriptors.find({it.htmlDisplayName.contains('Spock Parameterized')&& it.htmlDisplayName.contains('Mockito')}).filename == TemplateRegistry.SPOCK_PARAMETERIZED_MOCKITO_GROOVY_TEMPLATE
        assert descriptors.find({it.htmlDisplayName.contains('Specs2')&& it.htmlDisplayName.contains('Mockito')}).filename == TemplateRegistry.SPECS2_MOCKITO_SCALA_TEMPLATE
        assert descriptors.find({it.htmlDisplayName.contains('JUnit5')&& it.htmlDisplayName.contains('Mockito')}).displayName.find("JUnit5.*Mockito")!=null
    }

}

