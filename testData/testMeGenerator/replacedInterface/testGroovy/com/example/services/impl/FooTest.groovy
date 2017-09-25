package com.example.services.impl

import com.example.beans.JavaBean
import com.example.dependencies.ChildWithSetters
import com.example.dependencies.MasterInterface
import com.example.foes.BeanDependsOnInterface
import com.example.foes.Fear
import com.example.foes.Fire
import com.example.foes.FireBall
import com.example.foes.Ice
import org.junit.Test

/** created by TestMe integration test on MMXVI */
class FooTest {
    Foo foo = new Foo()

    @Test
    void testJack() {
        MasterInterface result = foo.jack(new BeanDependsOnInterface(new ChildWithSetters(strField: "strField", someNumber: 0, fire: new FireBall(new JavaBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: null, fire: null, ice: null, myOtherString: "myOtherString", someBinaryOption: true)))), new ChildWithSetters(strField: "strField", someNumber: 0, fire: new FireBall(new JavaBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new Fear(), fire: new Fire(), ice: new Ice(), myOtherString: "myOtherString", someBinaryOption: true))))
        assert result == new ChildWithSetters(strField: "strField", someNumber: 0, fire: new FireBall(new JavaBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new Fear(), fire: new Fire(), ice: new Ice(), myOtherString: "myOtherString", someBinaryOption: true)))
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme