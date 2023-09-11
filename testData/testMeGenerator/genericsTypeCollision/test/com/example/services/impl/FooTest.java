package com.example.services.impl;

import com.example.hole.Fire;
import com.example.util.List;
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
        List<Fire> result = foo.fight(java.util.List.of(new com.example.foes.Fire()));
        Assert.assertEquals(new List<Fire>(), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme