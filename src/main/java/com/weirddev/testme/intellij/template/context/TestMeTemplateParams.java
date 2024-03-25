package com.weirddev.testme.intellij.template.context;

import com.weirddev.testme.intellij.template.FileTemplateConfig;

/**
 * Custom params injected into Velocity context for test generation templates. Can be accessed from Velocity test templates with the $ operator. i.e. $TESTED_CLASS.canonicalName.
 *
 * Date: 24/10/2016.
 * @author Yaron Yamin
 */
public interface TestMeTemplateParams {
    /**
     * {@link com.weirddev.testme.intellij.template.context.Type} - representing target Class under test
     * @see Type
     */
    String TESTED_CLASS = "TESTED_CLASS";
    /**
     * Tested class package
     */
    String PACKAGE_NAME = "PACKAGE_NAME";
    /**
     * Tested class name
     */
    String CLASS_NAME = "CLASS_NAME";
    /**
     * instance of {@link com.weirddev.testme.intellij.template.context.TestBuilder}
     * @see TestBuilder
     */
    String TestBuilder = "TestBuilder";
    /**
     * instance of {@link com.weirddev.testme.intellij.template.context.StringUtils}
     * @see StringUtils
     */
    String StringUtils = "StringUtils";
    /**
     * instance of {@link com.weirddev.testme.intellij.template.context.MockitoMockBuilder}
     * @see MockitoMockBuilder
     */
    String MockitoMockBuilder = "MockitoMockBuilder";
    /**
     * instance of {@link com.weirddev.testme.intellij.template.context.PowerMockBuilder}
     * @see PowerMockBuilder
     */
    String PowerMockBuilder = "PowerMockBuilder";
    /**
     * instance of {@link com.weirddev.testme.intellij.template.context.TestSubjectInspector}
     * @see TestSubjectInspector
     */
    String TestSubjectUtils = "TestSubjectUtils";
    /**
     * JDK version of test module were test is being generated
     * instance of {@link com.intellij.util.lang.JavaVersion}
     * @see TestSubjectInspector
     */
    String JAVA_VERSION = "JAVA_VERSION";
    /**
     * Jar file paths of test module's dependencies
     * instance of {@link java.util.List<String>}
     * @see TestSubjectInspector
     */
    String TestedClasspathJars = "TestedClasspathJars";

    /**
     * user checked and selected mock field name list
     * instance of {@link java.util.List<String>}
     */
    String USER_CHECKED_MOCK_FIELDS = "USER_CHECKED_MOCK_FIELDS";

    /**
     * user checked and selected test method id list
     * instance of {@link java.util.List<String>}
     */
    String USER_CHECKED_TEST_METHODS = "USER_CHECKED_TEST_METHODS";

    /**
     * configured max recursion depth for object graph introspection
     * @see FileTemplateConfig#getMaxRecursionDepth()
     */
    String MAX_RECURSION_DEPTH = "MAX_RECURSION_DEPTH";
    /**
     * Current English Month name. format MMMM
     */
    String MONTH_NAME_EN = "MONTH_NAME_EN";
    /**
     * Current Day of month for numeric calculations. i.e. if today is January the 2nd then DAY_NUMERIC is 2
     */
    String DAY_NUMERIC = "DAY_NUMERIC";
    /**
     * Current Hour for numeric calculations. i.e. if the current time is 09:45 then HOUR_NUMERIC is 9
     */
    String HOUR_NUMERIC = "HOUR_NUMERIC";
    /**
     * Current Minute for numeric calculations. i.e. if the current time is 09:05 then MINUTE_NUMERIC is 5
     */
    String MINUTE_NUMERIC = "MINUTE_NUMERIC";
    /**
     * Current Second for numeric calculations. i.e. if the current time is 09:15:05 then SECOND_NUMERIC is 5
     */
    String SECOND_NUMERIC = "SECOND_NUMERIC";
    /**
     * Language of class under test: Groovy, Java, Scala
     */
    String TESTED_CLASS_LANGUAGE = "TESTED_CLASS_LANGUAGE";
}
