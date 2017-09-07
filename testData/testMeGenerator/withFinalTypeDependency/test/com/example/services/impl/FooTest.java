package com.example.services.impl;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    //Field finalCountdown of type FinalCountdown - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    Foo foo = new Foo();

    @Test
    public void testCount() throws Exception {
        BigDecimal result = foo.count(Integer.valueOf(0));
        Assert.assertEquals(new BigDecimal(0), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme