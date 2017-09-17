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

    @Test
    void testHasLine() {
        assert StringUtils.hasLine("toFind", "toFind")
        assert StringUtils.hasLine(" toFind  ", "toFind")
        assert StringUtils.hasLine(" toFinz \n\r toFind ", "toFind")
        assert StringUtils.hasLine(" toFinz \n toFind ", "toFind")
        assert StringUtils.hasLine("toFind \n something", "toFind")
        assert !StringUtils.hasLine(" toFinz \n\r toFind12 ", "toFind")
        assert !StringUtils.hasLine("#toFind", "toFind")
        assert !StringUtils.hasLine(" toFind  12", "toFind")
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme