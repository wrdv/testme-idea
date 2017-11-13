package com.weirddev.testme.intellij.template;

import com.weirddev.testme.intellij.template.context.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 10/12/2016
 *
 * @author Yaron Yamin
 */
public class TemplateRegistry {

    static private List<TemplateDescriptor> templateDescriptors = new ArrayList<TemplateDescriptor>();

    public static final String JUNIT4_MOCKITO_JAVA_TEMPLATE = "TestMe with JUnit4 & Mockito.java";
    public static final String JUNIT4_GROOVY_MOCKITO_JAVA_TEMPLATE = "TestMe with Groovy, JUnit4 & Mockito.groovy";
    public static final String SPOCK_GROOVY_MOCKITO_JAVA_TEMPLATE = "TestMe with Groovy, Spock & Mockito.groovy";
    public static final String SPOCK_PARAMETERIZED_GROOVY_MOCKITO_JAVA_TEMPLATE = "TestMe with Parameterized Groovy, Spock & Mockito.groovy";
    public static final String JUNIT5_MOCKITO_JAVA_TEMPLATE = "TestMe with JUnit5 & Mockito.java";
    public static final String TESTNG_MOCKITO_JAVA_TEMPLATE = "TestMe with TestNG & Mockito.java";

    static {
/*
        //html version (has issues with horizontal alignment of img to text in JLabel):
        URL juDark = TemplateRegistry.class.getResource("/icons/junit_dark.png");
        URL mockito = TemplateRegistry.class.getResource("/icons/mockito.png");
        URL ju5 = TemplateRegistry.class.getResource("/icons/junit5.png");
        templateDescriptors.add(new TemplateDescriptor("<html>with <i><b>JUnit4</b></i><img src='"+ juDark +"'> & <i><b>Mockito</b></i><img src='"+ mockito +"'></html>", "TestMe with JUnit4 & Mockito.java"));
        templateDescriptors.add(new TemplateDescriptor("<html>with <i><b>JUnit5</b></i><img src='"+ ju5 +"'> & <i><b>Mockito</b></i><img src='"+ mockito +"'></html>", "TestMe with JUnit4 & Mockito.java"));
*/
        templateDescriptors.add(new TemplateDescriptor("<html>with <i>JUnit4</i></html><JUnit4><html>& <i>Mockito</i></html><Mockito>", JUNIT4_MOCKITO_JAVA_TEMPLATE, Language.Java));
        templateDescriptors.add(new TemplateDescriptor("<html>with <i>JUnit5</i></html><JUnit5><html>& <i>Mockito</i></html><Mockito>", JUNIT5_MOCKITO_JAVA_TEMPLATE, Language.Java));
        templateDescriptors.add(new TemplateDescriptor("<html>with <i>TestNG </i></html><TestNG><html>& <i>Mockito</i></html><Mockito>", TESTNG_MOCKITO_JAVA_TEMPLATE, Language.Java));
        templateDescriptors.add(new TemplateDescriptor("<html>with <i>Groovy</i></html><Groovy><html><i>JUnit4</i></html><JUnit4><html>& <i>Mockito</i></html><Mockito>", JUNIT4_GROOVY_MOCKITO_JAVA_TEMPLATE, Language.Groovy));
        templateDescriptors.add(new TemplateDescriptor("<html>with <i>Spock</i></html><Groovy><html> & <i>Mockito</i></html><Mockito>", SPOCK_GROOVY_MOCKITO_JAVA_TEMPLATE, Language.Groovy));
        templateDescriptors.add(new TemplateDescriptor("<html>with <i>Spock Parameterized</i></html><Groovy><html> & <i>Mockito</i></html><Mockito> (Experimental)", SPOCK_PARAMETERIZED_GROOVY_MOCKITO_JAVA_TEMPLATE, Language.Groovy));
    }
    public List<TemplateDescriptor> getTemplateDescriptors(){
        return templateDescriptors;
    }
}
