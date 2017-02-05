package com.example.services.impl;

import com.example.foes.Fire;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/** created by TestMe integration test on MMXVI */
class FooTest {
    //Field fooFighter of type FooFighter[] - was not mocked since Mockito doesn't mock arrays
    @InjectMocks
    Foo foo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFight(){
        String[] result = foo.fight(new Fire[]{new Fire()}, new String[]{"foeName"}, new int[]{0});
        Assertions.assertArrayEquals(new String[]{"replaceMeWithExpectedResult"}, result);
    }

    @Test
    void testFireStarter(){
        Fire[] result = foo.fireStarter(new String[]{"foeName"}, new int[]{0});
        Assertions.assertArrayEquals(new Fire[]{new Fire()}, result);
    }

    @Test
    void testFireCounter(){
        int[] result = foo.fireCounter(new Fire[]{new Fire()});
        Assertions.assertArrayEquals(new int[]{0}, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme