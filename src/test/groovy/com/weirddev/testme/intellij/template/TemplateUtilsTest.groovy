package com.weirddev.testme.intellij.template

/**
 * Date: 16/11/2016
 * @author Yaron Yamin
 */
class TemplateUtilsTest extends GroovyTestCase {
    void testExtractClassName() {
        assert "Set" ==  new TemplateUtils().extractClassName("java.util.Set")
    }

    void testStripGenerics() {
        assert "java.util.Set" ==  new TemplateUtils().stripGenerics("java.util.Set<Fire>")
    }

    void testReplaceType() {
        assert "java.util.HashSet<Fire>" ==  new TemplateUtils().replaceType("java.util.Set<Fire>","java.util.HashSet")
    }
    void testReplaceTypeClass() {
        assert "HashSet<Fire>" ==  new TemplateUtils().replaceType("Set<Fire>","HashSet")
    }
}
