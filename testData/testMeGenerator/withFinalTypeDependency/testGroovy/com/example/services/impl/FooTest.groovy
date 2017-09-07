package com.example.services.impl

import org.junit.Test

/** created by TestMe integration test on MMXVI */
class FooTest {
    //Field finalCountdown of type FinalCountdown - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    Foo foo = new Foo()

    @Test
    void testCount() {
        BigDecimal result = foo.count(0)
        assert result == 0 as BigDecimal
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme