package com.example.services.impl

import com.example.foes.Fire
import org.junit.Test

/** created by TestMe integration test on MMXVI */
class FooTest {
    //Field fooFighter of type FooFighter[] - was not mocked since Mockito doesn't mock arrays
    Foo foo= new Foo()

    @Test
    void testFight() {
        String[] result = foo.fight([new Fire()] as Fire[], ["foeName"] as String[], [0] as int[])
        assert result == ["replaceMeWithExpectedResult"] as String[]
    }

    @Test
    void testFireStarter() {
        Fire[] result = foo.fireStarter(["foeName"] as String[], [0] as int[])
        assert result == [new Fire()] as Fire[]
    }

    @Test
    void testFireCounter() {
        int[] result = foo.fireCounter([new Fire()] as Fire[])
        assert result == [0] as int[]
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme