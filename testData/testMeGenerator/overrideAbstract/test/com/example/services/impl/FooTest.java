package com.example.services.impl;

import com.example.foes.Fire;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    Foo foo = new Foo();

    @Test
    public void testGetMeMyTees() throws Exception {
        List<Fire> result = foo.getMeMyTees(new Fire());
        Assert.assertEquals(Arrays.<Fire>asList(new Fire()), result);
    }

    @Test
    public void testGetMeMyTee() throws Exception {
        Fire result = foo.getMeMyTee(new Fire());
        Assert.assertEquals(new Fire(), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme