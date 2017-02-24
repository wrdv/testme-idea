package com.example.services.impl

import org.junit.Before
import org.junit.Test
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
        com.example.beans.BigBean result = foo.fight(new com.example.foes.Fire(), ["String"], new com.example.beans.BigBean(new com.example.services.impl.DoneThat(0, "aDay", new com.example.services.impl.Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(new com.example.services.impl.DoneThat(), new com.example.services.impl.Many(), new com.example.services.impl.DoneThat()), "ifYourInDaHood"), new com.example.services.impl.Many("family", "members", "only"), new com.example.services.impl.DoneThat(0, "aDay", new com.example.services.impl.Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(new com.example.services.impl.DoneThat(), new com.example.services.impl.Many(), new com.example.services.impl.DoneThat()), "ifYourInDaHood")), new com.example.beans.BeanThere(new com.example.services.impl.DoneThat(0, "aDay", new com.example.services.impl.Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(new com.example.services.impl.DoneThat(), new com.example.services.impl.Many(), new com.example.services.impl.DoneThat()), "ifYourInDaHood"), new com.example.services.impl.Many("family", "members", "only")))
        assert result == new com.example.beans.BigBean(new com.example.services.impl.DoneThat(0, "aDay", new com.example.services.impl.Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(new com.example.services.impl.DoneThat(), new com.example.services.impl.Many("family", "members", "only"), new com.example.services.impl.DoneThat()), "ifYourInDaHood"), new com.example.services.impl.Many("family", "members", "only"), new com.example.services.impl.DoneThat(0, "aDay", new com.example.services.impl.Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(new com.example.services.impl.DoneThat(), new com.example.services.impl.Many("family", "members", "only"), new com.example.services.impl.DoneThat()), "ifYourInDaHood"))
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme