package com.example.services.impl

import com.example.beans.BeanThere
import com.example.beans.BigBean
import com.example.dependencies.MasterInterface
import com.example.foes.Ace
import com.example.foes.BeanDependsOnInterface
import com.example.foes.Fire
import com.example.foes.Ice
import com.example.warriers.FooFighter
import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
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
        when(fooFighter.fight(any())).thenReturn("fightResponse")

        BigBean result = foo.fight(new Fire(), ["foeName"], new BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood")), new BeanThere(new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), new BigBean(null, new Many("family", "members", "only"), null), "ifYourInDaHood"), new Many("family", "members", "only")))
        assert result == new BigBean(new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"), new Many("family", "members", "only"), new DoneThat(0, "aDay", new Many("family", "members", "only"), new GregorianCalendar(2016, Calendar.JANUARY, 11, 22, 45).getTime(), null, "ifYourInDaHood"))
    }

    @Test
    void testVarargs() {
        Ice[] result = foo.varargs(new Fire(), new Ace(new Ice()), new Ice())
        assert result == [new Ice()] as Ice[]
    }

    @Test
    void testJack() {
        MasterInterface result = foo.jack(new BeanDependsOnInterface(null), null)
        assert result == null
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme