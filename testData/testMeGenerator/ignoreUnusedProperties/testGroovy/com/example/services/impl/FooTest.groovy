package com.example.services.impl

import com.example.beans.ConvertedBean
import com.example.beans.JavaBean
import com.example.dependencies.ChildWithSetters
import com.example.foes.Fear
import com.example.foes.Fire
import com.example.foes.FireBall
import com.example.foes.Ice
import com.example.warriers.FooFighter
import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest {
    @Mock
    FooFighter fooFighter
    @InjectMocks
    Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testConvert() {
        ConvertedBean result = foo.convert(new Fire(), "foeName", new JavaBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, fear: new Fear(), fire: new Fire(), ice: new Ice(), someBinaryOption: true))
        assert result == new ConvertedBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new Fear(), ice: new Ice(), someBinaryOption: true)
    }

    @Test
    void testCallOthers() {
        ChildWithSetters result = foo.callOthers(new ChildWithSetters(strField: "strField", someNumber: 0, fire: new FireBall(new JavaBean(someNum: 0, someLongerNum: 1l, ice: new Ice(), someBinaryOption: true))))
        assert result == new ChildWithSetters(strField: "strField", someNumber: 0, fire: new FireBall(new JavaBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, someLongerNum: 1l, fear: new Fear(), fire: new Fire(), ice: new Ice(), someBinaryOption: true)))
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme