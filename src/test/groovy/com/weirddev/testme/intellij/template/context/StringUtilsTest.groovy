package com.weirddev.testme.intellij.template.context

import org.junit.Test

/**
 * Date: 26/03/2017
 * @author Yaron Yamin
 */
class StringUtilsTest {

    @Test
    void testDeCapitalizeFirstLetter() {
        String result = StringUtils.deCapitalizeFirstLetter("ReplaceMeWithExpectedResult")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testCamelCaseToWords() {
        String result = StringUtils.camelCaseToWords("replaceMeWithExpectedResult")
        assert result == "replace Me With Expected Result"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme