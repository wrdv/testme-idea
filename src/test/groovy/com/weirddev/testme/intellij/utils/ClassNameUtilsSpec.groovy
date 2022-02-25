package com.weirddev.testme.intellij.utils

import spock.lang.*

class ClassNameUtilsSpec extends Specification {


    def "stripGenerics"() {

        expect:
        ClassNameUtils.stripGenerics(canonicalName) == result

        where:
        canonicalName               | result
        "java.util.Set"             | "java.util.Set"
        "java.util.Set<Fire>"       | "java.util.Set"
        "java.util.Set<List<Fire>>" | "java.util.Set"
    }

    def "extractGenerics"() {

        expect:
        ClassNameUtils.extractGenerics(canonicalName) == result

        where:
        canonicalName               | result
        "java.util.Set"             | ""
        "java.util.Set<Fire>"       | "<Fire>"
        "java.util.Set<List<Fire>>" | "<List<Fire>>"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme