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
        assert descriptors.size() ==4
        assert descriptors.find({it.tokenizedDisplayName.contains('JUnit4')&& it.tokenizedDisplayName.contains('Groovy')}).filename == TemplateRegistry.JUNIT4_GROOVY_MOCKITO_JAVA_TEMPLATE
        assert descriptors.find({it.tokenizedDisplayName.contains('Spock')&& it.tokenizedDisplayName.contains('Groovy')}).filename == TemplateRegistry.SPOCK_GROOVY_MOCKITO_JAVA_TEMPLATE
        assert descriptors.find({it.tokenizedDisplayName.contains('JUnit5')&& it.tokenizedDisplayName.contains('Mockito')}).filename == TemplateRegistry.JUNIT5_MOCKITO_JAVA_TEMPLATE
        assert descriptors.find({it.tokenizedDisplayName.contains('JUnit5')&& it.tokenizedDisplayName.contains('Mockito')}).displayName.find("JUnit5.*Mockito")!=null
    }
}

