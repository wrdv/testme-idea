package com.example.services.impl;

import com.example.beans.ConvertedBean;
import com.example.dependencies.Logger;
import com.example.foes.Fire;
import com.example.warriers.FooFighter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
class FooTest {
    @Mock
    FooFighter fooFighter;
    @Mock
    Logger logger;
    @InjectMocks
    Foo foo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFight() {
        when(fooFighter.surrender(any(), any(), anyInt())).thenReturn(new ConvertedBean());

        String result = foo.fight(new Fire(), "foeName");
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme