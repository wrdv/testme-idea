package com.example.services.impl;

import com.example.foes.Fire;
import com.example.foes.Ice;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    Foo foo = new Foo();

    @Test
    public void testGetMeMyTee() throws Exception {
        Fire result = foo.getMeMyTee(new Fire());
        Assert.assertEquals(new Fire(), result);
    }

    @Test
    public void testU2() throws Exception {
        Map<Ice, Fire> result = foo.u2();
        Assert.assertEquals(new HashMap<Ice, Fire>() {{
            put(new Ice(), new Fire());
        }}, result);
    }

    @Test
    public void testINeedU() throws Exception {
        foo.iNeedU(new Ice());
    }

    @Test
    public void testINeedUT() throws Exception {
        String result = foo.iNeedUT(new Ice(), new Fire());
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testGetMeMyTees() throws Exception {
        List<Fire> result = foo.getMeMyTees(new Fire());
        Assert.assertEquals(Arrays.<Fire>asList(new Fire()), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme