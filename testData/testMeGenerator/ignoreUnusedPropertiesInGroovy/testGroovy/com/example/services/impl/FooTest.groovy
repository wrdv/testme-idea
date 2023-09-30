package com.example.services.impl

import com.example.beans.ConvertedBean
import com.example.beans.JavaBean
import com.example.foes.Fear
import com.example.foes.Fire
import com.example.foes.Ice
import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest {
    @Mock
    Ice ice
    @Mock
    Collection<JavaBean> myBeans
    @InjectMocks
    Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testSmashing() {
        String result = foo.smashing(new FooBro(propInSamePackage: "propInSamePackage", antoherDirectlyAccessedInt: 0), new JavaBean(fear: new Fear(), fire: new Fire(), ice: new Ice()))
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testConvertWhileSettingPropsInline() {
        ConvertedBean result = foo.convertWhileSettingPropsInline(new FooBro(anotherProp: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime()), new JavaBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, fire: new Fire(), ice: new Ice(), someBinaryOption: true))
        assert result == new ConvertedBean(myString: "myString", myDate: new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), someNum: 0, ice: new Ice(), someBinaryOption: true)
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme