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

import java.util.ArrayList;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    private FooFighter fooFighter;
    @InjectMocks
    private Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        BigBean result = foo.fight(new Fire(), new ArrayList<String>(), new BigBean(new com.example.beans.DoneThat(0, "aDay", new com.example.beans.Many(), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(new com.example.beans.DoneThat(0, "aDay", new com.example.beans.Many(), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(new com.example.beans.DoneThat(), new com.example.beans.Many(), new com.example.beans.DoneThat())), new com.example.beans.Many(), new com.example.beans.DoneThat())), new com.example.beans.Many(), new com.example.beans.DoneThat()), new BeanThere());
        Assert.assertEquals(new BigBean(new com.example.beans.DoneThat(0, "aDay", new com.example.beans.Many(), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(new com.example.beans.DoneThat(0, "aDay", new com.example.beans.Many(), new java.util.GregorianCalendar(2016, java.util.Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(new com.example.beans.DoneThat(), new com.example.beans.Many(), new com.example.beans.DoneThat())), new com.example.beans.Many(), new com.example.beans.DoneThat())), new com.example.beans.Many(), new com.example.beans.DoneThat()), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://github.com/yaronyam/testme-intellij/issues