package com.weirddev.testme.intellij.template.context

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author : Yaron Yamin
 * @since : 7/11/20
 * */

class LanguageTest extends Specification {

    @Unroll
    def "safe Value Of where language=#language then expect: #expectedResult"() {
        expect:
        Language.safeValueOf(language) == expectedResult

        where:
        language  || expectedResult
        "Scala"   || Language.Scala
        "scala"   || Language.Scala
        "unknown" || Language.Java
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme