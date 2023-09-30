package com.example.services.impl

import com.example.beans.Spiral
import com.example.warriers.FooFighter
import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest {
    @Mock
    FooFighter fooFighter
    @InjectMocks
    Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testNeverEnding() {
        String result = foo.neverEnding(new Spiral(moon: "moon", rings: 0), new Spiral.Circle("aCircleInASpiral"), new Spiral.Wheel.WheelWithinAWheel(new Spiral.Wheel("neverEnding", true), "everSpinningReel"))
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme