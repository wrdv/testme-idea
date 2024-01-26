package com.example.services.impl

import com.example.beans.Result
import com.example.beans.ResultPage
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
        Future result = foo.lookInto(CompletableFuture.completedFuture("backTo"), CompletableFuture.completedFuture("theFuture"))

        then:
        result.get() == "replaceMeWithExpectedResult"
    }

    def "test warm"() {
        when:
        CompletableFuture<Integer> result = foo.warm(CompletableFuture.completedFuture(new Fire()), CompletableFuture.completedFuture(new Ice()))

        then:
        result.get() == 0
    }

    def "test find"() {
        when:
        ResultPage<Pokemon> result = foo.find()

        then:
        result == new ResultPage<Pokemon>(0, [new Pokemon()])
    }

    def "test resolve Result"() {
        when:
        Result<Pokemon> result = foo.resolveResult()

        then:
        result == new Result<Pokemon>(new Pokemon())
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme