package com.example.services.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    com.example.warriers.FooFighter fooFighter;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        com.example.beans.BigBean result = foo.fight(new com.example.foes.Fire(), java.util.Arrays.<String>asList("String"), new com.example.beans.BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(new DoneThat(0, "aDay", null, new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", null, new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood")), "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", new Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(new DoneThat(0, "aDay", null, new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", null, new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood")), "ifYourInDaHood")), new com.example.beans.BeanThere(new DoneThat(0, "aDay", new Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(new DoneThat(0, "aDay", null, new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", null, new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood")), "ifYourInDaHood"), new Many("family", "members", "only")));
        Assert.assertEquals(new com.example.beans.BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(null, null, null), "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", new Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(null, null, null), "ifYourInDaHood")), "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", new Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(null, null, null), "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", new Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(null, null, null), "ifYourInDaHood")), "ifYourInDaHood")), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme