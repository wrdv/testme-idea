package com.example.services.impl;

import com.example.beans.JavaBean;
import com.example.foes.Ice;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
public class FoovyTest {
    @Mock
    Ice ice;
    @Mock
    Collection<JavaBean> myBeans;
    @InjectMocks
    Foovy foovy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSmashing() throws Exception {
        String result = foovy.smashing("somePumpkins", 'a', new Ice());
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme