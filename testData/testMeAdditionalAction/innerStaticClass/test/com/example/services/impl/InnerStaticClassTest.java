package com.example.services.impl;

import org.junit.Test;

/**
 * created by TestMe integration test on MMXVI
 */
public class InnerStaticClassTest {
    Foo.InnerStaticClass innerStaticClass = new Foo.InnerStaticClass();

    @Test
    public void testMethodOfInnerClass() throws Exception {
        innerStaticClass.methodOfInnerClass();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme