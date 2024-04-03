package com.example.services.impl;

import com.example.beans.BeanThere;
import com.example.beans.BigBean;
import com.example.dependencies.MasterInterface;
import com.example.foes.Ace;
import com.example.foes.BeanDependsOnInterface;
import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.warriers.FooFighter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
        when(fooFighter.fight(any(Fire.class))).thenReturn("fightResponse");

        BigBean result = foo.fight(new Fire(), List.of("foeName"), new BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood")), new BeanThere(new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(null, new Many("family", "members", "only"), null), "ifYourInDaHood"), new Many("family", "members", "only")));
        Assert.assertEquals(new BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood")), result);
    }

    @Test
    public void testVarargs() throws Exception {
        Ice[] result = foo.varargs(new Fire(), new Ace(new Ice()), new Ice());
        Assert.assertArrayEquals(new Ice[]{new Ice()}, result);
    }

    @Test
    public void testJack() throws Exception {
        MasterInterface result = foo.jack(new BeanDependsOnInterface(null), null);
        Assert.assertEquals(null, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme