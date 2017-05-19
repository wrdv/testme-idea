package com.weirddev.testme.intellij.template.context

import spock.lang.Specification

/**
 * Date: 19/05/2017
 * @author Yaron Yamin
 */
class GroovyTestBuilderImplTest extends Specification {

    def "test should Prefer Setters Over Ctor"() {
        given:
        GroovyTestBuilderImpl groovyTestBuilderImpl = new GroovyTestBuilderImpl(4, null, true, true, 50)

        expect:
        result == groovyTestBuilderImpl.shouldPreferSettersOverCtor(noOfCtorArgs, noOfSetters)

        where:
        result || noOfCtorArgs | noOfSetters
        false  || 0            | 0
        false  || 1            | 0
        false  || 2            | 2
        true   || 2            | 3
        false  || 4            | 3
        false  || 4            | 5
        true   || 4            | 6
        false  || 5            | 6
        false  || 5            | 6
        false  || 5            | 7
        true   || 5            | 8
        false  || 10           | 14
        true   || 10           | 15
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme