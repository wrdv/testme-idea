package com.example.services.impl

import com.example.foes.Fire
import com.example.foes.Ice
import org.junit.Test

/** created by TestMe integration test on MMXVI */
class FooTest {
    Foo foo = new Foo()

    @Test
    void testGetMeMyTee() {
        Fire result = foo.getMeMyTee(new Fire())
        assert result == new Fire()
    }

    @Test
    void testU2() {
        Map<Ice, Fire> result = foo.u2()
        assert result == [(new Ice()): new Fire()]
    }

    @Test
    void testINeedU() {
        foo.iNeedU(new Ice())
    }

    @Test
    void testINeedUT() {
        String result = foo.iNeedUT(new Ice(), new Fire())
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testGetMeMyTees() {
        List<Fire> result = foo.getMeMyTees(new Fire())
        assert result == [new Fire()]
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme