package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.weapons.Fire;
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
    private FooFighter fooFighter;
    @InjectMocks
    private Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        String result = foo.fight(new Fire(), "someFoe");
        Assert.assertEquals("expectedResult", result);
    }
}

/**
 * Generated with love by TestMe :) For reporting issues and submitting feature requests: https://github.com/yaronyam/testme-intellij/issues
 */