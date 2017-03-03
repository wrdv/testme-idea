package com.weirddev.testme.intellij.template

import com.weirddev.testme.intellij.template.context.Type

/**
 * Date: 11/7/2016
 * @author Yaron Yamin
 */
class TypeTest extends GroovyTestCase {

    void testComplexGenericTypeCreation() {
        assert new Type("java.util.Map", "Map", "java.util", false, false, false, false, null)==new Type("java.util.Map<com.example.foes.Pokemon,java.util.List<com.example.foes.Fire>>")
    }
    void testGenericTypeCreation() {
        assert new Type("java.util.Set", "Set", "java.util", false, false,false,false, null)==new Type("java.util.Set<com.example.foes.Ice>")
    }

}
