package com.example.services.impl

import com.example.beans.JavaBean
import com.example.foes.Fear
import com.example.foes.Fire
import com.example.foes.Ice
import org.junit.Test

/** created by TestMe integration test on MMXVI */
class FooTest {
    //Field fooFighter of type FooFighter[] - was not mocked since Mockito doesn't mock arrays
    Foo foo = new Foo("thereGoes", "myHero", new JavaBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new Fear(), fire: new Fire(), ice: new Ice(), myOtherString: "myOtherString", someBinaryOption: true))

    @Test
    void testFight() {
        String result = foo.fight([new Fire()] as Fire[], ["foeName"] as String[], [0] as int[])
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme