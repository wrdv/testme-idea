package com.weirddev.testme.intellij.template.context

/**
 * Date: 2/16/2017
 * @author Yaron Yamin
 */
class TestBuilderTest extends GroovyTestCase {
    void testStripGenerics() {
        assert "java.util.Set" ==  new TestBuilder().stripGenerics("java.util.Set<Fire>")
    }
    void testReplaceType() {
        assert "java.util.HashSet<Fire>" ==  new TestBuilder().replaceType("java.util.Set<Fire>","java.util.HashSet%1\$s")
    }
    void testReplaceTypeWithNesting() {
        assert "java.util.HashSet<List<Fire>>" ==  new TestBuilder().replaceType("java.util.Set<List<Fire>>","java.util.HashSet%1\$s")
    }
    void testReplaceTypeWithoutGenerics() {
        assert "java.util.Arrays.asList" ==  new TestBuilder().replaceType("java.util.Set<Fire>","java.util.Arrays.asList")
    }
    void testReplaceTypeClass() {
        assert "HashSet<Fire>" ==  new TestBuilder().replaceType("Set<Fire>","HashSet%1\$s")
    }

    void testExtractGenerics() {
        assert "<List<Fire>>" ==  new TestBuilder().extractGenerics("java.util.Set<List<Fire>>")
    }
    void testResolveTypeName() {
        assert "java.util.HashSet" ==  new TestBuilder().resolveTypeName(new Type("java.util.Set<List<Fire>>","Set","java.util",false,false,[]),["java.util.Set":"java.util.HashSet"])
    }
    void testResolveTypeNameWithGenerics() {
        assert "java.util.HashSet<List<Fire>>" ==  new TestBuilder().resolveTypeName(new Type("java.util.Set<List<Fire>>","Set","java.util",false,false,[]),["java.util.Set":"java.util.HashSet%1\$s"])
    }
}
