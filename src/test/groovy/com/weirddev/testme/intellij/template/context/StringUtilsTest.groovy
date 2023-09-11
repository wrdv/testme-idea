package com.weirddev.testme.intellij.template.context

import org.junit.Test

/**
 * Date: 26/03/2017
 * @author Yaron Yamin
 */
class StringUtilsTest {

    @Test
    void testCapitalizeFirstLetter() {
        String result = StringUtils.capitalizeFirstLetter("replaceMeWithExpectedResult")
        assert result == "ReplaceMeWithExpectedResult"
    }
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
    @Test
    void testRemoveSuffix() {
        String result = StringUtils.removeSuffix("strWith_suffix_and_more_stuff_and_suffix", "suffix")
        assert result == "strWith_suffix_and_more_stuff_and_"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme