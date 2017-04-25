package com.example.services.impl;

import com.example.foes.Fire;
import org.junit.Assert;
import org.junit.Test;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {

    @Test
    public void testFight() throws Exception {
        String result = Foo.fight(new Fire(), "foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme