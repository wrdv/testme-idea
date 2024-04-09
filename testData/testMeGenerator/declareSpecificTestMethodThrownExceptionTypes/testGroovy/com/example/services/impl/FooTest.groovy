package com.example.services.impl

import com.example.foes.Fire
import org.junit.Test

/** created by TestMe integration test on MMXVI */
class FooTest {
    Foo foo = new Foo()

    @Test
    void testHasException() {
        Fire result = foo.hasException(new Fire())
        assert result == new Fire()
    }

    @Test
    void testHasNoException() {
        Fire result = foo.hasNoException(new Fire())
        assert result == new Fire()
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme