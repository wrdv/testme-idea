package com.example.services.impl;

import com.example.beans.BeanThere;
import com.example.beans.BigBean;
import com.example.foes.Fire;
import com.example.warriers.FooFighter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    FooFighter fooFighter;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        BigBean result = foo.fight(new Fire(), Arrays.<String>asList("String"), new BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(new DoneThat(0, "aDay", null, new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", null, new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood")), "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(new DoneThat(0, "aDay", null, new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", null, new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood")), "ifYourInDaHood")), new BeanThere(new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(new DoneThat(0, "aDay", null, new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", null, new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood")), "ifYourInDaHood"), new Many("family", "members", "only")));
        Assert.assertEquals(new BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(), "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(), "ifYourInDaHood")), "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(), "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(), "ifYourInDaHood")), "ifYourInDaHood")), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme