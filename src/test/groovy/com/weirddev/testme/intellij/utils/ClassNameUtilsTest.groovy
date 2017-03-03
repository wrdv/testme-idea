package com.weirddev.testme.intellij.utils

import org.junit.Test

/**
 * Date: 03/03/2017
 * @author Yaron Yamin
 */
class ClassNameUtilsTest {

    @Test
    void testIsArray() {
        boolean result = ClassNameUtils.isArray("canonicalName")
        assert result == false
    }

    @Test
    void testExtractClassName() {
        assert "Set" ==  new ClassNameUtils().extractClassName("java.util.Set")
    }

    @Test
    void testExtractPackageName() {
        String result = ClassNameUtils.extractPackageName("java.util.Set")
        assert result == "java.util"
    }

    @Test
    void testStripArrayDesignator() {
        String result = ClassNameUtils.stripArrayDesignator("typeName")
        assert result == "typeName"
    }

    @Test
    void testExtractContainerType() {
        String result = ClassNameUtils.extractContainerType("Set<Something>")
        assert result == "Set"
    }

    @Test
    void testExtractTargetPropertyNameForSetter() {
        String result = ClassNameUtils.extractTargetPropertyName("setMyName", true, false)
        assert result == "myName"
    }
    @Test
    void testExtractTargetPropertyNameForGetter() {
        String result = ClassNameUtils.extractTargetPropertyName("getMyName", false, true)
        assert result == "myName"
    }
    @Test
    void testExtractTargetPropertyNameForIsGetter() {
        String result = ClassNameUtils.extractTargetPropertyName("isMyName", false, true)
        assert result == "myName"
    }

    @Test
    void testRemoveFromCamelCaseName() {
        String result = ClassNameUtils.removeFromCamelCaseName("setMyName", "set")
        assert result == "myName"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme