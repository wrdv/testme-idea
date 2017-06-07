package com.example.services.impl

import com.example.foes.Fire
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
    void testFight() {
        Double result = foo.fight(new Fire(), "foeName", (byte)0, (short)0, 0, 0l, 0f, 0d, (char)'a', true, "00110" as Byte, (short)0, 0, 1l, 1.1f, 0d, 'a' as Character, Boolean.TRUE, 0 as BigDecimal)
        assert result == 0d
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme