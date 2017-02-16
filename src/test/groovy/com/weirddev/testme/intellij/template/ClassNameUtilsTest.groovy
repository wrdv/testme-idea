package com.weirddev.testme.intellij.template

import com.weirddev.testme.intellij.utils.ClassNameUtils

/**
 * Date: 16/11/2016
 * @author Yaron Yamin
 */
class ClassNameUtilsTest extends GroovyTestCase {
    void testExtractClassName() {
        assert "Set" ==  new ClassNameUtils().extractClassName("java.util.Set")
    }
}
