package com.example.services.impl

import com.example.beans.ConvertedBean
import com.example.foes.Fire
import com.example.warriers.TechFighter
import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.util.function.Supplier
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest {
    @Mock
    TechFighter techFighter
    @Mock
    Supplier<Integer> result
    @InjectMocks
    Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testFight() {
        when(techFighter.fight(any(Fire.class))).thenReturn("fightResponse")
        when(techFighter.surrender(any(Fear.class), any(Ice.class), anyInt())).thenReturn(new ConvertedBean(myString: "myString", someNum: 0))
        when(result.get()).thenReturn(0)

        String result = foo.fight(new Fire(), "foeName")
        verify(techFighter).initSelfArming(anyString())
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme