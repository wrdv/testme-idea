package com.weirddev.testme.intellij.template.context

import spock.lang.Specification

/**
 * Date: 2/16/2017
 * @author Yaron Yamin
 */
class JavaTestBuilderImplTest extends Specification {
    static Type fearType = new Type("com.example.foes.Fear", "Fear", "com.example.foes", false, false, [])
    static Type stringType = new Type("java.lang.String", "String", "java.lang", false, false, [])
    static Type queueWithTypeParams = new Type("java.util.Queue<java.util.List<com.example.foes.Fear>>", "Queue<List<Fear>>", "java.util", false, false, [new Type("java.util.List<com.example.foes.Fear>", "List<Fear>", "java.util", false, false, [fearType])])
    static Map globalReplacementMap = ["java.util.Queue"       : "new java.util.LinkedList<TYPES>(java.util.Arrays.asList(<VAL>))",
                                       "java.util.Set"         : "new java.util.HashSet<TYPES>(java.util.Arrays.asList(<VAL>))",
                                       "java.util.Map"         : "new java.util.HashMap<TYPES>(){{put(<VAL>,<VAL>);}}",
                                       "java.util.NavigableMap": "new java.util.TreeMap<TYPES>(new java.util.HashMap<TYPES>(){{put(<VAL>,<VAL>);}})",
                                       "java.util.List"        : "java.util.Arrays.<TYPES>asList(<VAL>)"
    ]
    def testBuilder = new JavaTestBuilderImpl(5)

    def "stripGenerics"() {

        expect:
        testBuilder.stripGenerics(canonicalName) == result

        where:
        canonicalName               | result
        "java.util.Set"             | "java.util.Set"
        "java.util.Set<Fire>"       | "java.util.Set"
        "java.util.Set<List<Fire>>" | "java.util.Set"
    }

    def "extractGenerics"() {

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
        result == testBuilder.resolveType(new Type(canonicalName, "Set", "java.util", false, false, []), replacementMap as HashMap)

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

    def "renderJavaCallParam - generic collection"() {
        expect:
        testBuilder.renderJavaCallParam(type, "paramName", globalReplacementMap, [:], 9) == result

        where:
        result                                                                                                                                                                           | type
        "new java.util.LinkedList<java.util.List<com.example.foes.Fear>>(java.util.Arrays.asList(java.util.Arrays.<com.example.foes.Fear>asList(new com.example.foes.Fear())))"          | queueWithTypeParams
        "new java.util.HashSet(java.util.Arrays.asList(\"String\"))"                                                                                                                     | new Type("java.util.Set", "Set", "java.util", false, false, [])
        "new java.util.HashMap<java.lang.String,com.example.foes.Fear>(){{put(\"String\",new com.example.foes.Fear());}}"                                                                | new Type("java.util.Map<java.lang.String,com.example.foes.Fear>", "Map", "java.util", false, false, [stringType, fearType])
        "new java.util.HashMap(){{put(\"String\",\"String\");}}"                                                                                                                         | new Type("java.util.Map", "Map", "java.util", false, false, [])
        "new java.util.TreeMap<java.lang.String,com.example.foes.Fear>(new java.util.HashMap<java.lang.String,com.example.foes.Fear>(){{put(\"String\",new com.example.foes.Fear());}})" | new Type("java.util.NavigableMap<java.lang.String,com.example.foes.Fear>", "NavigableMap", "java.util", false, false, [stringType, fearType])
    }
}
