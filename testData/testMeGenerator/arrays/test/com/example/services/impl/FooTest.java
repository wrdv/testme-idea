package com.example.services.impl;

import com.example.foes.Fire;
import org.junit.Assert;
import org.junit.Test;

/** created by TestMe integration test on MMXVI */
public class FooTest {
    //Field fooFighter of type FooFighter[] - was not mocked since Mockito doesn't mock arrays
    Foo foo= new Foo();

    @Test
    public void testFight() throws Exception {
        String[] result = foo.fight(new Fire[]{new Fire()}, new String[]{"foeName"}, new int[]{0});
        Assert.assertArrayEquals(new String[]{"replaceMeWithExpectedResult"}, result);
    }

    @Test
    public void testFireStarter() throws Exception {
        Fire[] result = foo.fireStarter(new String[]{"foeName"}, new int[]{0});
        Assert.assertArrayEquals(new Fire[]{new Fire()}, result);
    }

    @Test
    public void testFireCounter() throws Exception {
        int[] result = foo.fireCounter(new Fire[]{new Fire()});
        Assert.assertArrayEquals(new int[]{0}, result);
    }

    @Test
    public void testRouterSearch() throws Exception {
        String result = foo.routerSearch("dstIp", new String[][]{new String[]{"ipTable"}});
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testA3dimSearch() throws Exception {
        String result = foo.a3dimSearch("dstIp", new String[][][]{new String[][]{new String[]{"ipTable"}}});
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme