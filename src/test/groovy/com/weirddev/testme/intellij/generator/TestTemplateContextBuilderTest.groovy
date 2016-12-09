package com.weirddev.testme.intellij.generator

/**
 * Date: 09/12/2016
 * @author Yaron Yamin
 */
class TestTemplateContextBuilderTest extends GroovyTestCase {

    void testPopulateDateFieldsSingleDigits() {
        def fields = [:]
        new TestTemplateContextBuilder().populateDateFields(fields, new GregorianCalendar(2020, 11, 5, 7, 5))
        assert fields == ["MONTH_NAME_EN":"December", "DAY_NUMERIC":5, "HOUR_NUMERIC":7, "MINUTE_NUMERIC":5]
    }
    void testPopulateDateFieldsDoubleDigits() {
        def fields = [:]
        new TestTemplateContextBuilder().populateDateFields(fields, new GregorianCalendar(2020, 0,25, 22, 45))
        assert fields == ["MONTH_NAME_EN":"January", "DAY_NUMERIC":25, "HOUR_NUMERIC":22, "MINUTE_NUMERIC":45]
    }
}
