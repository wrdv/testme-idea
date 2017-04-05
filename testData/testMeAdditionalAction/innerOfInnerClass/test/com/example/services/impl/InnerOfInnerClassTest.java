package com.example.services.impl;

import org.junit.Test;

/**
 * created by TestMe integration test on MMXVI
 */
public class InnerOfInnerClassTest {
    Foo.InnerClass.InnerOfInnerClass innerOfInnerClass = new Foo().new InnerClass().new InnerOfInnerClass();

    @Test
    public void testMethodOfInnerClass() throws Exception {
        innerOfInnerClass.methodOfInnerClass();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme