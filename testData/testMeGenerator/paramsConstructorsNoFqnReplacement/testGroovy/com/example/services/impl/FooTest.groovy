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
        com.example.beans.BigBean result = foo.fight(new com.example.foes.Fire(), ["foeName"], new com.example.beans.BigBean(new com.example.services.impl.DoneThat(0, "aDay", new com.example.services.impl.Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"), new com.example.services.impl.Many("family", "members", "only"), new com.example.services.impl.DoneThat(0, "aDay", new com.example.services.impl.Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood")), new com.example.beans.BeanThere(new com.example.services.impl.DoneThat(0, "aDay", new com.example.services.impl.Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(null, new com.example.services.impl.Many("family", "members", "only"), null), "ifYourInDaHood"), new com.example.services.impl.Many("family", "members", "only")))
        assert result == new com.example.beans.BigBean(new com.example.services.impl.DoneThat(0, "aDay", new com.example.services.impl.Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"), new com.example.services.impl.Many("family", "members", "only"), new com.example.services.impl.DoneThat(0, "aDay", new com.example.services.impl.Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"))
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme