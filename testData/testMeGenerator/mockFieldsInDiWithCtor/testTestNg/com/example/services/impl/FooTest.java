package com.example.services.impl;

import com.example.beans.ConvertedBean;
import com.example.foes.Fear;
import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.warriers.TechFighter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() {
        when(techFighter.fight(any(Fire.class))).thenReturn("fightResponse");
        when(techFighter.surrender(any(Fear.class), any(Ice.class), anyInt())).thenReturn(new ConvertedBean());
        when(result.get()).thenReturn(Integer.valueOf(0));

        String result = foo.fight(new Fire(), "foeName");
        verify(techFighter).initSelfArming(anyString());
        Assert.assertEquals(result, "replaceMeWithExpectedResult");
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme