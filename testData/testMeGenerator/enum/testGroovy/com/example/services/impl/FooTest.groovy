package com.example.services.impl

import com.example.foes.Fire
import com.example.services.FooType
import com.example.services.Result
import com.example.warriers.FooFighter
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest {
    @Mock
    FooFighter fooFighter
    //Field result of type Result - was not mocked since Mockito doesn't mock enums
    @InjectMocks
    Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testFight() {
        Result result = foo.fight(new Fire(), new FooType())
        assert result == Result.WinWin
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme