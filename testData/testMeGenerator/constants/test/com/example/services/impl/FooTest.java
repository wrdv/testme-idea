package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
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
    //TODO Field MUTABLE of type String - was not mocked since Mockito doesn't mock a Final class
    //TODO Field MUTABLE_INITIALIZED_IN_CTOR of type String - was not mocked since Mockito doesn't mock a Final class
    //TODO Field MUTABLE_INITIALIZED_IN_DEFAULT_CTOR of type String - was not mocked since Mockito doesn't mock a Final class
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
        String result = foo.fight(new Fire(), "foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://github.com/yaronyam/testme-intellij/issues