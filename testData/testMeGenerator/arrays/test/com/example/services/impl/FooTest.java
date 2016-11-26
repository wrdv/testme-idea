package com.example.services.impl;

import com.example.foes.Fire;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** created by TestMe integration test on MMXVI */
public class FooTest {
    //Field fooFighter of type FooFighter[] - was not mocked since Mockito doesn't mock arrays
    @InjectMocks
    private Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        String[] result = foo.fight(new Fire[]{new Fire()}, new String[]{"foeName"}, new int[]{0});
        Assert.assertArrayEquals(new String[]{"replaceMeWithExpectedResult"}, result);
    }

    @Test
    public void testFireStarter() throws Exception {
        Fire[] result = foo.fireStarter(new String[]{"foeName"}, new int[]{0});
        Assert.assertArrayEquals(new Fire[]{new Fire()}, result);
    }

    @Test
    public void testFireCounter() throws Exception {
        int[] result = foo.fireCounter(new Fire[]{new Fire()});
        Assert.assertArrayEquals(new int[]{0}, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://github.com/yaronyam/testme-intellij/issues