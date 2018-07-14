package com.example.services.impl

import com.example.foes.Fire
import com.example.foes.Ice
import com.example.foes.Pokemon
import com.example.warriers.FooFighter
import spock.lang.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.util.concurrent.CompletableFuture
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
    def "fight where withFire=#withFire and foeName=#foeName then expect: #expectedResult"() {
        expect:
        foo.fight(withFire, foeName) == expectedResult

        where:
        withFire     | foeName   || expectedResult
        [new Fire()] | "foeName" || "expectedResult"
    }

    @Unroll
    def "into The Void"() {
        expect:
        foo.intoTheVoid()
        assert expectedResult == false //todo - validate something

        where:
        expectedResult << true
    }

    @Unroll
    def "look Into where backTo=#backTo and theFuture=#theFuture then expect: #expectedResult"() {
        expect:
        foo.lookInto(backTo, theFuture).get() == expectedResult

        where:
        backTo                                      | theFuture                                   || expectedResult
        CompletableFuture.completedFuture("String") | CompletableFuture.completedFuture("String") || "String"
    }

    @Unroll
    def "warm where up=#up and coolDown=#coolDown then expect: #expectedResult"() {
        expect:
        foo.warm(up, coolDown).get() == expectedResult

        where:
        up                                            | coolDown                                     || expectedResult
        CompletableFuture.completedFuture(new Fire()) | CompletableFuture.completedFuture(new Ice()) || 0
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme