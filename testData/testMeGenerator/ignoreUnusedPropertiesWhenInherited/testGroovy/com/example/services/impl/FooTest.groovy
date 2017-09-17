package com.example.services.impl

import com.example.beans.ConvertedBean
import com.example.beans.InheritingBean
import com.example.beans.JavaBean
import com.example.foes.Fear
import com.example.foes.Fire
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
        ConvertedBean result = foo.convert(new Fire(), "foeName", new InheritingBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, fear: new Fear(), fire: new Fire(), ice: new Ice(), someBinaryOption: true))
        assert result == new ConvertedBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, fear: new Fear(), ice: new Ice(), someBinaryOption: true)
    }

    @Test
    void testConvert2() {
        ConvertedBean result = foo.convert(new Fire(), "foeName", new JavaBean(fear: new Fear()))
        assert result == new ConvertedBean(fear: new Fear())
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme