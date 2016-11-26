package com.example.services.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    private com.example.warriers.FooFighter fooFighter;
    @InjectMocks
    private Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        com.example.beans.BigBean result = foo.fight(new com.example.foes.Fire(), new java.util.ArrayList<String>(), new com.example.beans.BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(new DoneThat(0, "aDay", new Many(), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(), "ifYourInDaHood"), new Many(), new DoneThat()), "ifYourInDaHood"), new Many(), new DoneThat()), new com.example.beans.BeanThere());
        Assert.assertEquals(new com.example.beans.BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(new DoneThat(0, "aDay", new Many(), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new com.example.beans.BigBean(), "ifYourInDaHood"), new Many(), new DoneThat()), "ifYourInDaHood"), new Many(), new DoneThat()), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://github.com/yaronyam/testme-intellij/issues