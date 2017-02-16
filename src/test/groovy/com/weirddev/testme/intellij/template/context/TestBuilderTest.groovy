package com.weirddev.testme.intellij.template.context

import spock.lang.Specification

/**
 * Date: 2/16/2017
 * @author Yaron Yamin
 */
class TestBuilderTest extends Specification {

    def "stripGenerics"() {
        given:
        def testBuilder = new TestBuilder()

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
        def testBuilder = new TestBuilder()

        expect:
        testBuilder.extractGenerics(canonicalName) == result

        where:
        canonicalName               | result
        "java.util.Set"             | ""
        "java.util.Set<Fire>"       | "<Fire>"
        "java.util.Set<List<Fire>>" | "<List<Fire>>"
    }

    def "replaceType"() {
        given:
        def testBuilder = new TestBuilder()

        expect:
        testBuilder.replaceType(canonicalName, replacementType) == result

        where:
        result                          | canonicalName               | replacementType
        "java.util.HashSet<Fire>"       | "java.util.Set<Fire>"       | "java.util.HashSet%1\$s"
        "java.util.HashSet<List<Fire>>" | "java.util.Set<List<Fire>>" | "java.util.HashSet%1\$s"
        "java.util.Arrays.asList"       | "java.util.Set<Fire>"       | "java.util.Arrays.asList"
        "HashSet<Fire>"                 | "Set<Fire>"                 | "HashSet%1\$s"
    }

    def "resolveTypeName"() {
        expect:
        result == new TestBuilder().resolveTypeName(new Type(canonicalName, "Set", "java.util", false, false, []), replacementMap as HashMap)

        where:
        result                          | canonicalName               | replacementMap
        "java.util.HashSet<Fire>"       | "java.util.Set<Fire>"       | ["java.util.Set": "java.util.HashSet%1\$s"]
        "java.util.HashSet<List<Fire>>" | "java.util.Set<List<Fire>>" | ["java.util.Set": "java.util.HashSet%1\$s"]
        "java.util.Set<List<Fire>>"     | "java.util.Set<List<Fire>>" | ["java.util.SetZ": "java.util.HashSet%1\$s"]
        "java.util.Set<List<Fire>>"     | "java.util.Set<List<Fire>>" | []
        "java.util.HashSet"             | "java.util.Set<List<Fire>>" | ["java.util.Set": "java.util.HashSet"]
        "java.util.Arrays.asList"       | "java.util.Set<Fire>"       | ["java.util.Set": "java.util.Arrays.asList"]
    }
}
