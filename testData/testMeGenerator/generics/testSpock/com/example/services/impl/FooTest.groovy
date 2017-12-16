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
import java.util.concurrent.Future

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

    def "test fight"() {
        when:
        String result = foo.fight([new Fire()], "foeName")

        then:
        result == "replaceMeWithExpectedResult"
    }

    def "test into The Void"() {
        when:
        foo.intoTheVoid()

        then:
        false//todo - validate something
    }

    def "test look Into"() {
        when:
        Future result = foo.lookInto(CompletableFuture.completedFuture("String"), CompletableFuture.completedFuture("String"))

        then:
        result.get() == "String"
    }

    def "test warm"() {
        when:
        CompletableFuture<Integer> result = foo.warm(CompletableFuture.completedFuture(new Fire()), CompletableFuture.completedFuture(new Ice()))

        then:
        result.get() == 0
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme