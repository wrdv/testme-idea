package com.example.services.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/** created by TestMe integration test on MMXVI */
public class FooTest {
    @Mock
    com.example.warriers.FooFighter fooFighter;
    @Mock
    com.example.foes.Pokemon pokey;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        when(fooFighter.fight(any(com.example.foes.Fire.class))).thenReturn("fightResponse");

        String result = foo.fight(new com.example.foes.Fire(), "foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testIDefault() throws Exception {
        foo.iDefault();
    }

    @Test
    public void testAsFather() throws Exception {
        String result = foo.asFather("asSon");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme