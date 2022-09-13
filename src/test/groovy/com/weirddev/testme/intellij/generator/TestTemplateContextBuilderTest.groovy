package com.weirddev.testme.intellij.generator

import org.junit.Test
import org.mockito.Mockito
/**
 * Date: 09/12/2016
 * @author Yaron Yamin
 */
class TestTemplateContextBuilderTest {

        def fields = [:] as Map<String,Object>
        def builderFactory = Mockito.mock(MockBuilderFactory)
    @Test
    void testPopulateDateFieldsSingleDigits() {
        new TestTemplateContextBuilder(builderFactory).populateDateFields(fields, new GregorianCalendar(2020, 11, 5, 7, 5,3))
        assert fields == ["MONTH_NAME_EN":"December", "DAY_NUMERIC":5, "HOUR_NUMERIC":7, "MINUTE_NUMERIC":5, "SECOND_NUMERIC":3]
    }
    @Test
    void testPopulateDateFieldsDoubleDigits() {
        new TestTemplateContextBuilder(builderFactory).populateDateFields(fields, new GregorianCalendar(2020, 0,25, 22, 45,33))
        assert fields == ["MONTH_NAME_EN":"January", "DAY_NUMERIC":25, "HOUR_NUMERIC":22, "MINUTE_NUMERIC":45, "SECOND_NUMERIC":33]
    }
}
