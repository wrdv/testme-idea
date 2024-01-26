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

    @Test
    void testRouterSearch() {
        String result = foo.routerSearch("dstIp", [["ipTable"]] as String[][])
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testA3dimSearch() {
        String result = foo.a3dimSearch("dstIp", [[["ipTable"]]] as String[][][])
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme