package com.weirddev.testme.intellij.template.context

import spock.lang.Specification

/**
 * Date: 19/05/2017
 * @author Yaron Yamin
 */
class GroovyTestBuilderImplTest extends Specification {

    def "test should Prefer Setters Over Ctor"() {
        when:
        GroovyTestBuilderImpl groovyTestBuilderImpl = new GroovyTestBuilderImpl(4, null, true, true, 50)

        expect:
        boolean result = groovyTestBuilderImpl.shouldPreferSettersOverCtor(noOfCtorArgs, noOfSetters)

        where:
        noOfCtorArgs | noOfSetters || result
        0            | 0           || false
        1            | 0           || false
        2            | 2           || false
        2            | 3           || true
        4            | 3           || false
        4            | 5           || false
        4            | 6           || true
        5            | 6           || false
        5            | 6           || false
        5            | 7           || true
        10           | 14          || false
        10           | 15          || true
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme