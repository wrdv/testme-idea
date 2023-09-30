package com.example.services.impl;

import com.example.beans.JavaBean;
import com.example.foes.Fire;
import org.junit.Assert;
import org.junit.Test;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    //Field fooFighter of type FooFighter[] - was not mocked since Mockito doesn't mock arrays
    Foo foo = new Foo("thereGoes", "myHero", new JavaBean());

    @Test
    public void testFight() throws Exception {
        String result = foo.fight(new Fire[]{new Fire()}, new String[]{"foeName"}, new int[]{0});
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme