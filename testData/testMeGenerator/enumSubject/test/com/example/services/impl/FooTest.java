package com.example.services.impl;

import org.junit.Assert;
import org.junit.Test;

/** created by TestMe integration test on MMXVI */
public class FooTest {
    
    
    Foo foo = Foo.One;

    @Test
    public void testGetValue() throws Exception {
        String result = foo.getValue();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme