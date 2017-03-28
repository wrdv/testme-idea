package com.example.services.impl

import com.example.beans.JavaBean
import com.example.foes.Fear
import com.example.foes.Fire
import com.example.foes.Ice
import com.example.groovies.ImGroovy
import com.example.groovies.ImGroovyWithTupleCtor
import com.example.warriers.FooFighter
import spock.lang.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {
    @Mock
    FooFighter fooFighter
    @InjectMocks
    Foo foo

    def setup() {
        MockitoAnnotations.initMocks(this)
    }

    def "test find"() {
        when:
        Collection<ImGroovyWithTupleCtor> result = foo.find([new ImGroovyWithTupleCtor("myName", new Ice(), [new JavaBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new Fear(), fire: new Fire(), ice: new Ice(), someBinaryOption: true)])], new ImGroovy(myName: "myName", ice: new Ice(), myBeans: [new JavaBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new Fear(), fire: new Fire(), ice: new Ice(), someBinaryOption: true)]))

        then:
        result == [new ImGroovyWithTupleCtor("myName", new Ice(), [new JavaBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new Fear(), fire: new Fire(), ice: new Ice(), someBinaryOption: true)])]
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme