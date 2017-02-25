package com.example.services.impl

import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest {
    @Mock
    com.example.warriers.FooFighter fooFighter
    @InjectMocks
    com.example.services.impl.Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testFight() {
        java.lang.String result = foo.fight(new com.example.foes.Fire(), "foeName")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testFight2() {
        java.lang.String result = foo.fight(new com.example.foes.Fire(), new com.example.foes.Ice(), "foeName")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testFight3() {
        java.lang.String result = foo.fight(new com.example.foes.Fire(), new com.example.foes.Ice(), new com.example.foes.Ice(), "foeName")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testFight4() {
        java.lang.String result = foo.fight(0)
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testFold() {
        java.lang.String result = foo.fold("foeName")
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testFold2() {
        java.lang.String result = foo.fold("foeName", "truce")
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme