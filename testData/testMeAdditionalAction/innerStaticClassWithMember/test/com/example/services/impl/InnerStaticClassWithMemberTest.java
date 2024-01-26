package com.example.services.impl;

import com.example.foes.Fire;
import com.example.warriers.FooFighter;
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
public class InnerStaticClassWithMemberTest {
    @Mock
    FooFighter innerFooFighter;
    @InjectMocks
    Foo.InnerStaticClassWithMember innerStaticClassWithMember;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMethodOfInnerClass() throws Exception {
        when(innerFooFighter.fight(any())).thenReturn("fightResponse");

        String result = innerStaticClassWithMember.methodOfInnerClass(new Fire());
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme