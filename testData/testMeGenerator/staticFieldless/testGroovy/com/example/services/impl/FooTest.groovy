package com.example.services.impl

import com.example.foes.Fire
import org.junit.Test

/** created by TestMe integration test on MMXVI */
class FooTest {

    @Test
    void testFight() {
        String result = Foo.fight(new Fire(), "foeName")
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme