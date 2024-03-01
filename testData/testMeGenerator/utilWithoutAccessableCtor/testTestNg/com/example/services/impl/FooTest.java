package com.example.services.impl;

import com.example.foes.Fire;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {

    @Test
    public void testFight() {
        String result = Foo.fight(new Fire(), "foeName");
        Assert.assertEquals(result, "replaceMeWithExpectedResult");
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme