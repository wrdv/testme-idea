package com.example.services.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** created by TestMe integration test on MMXVI */
public class FooTest {
    @Mock
    java.util.List<com.example.warriers.FooFighter> fooFighter;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        java.util.List<com.example.foes.Fire> result = foo.fight(new java.util.ArrayList<com.example.foes.Fire>(), new java.util.HashSet<com.example.foes.Fire>(), new java.util.HashMap<String,com.example.foes.Ice>(), new java.util.ArrayList<String>(), new java.util.ArrayList<java.util.List<String>>(), new java.util.LinkedList<java.util.List<com.example.foes.Fear>>());
        Assert.assertEquals(new java.util.ArrayList<com.example.foes.Fire>(), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://github.com/yaronyam/testme-intellij/issues