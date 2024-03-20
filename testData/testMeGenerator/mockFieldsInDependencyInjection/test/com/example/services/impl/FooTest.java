package com.example.services.impl;

import com.example.beans.ConvertedBean;
import com.example.foes.Fire;
import com.example.warriers.TechFighter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Supplier;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    TechFighter techFighter;
    @Mock
    Supplier<Integer> result;

    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        when(techFighter.fight(any())).thenReturn("fightResponse");
        when(techFighter.surrender(any(), any(), anyInt())).thenReturn(new ConvertedBean());
        when(result.get()).thenReturn(Integer.valueOf(0));

        String result = foo.fight(new Fire(), "foeName");
        verify(techFighter).initSelfArming(anyString());
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme