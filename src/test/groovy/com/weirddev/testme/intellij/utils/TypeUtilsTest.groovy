package com.weirddev.testme.intellij.utils

import spock.lang.Specification
import spock.lang.Unroll

class TypeUtilsTest extends Specification {

    @Unroll
    def "is Language Base Class where typeCanonicalName=#typeCanonicalName then expect: #expectedResult"() {
        expect:
        TypeUtils.isLanguageBaseClass(typeCanonicalName) == expectedResult

        where:
        typeCanonicalName                 || expectedResult
        "java.lang.Object"                || true
        "java.lang.Class"                 || true
        "groovy.lang.GroovyObjectSupport" || true
        "java.lang.String"                || false
    }

    @Unroll
    def "is Basic Type where qualifiedName=#qualifiedName then expect: #expectedResult"() {
        expect:
        TypeUtils.isBasicType(qualifiedName) == expectedResult

        where:
        qualifiedName         || expectedResult
        "java.lang.String"    || true
        "scala.Predef.String" || true
        "byte"                || true
        "java.lang.Object"    || false
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme