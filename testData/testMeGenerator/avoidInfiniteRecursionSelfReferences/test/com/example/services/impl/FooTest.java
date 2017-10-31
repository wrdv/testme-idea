package com.example.services.impl;

import com.example.SelfReferringType;
import com.example.dependencies.SelfishService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    SelfishService selfishService;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoSelf() throws Exception {
        when(selfishService.getSelfishType()).thenReturn(SelfReferringType.ONE);
        String result = foo.doSelf();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testGetSelf() throws Exception {
        when(selfishService.getSelfishType()).thenReturn(SelfReferringType.ONE);
        String result = foo.doSelf();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme