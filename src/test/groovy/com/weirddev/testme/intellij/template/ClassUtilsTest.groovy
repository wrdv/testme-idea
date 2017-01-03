package com.weirddev.testme.intellij.template

import com.weirddev.testme.intellij.template.context.ClassUtils

/**
 * Date: 16/11/2016
 * @author Yaron Yamin
 */
class ClassUtilsTest extends GroovyTestCase {
    void testExtractClassName() {
        assert "Set" ==  new ClassUtils().extractClassName("java.util.Set")
    }

    void testStripGenerics() {
        assert "java.util.Set" ==  new ClassUtils().stripGenerics("java.util.Set<Fire>")
    }

    void testReplaceType() {
        assert "java.util.HashSet<Fire>" ==  new ClassUtils().replaceType("java.util.Set<Fire>","java.util.HashSet")
    }
    void testReplaceTypeClass() {
        assert "HashSet<Fire>" ==  new ClassUtils().replaceType("Set<Fire>","HashSet")
    }
}
