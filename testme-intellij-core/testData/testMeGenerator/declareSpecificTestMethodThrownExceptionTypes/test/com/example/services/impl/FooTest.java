package com.example.services.impl;

import com.example.foes.Fire;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    Foo foo = new Foo();

    @Test
    public void testHasException() throws IOException {
        Fire result = foo.hasException(new Fire());
        Assert.assertEquals(new Fire(), result);
    }

    @Test
    public void testHasNoException() {
        Fire result = foo.hasNoException(new Fire());
        Assert.assertEquals(new Fire(), result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme