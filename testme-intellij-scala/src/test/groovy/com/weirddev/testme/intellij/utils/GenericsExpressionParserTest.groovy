package com.weirddev.testme.intellij.utils

import com.weirddev.testme.intellij.scala.utils.GenericsExpressionParser
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Date: 15/12/2017
 * @author Yaron Yamin
 */
class GenericsExpressionParserTest extends Specification {

    @Unroll
    def "extract Generic Types where canonicalName=#canonicalName then expect: #expectedResult"() {
        expect:
        GenericsExpressionParser.extractGenericTypes(canonicalName) == expectedResult

        where:
        canonicalName                                                                || expectedResult
        ""                                                                           || []
        "canonicalName"                                                              || []
        "Option<Int>"                                                                || ['Int']
        "Option<Option<Int>>"                                                        || ['Option<Int>']
        "Map<String,Int>"                                                            || ['String', 'Int']
        "Map<String,Map<String,Double>>"                                             || ['String', 'Map<String,Double>']
        "Tuple3<Map<String,Int>,Option<Option<Int>>,Map<String,Map<String,Double>>>" || ['Map<String,Int>', 'Option<Option<Int>>', 'Map<String,Map<String,Double>>']
        "Option<Map<String,Int>>"                                                    || ['Map<String,Int>']
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme