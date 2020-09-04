package com.weirddev.testme.intellij.template

import com.weirddev.testme.intellij.common.utils.LanguageUtils
import com.weirddev.testme.intellij.template.context.Language
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

    @Test
    void testGetIncludedTemplateDescriptors() {
        assert new TemplateRegistry().getIncludedTemplateDescriptors() == [
                new TemplateDescriptor(language: Language.Groovy, htmlDisplayName:'TestMe macros.groovy', displayName:'TestMe macros.groovy', tokenizedName:'TestMe macros.groovy', filename:'TestMe macros.groovy', dependantPlugins:[LanguageUtils.GROOVY_PLUGIN_ID]),
                new TemplateDescriptor(language: Language.Java, htmlDisplayName:'TestMe common macros.java', displayName:'TestMe common macros.java', tokenizedName:'TestMe common macros.java', filename:'TestMe common macros.java', dependantPlugins:null),
                new TemplateDescriptor(language: Language.Java, htmlDisplayName:'TestMe macros.java', displayName:'TestMe macros.java', tokenizedName:'TestMe macros.java', filename:'TestMe macros.java', dependantPlugins:null),
                new TemplateDescriptor(language: Language.Java, htmlDisplayName:'TestMe Footer.java', displayName:'TestMe Footer.java', tokenizedName:'TestMe Footer.java', filename:'TestMe Footer.java', dependantPlugins:null),
                new TemplateDescriptor(language: Language.Scala, htmlDisplayName:'TestMe macros.scala', displayName:'TestMe macros.scala', tokenizedName:'TestMe macros.scala', filename:'TestMe macros.scala', dependantPlugins:[LanguageUtils.SCALA_PLUGIN_ID])]

    }
}

