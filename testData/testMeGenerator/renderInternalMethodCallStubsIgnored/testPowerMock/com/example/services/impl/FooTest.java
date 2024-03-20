package com.example.services.impl;

import com.example.beans.ConvertedBean;
import com.example.foes.Fire;
import com.example.warriers.TechFighter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Foo.class})
@PowerMockIgnore("javax.management.*")
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
        when(techFighter.fight(any(Fire.class))).thenReturn("fightResponse");
        when(techFighter.surrender(any(Fear.class), any(Ice.class), anyInt())).thenReturn(new ConvertedBean());
        when(result.get()).thenReturn(Integer.valueOf(0));
        String result = foo.fight(new Fire(), "foeName");
        verify(techFighter).initSelfArming(anyString());
        Assert.assertEquals("replaceMeWithExpectedResult", result);

    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme