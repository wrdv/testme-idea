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
        assert "java.util.HashSet<Fire>" ==  new ClassUtils().replaceType("java.util.Set<Fire>","java.util.HashSet%1\$s")
    }
    void testReplaceTypeWithNesting() {
        assert "java.util.HashSet<List<Fire>>" ==  new ClassUtils().replaceType("java.util.Set<List<Fire>>","java.util.HashSet%1\$s")
    }
    void testReplaceTypeWithoutGenerics() {
        assert "java.util.Arrays.asList" ==  new ClassUtils().replaceType("java.util.Set<Fire>","java.util.Arrays.asList")
    }
    void testReplaceTypeClass() {
        assert "HashSet<Fire>" ==  new ClassUtils().replaceType("Set<Fire>","HashSet%1\$s")
    }

    void testExtractGenerics() {
        assert "<List<Fire>>" ==  new ClassUtils().extractGenerics("java.util.Set<List<Fire>>")
    }
}
