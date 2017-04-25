package com.weirddev.testme.intellij.template.context;

/**
 * Created by Yaron Yamin on 24/10/2016.
 */
public interface TestMeTemplateParams {

    String TESTED_CLASS = "TESTED_CLASS"; //Target test Class
    String TESTED_CLASS_METHODS = "TESTED_CLASS_METHODS";
    String TESTED_CLASS_FIELDS = "TESTED_CLASS_FIELDS";

    String PACKAGE_NAME = "PACKAGE_NAME";
    String CLASS_NAME = "CLASS_NAME";

    String JAVA_TEST_BUILDER = "JavaTestBuilder";
    String GROOVY_TEST_BUILDER = "GroovyTestBuilder";
    String STRING_UTILS = "StringUtils";
    String MOCKITO_UTILS = "MockitoUtils";
    String TEST_SUBJECT_UTILS = "TestSubjectUtils";
    String MAX_RECURSION_DEPTH = "MAX_RECURSION_DEPTH";

    String MONTH_NAME_EN = "MONTH_NAME_EN";
    String DAY_NUMERIC = "DAY_NUMERIC"; //Current Day of month for numeric calculations. i.e. if today is January the 2nd - DAY_NUMERIC = 2. DAY = 02
    String HOUR_NUMERIC = "HOUR_NUMERIC"; //Current Hour for numeric calculations. i.e. if the current time is 09:45 - HOUR_NUMERIC = 9. HOUR = 09
    String MINUTE_NUMERIC = "MINUTE_NUMERIC"; //Current Minute for numeric calculations. i.e. if the current time is 09:05 - MINUTE_NUMERIC = 5. MINUTE = 05
    String SECOND_NUMERIC = "SECOND_NUMERIC"; //Current Second for numeric calculations. i.e. if the current time is 09:15:05 - SECOND_NUMERIC = 5
}
