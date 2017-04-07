package com.example.services.impl

import com.example.foes.Fire
import org.junit.Test

/** created by TestMe integration test on MMXVI */
class FooTest {
    //Field fooFighter of type FooFighter[] - was not mocked since Mockito doesn't mock arrays
    Foo foo= new Foo()
    @Test
    void testFight() {
        String result = foo.fight([new Fire()] as Fire[], ["foeName"] as String[], [0] as int[])
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme