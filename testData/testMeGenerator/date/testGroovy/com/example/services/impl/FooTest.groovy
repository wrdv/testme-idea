package com.example.services.impl

import com.example.foes.Fire
import com.example.warriers.FooFighter
import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.ZoneOffset
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
    void testFight() {
        Date result = foo.fight(new Fire(), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime())
        assert result == new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime()
    }

    @Test
    void testFightAnyDay() {
        LocalDate result = foo.fightAnyDay(new Fire(), LocalDate.of(2016, Month.JANUARY, 11))
        assert result == LocalDate.of(2016, Month.JANUARY, 11)
    }

    @Test
    void testFightAnyTime() {
        LocalTime result = foo.fightAnyTime(new Fire(), LocalTime.of(22, 45, 55))
        assert result == LocalTime.of(22, 45, 55)
    }

    @Test
    void testFightAnyDate() {
        LocalDateTime result = foo.fightAnyDate(new Fire(), LocalDateTime.of(2016, Month.JANUARY, 11, 22, 45, 55))
        assert result == LocalDateTime.of(2016, Month.JANUARY, 11, 22, 45, 55)
    }

    @Test
    void testMaybe() {
        Instant result = foo.maybe(LocalDateTime.of(2016, Month.JANUARY, 11, 22, 45, 55).toInstant(ZoneOffset.UTC), new Fire())
        assert result == LocalDateTime.of(2016, Month.JANUARY, 11, 22, 45, 55).toInstant(ZoneOffset.UTC)
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme