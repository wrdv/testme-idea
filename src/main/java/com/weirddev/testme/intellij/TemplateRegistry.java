package com.weirddev.testme.intellij;

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

    public static final String JUNIT5_MOCKITO_JAVA_TEMPLATE = "TestMe with JUnit5 & Mockito.java";

    static {
/*
        //html version:
        URL juDark = TemplateRegistry.class.getResource("/icons/junit_dark.png");
        URL mockito = TemplateRegistry.class.getResource("/icons/mockito.png");
        URL ju5 = TemplateRegistry.class.getResource("/icons/junit5.png");
        templateDescriptors.add(new TemplateDescriptor("<html>with <i><b>JUnit4</b></i><img src='"+ juDark +"'> & <i><b>Mockito</b></i><img src='"+ mockito +"'></html>", "TestMe with JUnit4 & Mockito.java"));
        templateDescriptors.add(new TemplateDescriptor("<html>with <i><b>JUnit5</b></i><img src='"+ ju5 +"'> & <i><b>Mockito</b></i><img src='"+ mockito +"'></html>", "TestMe with JUnit4 & Mockito.java"));
*/
        templateDescriptors.add(new TemplateDescriptor("with JUnit4<JUnit4>& Mockito<Mockito>", JUNIT4_MOCKITO_JAVA_TEMPLATE));
        templateDescriptors.add(new TemplateDescriptor("with JUnit5<JUnit5>& Mockito<Mockito>", JUNIT5_MOCKITO_JAVA_TEMPLATE));
    }
    public List<TemplateDescriptor> getTemplateDescriptors(){
        return templateDescriptors;
    }
}
