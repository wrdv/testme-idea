package com.example.services.impl;

import com.example.foes.Fire;
import com.example.warriers.FooFighter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.*;

/** created by TestMe integration test on MMXVI */
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
        Date result = foo.fight(new Fire(), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime());
        Assert.assertEquals(new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), result);
    }

    @Test
    public void testFightAnyDay() throws Exception {
        LocalDate result = foo.fightAnyDay(new Fire(), LocalDate.of(2016, Month.JANUARY, 11));
        Assert.assertEquals(LocalDate.of(2016, Month.JANUARY, 11), result);
    }

    @Test
    public void testFightAnyTime() throws Exception {
        LocalTime result = foo.fightAnyTime(new Fire(), LocalTime.of(22, 45, 55));
        Assert.assertEquals(LocalTime.of(22, 45, 55), result);
    }

    @Test
    public void testFightAnyDate() throws Exception {
        LocalDateTime result = foo.fightAnyDate(new Fire(), LocalDateTime.of(2016, Month.JANUARY, 11, 22, 45, 55));
        Assert.assertEquals(LocalDateTime.of(2016, Month.JANUARY, 11, 22, 45, 55), result);
    }

    @Test
    public void testMaybe() throws Exception {
        Instant result = foo.maybe(LocalDateTime.of(2016, Month.JANUARY, 11, 22, 45, 55).toInstant(ZoneOffset.UTC), new Fire());
        Assert.assertEquals(LocalDateTime.of(2016, Month.JANUARY, 11, 22, 45, 55).toInstant(ZoneOffset.UTC), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme