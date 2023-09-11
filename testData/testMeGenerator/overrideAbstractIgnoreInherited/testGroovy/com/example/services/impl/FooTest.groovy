package com.example.services.impl

import com.example.foes.Fire
import org.junit.Test

/** created by TestMe integration test on MMXVI */
class FooTest {
    Foo foo = new Foo()

    @Test
    void testGetMeMyTee() {
        Fire result = foo.getMeMyTee(new Fire())
        assert result == new Fire()
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme