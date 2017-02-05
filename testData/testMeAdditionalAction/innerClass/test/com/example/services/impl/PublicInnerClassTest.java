package com.example.services.impl;

import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
public class PublicInnerClassTest {

    Foo.PublicInnerClass publicInnerClass = new Foo().new PublicInnerClass();

    @Test
    public void testMethodOfInnerClass() throws Exception {
        publicInnerClass.methodOfInnerClass();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme