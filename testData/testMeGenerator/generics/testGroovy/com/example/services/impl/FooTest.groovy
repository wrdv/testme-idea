package com.example.services.impl

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
    Map<Pokemon,List<Fire>> hotPokeys
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
        Future result = foo.lookInto(CompletableFuture.completedFuture("String"), CompletableFuture.completedFuture("String"))
        assert result.get() == "String"
    }

    @Test
    void testWarm() {
        CompletableFuture<Integer> result = foo.warm(CompletableFuture.completedFuture(new Fire()), CompletableFuture.completedFuture(new Ice()))
        assert result.get() == 0
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme