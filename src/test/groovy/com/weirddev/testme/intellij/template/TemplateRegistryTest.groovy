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
        assert descriptors.find({it.tokenizedDisplayName.contains('JUnit4')&& it.tokenizedDisplayName.contains('Groovy')}).filename == TemplateRegistry.JUNIT4_MOCKITO_GROOVY_TEMPLATE
        assert descriptors.find({it.tokenizedDisplayName.contains('<i>Spock</i>')&& it.tokenizedDisplayName.contains('Groovy')}).filename == TemplateRegistry.SPOCK_MOCKITO_GROOVY_TEMPLATE
        assert descriptors.find({it.tokenizedDisplayName.contains('JUnit5')&& it.tokenizedDisplayName.contains('Mockito')}).filename == TemplateRegistry.JUNIT5_MOCKITO_JAVA_TEMPLATE
        assert descriptors.find({it.tokenizedDisplayName.contains('TestNG')&& it.tokenizedDisplayName.contains('Mockito')}).filename == TemplateRegistry.TESTNG_MOCKITO_JAVA_TEMPLATE
        assert descriptors.find({it.tokenizedDisplayName.contains('Spock Parameterized')&& it.tokenizedDisplayName.contains('Mockito')}).filename == TemplateRegistry.SPOCK_PARAMETERIZED_MOCKITO_GROOVY_TEMPLATE
        assert descriptors.find({it.tokenizedDisplayName.contains('Specs2')&& it.tokenizedDisplayName.contains('Mockito')}).filename == TemplateRegistry.SPECS2_MOCKITO_SCALA_TEMPLATE
        assert descriptors.find({it.tokenizedDisplayName.contains('JUnit5')&& it.tokenizedDisplayName.contains('Mockito')}).displayName.find("JUnit5.*Mockito")!=null
    }
}

