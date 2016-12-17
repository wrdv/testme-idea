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
    static {
        templateDescriptors.add(new TemplateDescriptor("TestMe with <JUnit4>JUnit4 & <Mockito>Mockito", "TestMe with JUnit4 & Mockito.java"));
        templateDescriptors.add(new TemplateDescriptor("TestMe with <JUnit5>JUnit5 & <Mockito>Mockito", "TestMe with JUnit5 & Mockito.java"));
    }
    public List<TemplateDescriptor> getTemplateDescriptors(){
        return templateDescriptors;
    }
}
