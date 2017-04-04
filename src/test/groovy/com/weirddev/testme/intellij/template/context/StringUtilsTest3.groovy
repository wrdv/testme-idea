package com.weirddev.testme.intellij.template.context

import org.junit.Before
import org.junit.Test

/**
 * Date: 30/03/2017
 * @author Yaron Yamin
 */
class StringUtilsTest3 {

    StringUtils stringUtils = new StringUtils()

    @Test
    void testDeCapitalizeFirstLetter() {
        String result = StringUtils.deCapitalizeFirstLetter("data")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testCamelCaseToWords() {
        String result = StringUtils.camelCaseToWords("data")
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme