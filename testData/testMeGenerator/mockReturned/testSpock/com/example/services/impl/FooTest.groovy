package com.example.services.impl

import com.example.beans.ConvertedBean
import com.example.dependencies.Logger
import com.example.foes.Fear
import com.example.foes.Fire
import com.example.foes.Ice
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
    @Mock
    Logger logger
    @InjectMocks
    Foo foo

    def setup() {
        MockitoAnnotations.initMocks(this)
    }

    def "test fight"() {
        given:
        when(fooFighter.surrender(any(), any(), anyInt())).thenReturn(new ConvertedBean(myString: "myString", someNum: 0, fear: new Fear(), ice: new Ice()))

        when:
        String result = foo.fight(new Fire(), "foeName")

        then:
        result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme