package com.example.services.impl;

import com.example.foes.Fire;
import com.example.warriers.FooFighter;
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
    FooFighter fooFighter;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        Fire result = foo.fight(new Fire(), new com.example.hole.Fire());
        Assert.assertEquals(new Fire(), result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme