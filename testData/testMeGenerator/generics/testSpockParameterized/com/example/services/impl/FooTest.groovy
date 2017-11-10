package com.example.services.impl

import com.example.foes.Fire
import com.example.foes.Ice
import com.example.foes.Pokemon
import com.example.warriers.FooFighter
import spock.lang.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest extends Specification {
    @Mock
    Set<Ice> escimoRealEstate
    @Mock
    Map<Pokemon, List<Fire>> hotPokeys
    @Mock
    FooFighter fooFighter
    @InjectMocks
    Foo foo

    def setup() {
        MockitoAnnotations.initMocks(this)
    }
    @Unroll
    def "test fight where withFire=#withFire and foeName=#foeName then expect: #result"() {
        given:
        when(fooFighter.fight(any())).thenReturn("fightResponse")

        expect:
        foo.fight(withFire, foeName) == result

        where:
        withFire     | foeName   || result
        [new Fire()] | "foeName" || "replaceMeWithExpectedResult"
        [null]       | null      || null
    }

    def "test into The Void"() {
        when:
        foo.intoTheVoid()

        then:
        false//todo - validate something
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme