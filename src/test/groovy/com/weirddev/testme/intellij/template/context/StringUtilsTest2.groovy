package com.weirddev.testme.intellij.template.context

import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/**
 * Date: 30/03/2017
 * @author Yaron Yamin
 */
class StringUtilsTest2 {
    @InjectMocks
    StringUtils stringUtils

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

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

    @Test
    void testConcat() {
        String result = stringUtils.concat(["String"])
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testGetPackageAsPath() {
        String result = StringUtils.getPackageAsPath("pckge")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testRemoveUnderScores() {
        String result = StringUtils.removeUnderScores("data")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testRemoveAndHump() {
        String result = StringUtils.removeAndHump("data")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testRemoveAndHump2() {
        String result = StringUtils.removeAndHump("data", "replaceThis")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testFirstLetterCaps() {
        String result = StringUtils.firstLetterCaps("data")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testCapitalizeFirstLetter() {
        String result = StringUtils.capitalizeFirstLetter("data")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testSplit() {
        String[] result = StringUtils.split("line", "delim")
        assert result == ["replaceMeWithExpectedResult"] as String[]
    }

    @Test
    void testChop() {
        String result = StringUtils.chop("s", 0)
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testChop2() {
        String result = StringUtils.chop("s", 0, "eol")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testStringSubstitution() {
        StringBuffer result = StringUtils.stringSubstitution("argStr", null)
        assert result == null
    }

    @Test
    void testStringSubstitution2() {
        StringBuffer result = StringUtils.stringSubstitution("argStr", ["String": "String"])
        assert result == null
    }

    @Test
    void testFileContentsToString() {
        String result = StringUtils.fileContentsToString("file")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testCollapseNewlines() {
        String result = StringUtils.collapseNewlines("argStr")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testCollapseSpaces() {
        String result = StringUtils.collapseSpaces("argStr")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testSub() {
        String result = StringUtils.sub("line", "oldString", "newString")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testStackTrace() {
        String result = StringUtils.stackTrace(null)
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testNormalizePath() {
        String result = StringUtils.normalizePath("path")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testSelect() {
        String result = stringUtils.select(true, "trueString", "falseString")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testAllEmpty() {
        boolean result = stringUtils.allEmpty(["String"])
        assert result == true
    }

    @Test
    void testTrimStrings() {
        List result = StringUtils.trimStrings(["String"])
        assert result == ["String"]
    }

    @Test
    void testNullTrim() {
        String result = StringUtils.nullTrim("s")
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme