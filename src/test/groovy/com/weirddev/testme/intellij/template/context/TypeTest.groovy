package com.weirddev.testme.intellij.template.context

import com.weirddev.testme.intellij.utils.ClassNameUtils
import org.junit.Test

/**
 * Date: 11/7/2016
 * @author Yaron Yamin
 */
class TypeTest{

    @Test
    void testComplexGenericTypeCreation() {
        assert new Type("java.util.Map", "Map", "java.util", false, false, false, false, 0, false, null)== typeFromCanonicalName("java.util.Map<com.example.foes.Pokemon,java.util.List<com.example.foes.Fire>>")
    }
    @Test
    void testGenericTypeCreation() {
        assert new Type("java.util.Set", "Set", "java.util", false, false, false, false, 0, false, null)== typeFromCanonicalName("java.util.Set<com.example.foes.Ice>")
    }

    static Type typeFromCanonicalName(String canonicalName) {
        return new Type(ClassNameUtils.extractContainerType(canonicalName), ClassNameUtils.extractClassName(canonicalName), ClassNameUtils.extractPackageName(canonicalName),false, false,false, ClassNameUtils.isArray(canonicalName), ClassNameUtils.arrayDimensions(canonicalName), ClassNameUtils.isVarargs(canonicalName),null);
    }

}