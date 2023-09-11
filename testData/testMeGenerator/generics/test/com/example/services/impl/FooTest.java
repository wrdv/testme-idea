package com.example.services.impl;

import com.example.beans.Result;
import com.example.beans.ResultPage;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.mockito.Mockito.*;

/** created by TestMe integration test on MMXVI */
public class FooTest {
    @Mock
    Set<Ice> escimoRealEstate;
    @Mock
    Map<Pokemon, List<Fire>> hotPokeys;
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
        String result = foo.fight(new ArrayList<>(List.of(new Fire())), "foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testIntoTheVoid() throws Exception {
        foo.intoTheVoid();
    }

    @Test
    public void testLookInto() throws Exception {
        Future result = foo.lookInto(CompletableFuture.completedFuture("backTo"), CompletableFuture.completedFuture("theFuture"));
        Assert.assertEquals("replaceMeWithExpectedResult", result.get());
    }

    @Test
    public void testWarm() throws Exception {
        CompletableFuture<Integer> result = foo.warm(CompletableFuture.completedFuture(new Fire()), CompletableFuture.completedFuture(new Ice()));
        Assert.assertEquals(Integer.valueOf(0), result.get());
    }

    @Test
    public void testFind() throws Exception {
        ResultPage<Pokemon> result = foo.find();
        Assert.assertEquals(new ResultPage<Pokemon>(0, List.of(new Pokemon())), result);
    }

    @Test
    public void testResolveResult() throws Exception {
        Result<Pokemon> result = foo.resolveResult();
        Assert.assertEquals(new Result<Pokemon>(new Pokemon()), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme