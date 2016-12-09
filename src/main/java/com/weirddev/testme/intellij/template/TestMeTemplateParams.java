package com.weirddev.testme.intellij.template;

/**
 * Created by Yaron Yamin on 24/10/2016.
 */
public interface TestMeTemplateParams {
    String ATTRIBUTE_EXCEPTION = "EXCEPTION";
    String ATTRIBUTE_EXCEPTION_TYPE = "EXCEPTION_TYPE";
    String ATTRIBUTE_DESCRIPTION = "DESCRIPTION";
    String ATTRIBUTE_DISPLAY_NAME = "DISPLAY_NAME";

    String ATTRIBUTE_RETURN_TYPE = "RETURN_TYPE";
    String ATTRIBUTE_DEFAULT_RETURN_VALUE = "DEFAULT_RETURN_VALUE";
    String ATTRIBUTE_CALL_SUPER = "CALL_SUPER";

    String ATTRIBUTE_SIMPLE_CLASS_NAME = "SIMPLE_CLASS_NAME";
    String ATTRIBUTE_METHOD_NAME = "METHOD_NAME";
    //    String ourEncoding = CharsetToolkit.UTF8;

    String TESTED_CLASS_NAME = "TESTED_CLASS_NAME";
    String TESTED_CLASS_METHODS = "TESTED_CLASS_METHODS";
    String TESTED_CLASS_FIELDS = "TESTED_CLASS_FIELDS";

    String PACKAGE_NAME = "PACKAGE_NAME";
    String CLASS_NAME = "CLASS_NAME";

    String ATTRIBUTE_FILE_NAME = "FILE_NAME";
    String CLASS_UTILS = "ClassUtils";
    String MONTH_NAME_EN = "MONTH_NAME_EN";
    String MAX_RECURSION_DEPTH = "MAX_RECURSION_DEPTH";
    String STRING_UTILS = "StringUtils";
    String DAY_NUMERIC = "DAY_NUMERIC"; //Current Day of month for numeric calculations. i.e. if today is January the 2nd - DAY_NUMERIC = 2. DAY = 02
    String HOUR_NUMERIC = "HOUR_NUMERIC"; //Current Hour for numeric calculations. i.e. if the current time is 09:45 - HOUR_NUMERIC = 9. HOUR = 09
    String MINUTE_NUMERIC = "MINUTE_NUMERIC"; //Current Minute for numeric calculations. i.e. if the current time is 09:05 - MINUTE_NUMERIC = 5. MINUTE = 05

}
