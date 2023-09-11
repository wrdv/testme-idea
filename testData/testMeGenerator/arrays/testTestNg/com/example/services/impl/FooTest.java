package com.example.services.impl;

import com.example.foes.Fire;
import org.testng.Assert;
import org.testng.annotations.Test;
/** created by TestMe integration test on MMXVI */
public class FooTest {
    //Field fooFighter of type FooFighter[] - was not mocked since Mockito doesn't mock arrays
    Foo foo= new Foo();

    @Test
    public void testFight(){
        String[] result = foo.fight(new Fire[]{new Fire()}, new String[]{"foeName"}, new int[]{0});
        Assert.assertEquals(result, new String[]{"replaceMeWithExpectedResult"});
    }

    @Test
    public void testFireStarter(){
        Fire[] result = foo.fireStarter(new String[]{"foeName"}, new int[]{0});
        Assert.assertEquals(result, new Fire[]{new Fire()});
    }

    @Test
    public void testFireCounter(){
        int[] result = foo.fireCounter(new Fire[]{new Fire()});
        Assert.assertEquals(result, new int[]{0});
    }

    @Test
    public void testRouterSearch(){
        String result = foo.routerSearch("dstIp", new String[][]{new String[]{"ipTable"}});
        Assert.assertEquals(result, "replaceMeWithExpectedResult");
    }

    @Test
    public void testA3dimSearch(){
        String result = foo.a3dimSearch("dstIp", new String[][][]{new String[][]{new String[]{"ipTable"}}});
        Assert.assertEquals(result, "replaceMeWithExpectedResult");
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme