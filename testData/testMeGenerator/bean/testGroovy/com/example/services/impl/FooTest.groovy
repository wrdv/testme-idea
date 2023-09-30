package com.example.services.impl

import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest {
    @Mock
    com.example.warriers.FooFighter fooFighter
    @InjectMocks
    com.example.services.impl.Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testFight() {
        com.example.beans.JavaBean result = foo.fight(new com.example.foes.Fire(), new com.example.foes.FireBall(new com.example.beans.JavaBean(myString: "myString", myDate: new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new com.example.foes.Fear(), fire: new com.example.foes.Fire(), ice: new com.example.foes.Ice(), myOtherString: "myOtherString", someBinaryOption: true)), new com.example.beans.JavaBean(myString: "myString", myDate: new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new com.example.foes.Fear(), fire: new com.example.foes.Fire(), ice: new com.example.foes.Ice(), myOtherString: "myOtherString", someBinaryOption: true))
        assert result == new com.example.beans.JavaBean(myString: "myString", myDate: new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new com.example.foes.Fear(), fire: new com.example.foes.Fire(), ice: new com.example.foes.Ice(), myOtherString: "myOtherString", someBinaryOption: true)
    }

    @Test
    void testFight2() {
        com.example.dependencies.ChildWithSetters result = foo.fight(new com.example.dependencies.ChildWithSetters(strField: "strField", someNumber: 0, fire: new com.example.foes.FireBall(new com.example.beans.JavaBean(myString: "myString", myDate: new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new com.example.foes.Fear(), fire: new com.example.foes.Fire(), ice: new com.example.foes.Ice(), myOtherString: "myOtherString", someBinaryOption: true))))
        assert result == new com.example.dependencies.ChildWithSetters(strField: "strField", someNumber: 0, fire: new com.example.foes.FireBall(new com.example.beans.JavaBean(myString: "myString", myDate: new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new com.example.foes.Fear(), fire: new com.example.foes.Fire(), ice: new com.example.foes.Ice(), myOtherString: "myOtherString", someBinaryOption: true)))
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme