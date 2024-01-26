package com.example.services.impl;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    FinalCountdown finalCountdown;
    Foo foo = new Foo();

    @InjectMocks
    Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }
    @Test
    public void testCount() throws Exception {
        BigDecimal result = foo.count(Integer.valueOf(0));
        Assert.assertEquals(new BigDecimal(0), result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme