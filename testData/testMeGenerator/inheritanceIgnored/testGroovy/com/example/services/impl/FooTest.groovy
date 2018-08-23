package com.example.services.impl

import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest {
    @Mock
    com.example.warriers.FooFighter fooFighter
    @Mock
    com.example.foes.Pokemon pokey
    @InjectMocks
    com.example.services.impl.Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testFight() {
        when(fooFighter.fight(any())).thenReturn("fightResponse")

        java.lang.String result = foo.fight(new com.example.foes.Fire(), "foeName")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testIDefault() {
        foo.iDefault()
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme