package com.example.services.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    private com.example.warriers.FooFighter fooFighter;
    @Mock
    private com.example.foes.Pokemon pokey;
    @InjectMocks
    private Foo foo;

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
    public void testIDefault() throws Exception {
        foo.iDefault();
    }

    @Test
    public void testAsFather() throws Exception {
        String result = foo.asFather("asSon");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://github.com/yaronyam/testme-intellij/issues