package com.example.services.impl

import org.junit.Test

/** created by TestMe integration test on MMXVI */
class FooTest {
    
    
    Foo foo = Foo.One

    @Test
    void testGetValue() {
        String result = foo.getValue()
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme