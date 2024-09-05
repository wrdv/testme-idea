package com.example.services.impl

import com.example.beans.Result
import com.example.beans.ResultPage
import com.example.foes.Fire
import com.example.foes.Ice
import com.example.foes.Pokemon
import com.example.warriers.FooFighter
import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest {
    @Mock
    Set<Ice> escimoRealEstate
    @Mock
    Map<Pokemon, List<Fire>> hotPokeys
    @Mock
    FooFighter fooFighter
    @InjectMocks
    Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testFight() {
        String result = foo.fight([new Fire()], "foeName")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testIntoTheVoid() {
        foo.intoTheVoid()
    }

    @Test
    void testLookInto() {
        Future result = foo.lookInto(CompletableFuture.completedFuture("backTo"), CompletableFuture.completedFuture("theFuture"))
        assert result.get() == "replaceMeWithExpectedResult"
    }

    @Test
    void testWarm() {
        CompletableFuture<Integer> result = foo.warm(CompletableFuture.completedFuture(new Fire()), CompletableFuture.completedFuture(new Ice()))
        assert result.get() == 0
    }

    @Test
    void testFind() {
        ResultPage<Pokemon> result = foo.find()
        assert result == new ResultPage<Pokemon>(0, [new Pokemon()])
    }

    @Test
    void testResolveResult() {
        Result<Pokemon> result = foo.resolveResult()
        assert result == new Result<Pokemon>(new Pokemon())
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme