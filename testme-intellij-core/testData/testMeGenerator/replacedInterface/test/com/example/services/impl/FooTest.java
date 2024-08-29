package com.example.services.impl;

import com.example.dependencies.ChildWithSetters;
import com.example.dependencies.MasterInterface;
import com.example.foes.BeanDependsOnInterface;
import org.junit.Assert;
import org.junit.Test;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    Foo foo = new Foo();

    @Test
    public void testJack() throws Exception {
        MasterInterface result = foo.jack(new BeanDependsOnInterface(new ChildWithSetters()), new ChildWithSetters());
        Assert.assertEquals(new ChildWithSetters(), result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme