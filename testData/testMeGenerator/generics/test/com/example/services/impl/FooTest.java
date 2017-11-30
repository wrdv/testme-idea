package com.example.services.impl;

import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.foes.Pokemon;
import com.example.warriers.FooFighter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.mockito.Mockito.*;

/** created by TestMe integration test on MMXVI */
public class FooTest {
    @Mock
    Set<Ice> escimoRealEstate;
    @Mock
    Map<Pokemon,List<Fire>> hotPokeys;
    @Mock
    FooFighter fooFighter;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        String result = foo.fight(new ArrayList<Fire>(Arrays.asList(new Fire())), "foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testIntoTheVoid() throws Exception {
        foo.intoTheVoid();
    }

    @Test
    public void testLookInto() throws Exception {
        Future result = foo.lookInto(CompletableFuture.completedFuture("String"), CompletableFuture.completedFuture("String"));
        Assert.assertEquals("String", result.get());
    }

    @Test
    public void testWarm() throws Exception {
        CompletableFuture<Integer> result = foo.warm(CompletableFuture.completedFuture(new Fire()), CompletableFuture.completedFuture(new Ice()));
        Assert.assertEquals(Integer.valueOf(0), result.get());
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme