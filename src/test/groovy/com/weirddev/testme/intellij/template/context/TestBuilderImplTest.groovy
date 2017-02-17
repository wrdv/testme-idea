package com.weirddev.testme.intellij.template.context

import spock.lang.Specification

/**
 * Date: 2/16/2017
 * @author Yaron Yamin
 */
class TestBuilderImplTest extends Specification {

    def "stripGenerics"() {
        given:
        def testBuilder = new TestBuilderImpl()

        expect:
        testBuilder.stripGenerics(canonicalName) == result

        where:
        canonicalName               | result
        "java.util.Set"             | "java.util.Set"
        "java.util.Set<Fire>"       | "java.util.Set"
        "java.util.Set<List<Fire>>" | "java.util.Set"
    }

    def "extractGenerics"() {
        given:
        def testBuilder = new TestBuilderImpl()

        expect:
        testBuilder.extractGenerics(canonicalName) == result

        where:
        canonicalName               | result
        "java.util.Set"             | ""
        "java.util.Set<Fire>"       | "<Fire>"
        "java.util.Set<List<Fire>>" | "<List<Fire>>"
    }

    def "resolveType"() {
        expect:
        result == new TestBuilderImpl().resolveType(new Type(canonicalName, "Set", "java.util", false, false, []), replacementMap as HashMap)

        where:
        result                          | canonicalName               | replacementMap
        "java.util.HashSet<Fire>"       | "java.util.Set<Fire>"       | ["java.util.Set": "java.util.HashSet<TYPES>"]
        "java.util.HashSet<List<Fire>>" | "java.util.Set<List<Fire>>" | ["java.util.Set": "java.util.HashSet<TYPES>"]
        "java.util.Set<List<Fire>>"     | "java.util.Set<List<Fire>>" | ["java.util.SetZ": "java.util.HashSet<TYPES>"]
        "java.util.Set<List<Fire>>"     | "java.util.Set<List<Fire>>" | []
        "java.util.HashSet"             | "java.util.Set<List<Fire>>" | ["java.util.Set": "java.util.HashSet"]
        "java.util.Arrays.asList"       | "java.util.Set<Fire>"       | ["java.util.Set": "java.util.Arrays.asList"]
        "HashSet<Fire>"                 | "Set<Fire>"                 | ["Set": "HashSet<TYPES>"]
    }
}
