package com.example.services.impl;

import com.example.beans.BeanByCtor;
import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.groovies.ImGroovy;
import com.example.warriers.FooFighter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    FooFighter fooFighter;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindWithDelegatedCalls() throws Exception {
        DelegateCtor result = foo.findWithDelegatedCalls(List.of(new BeanByCtor("myName", new Ice(), null, 0d)), new ImGroovy());
        Assert.assertEquals(new DelegateCtor("youre", "asCold", null, new Fire()), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme