package com.example.services.impl

import com.example.beans.ConvertedBean
import com.example.dependencies.Logger
import com.example.foes.Fear
import com.example.foes.Fire
import com.example.foes.Ice
import com.example.warriers.FooFighter
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
    FooFighter fooFighter
    @Mock
    Supplier<Integer> result
    @Mock
    Logger logger
    @InjectMocks
    Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testFight() {
        when(fooFighter.surrender(any(), any(), anyInt())).thenReturn(new ConvertedBean(myString: "myString", someNum: 0, fear: new Fear(), ice: new Ice()))
        when(result.get()).thenReturn(0)

        String result = foo.fight(new Fire(), "foeName")
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme