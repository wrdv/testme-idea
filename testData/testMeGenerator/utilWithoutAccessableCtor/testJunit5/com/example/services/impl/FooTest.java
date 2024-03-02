package com.example.services.impl;

import com.example.foes.Fire;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * created by TestMe integration test on MMXVI
 */
class FooTest {

    @Test
    void testFight() {
        String result = Foo.fight(new Fire(), "foeName");
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme