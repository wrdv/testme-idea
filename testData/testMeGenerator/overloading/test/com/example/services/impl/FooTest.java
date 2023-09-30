package com.example.services.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    com.example.warriers.FooFighter fooFighter;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        String result = foo.fight(new com.example.foes.Fire(), "foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testFight2() throws Exception {
        String result = foo.fight(new com.example.foes.Fire(), new com.example.foes.Ice(), "foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testFight3() throws Exception {
        String result = foo.fight(new com.example.foes.Fire(), new com.example.foes.Ice(), new com.example.foes.Ice(), "foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testFight4() throws Exception {
        String result = foo.fight(0);
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testFold() throws Exception {
        String result = foo.fold("foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testFold2() throws Exception {
        String result = foo.fold("foeName", "truce");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme