package com.example.services.impl;

import org.junit.Test;

/**
 * created by TestMe integration test on MMXVI
 */
public class PublicInnerClassTest {

    Foo.PublicInnerClass innerStaticClass = new Foo().new PublicInnerClass();

    @Test
    public void testMethodOfInnerClass() throws Exception {
        publicInnerClass.methodOfInnerClass();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://github.com/yaronyam/testme-intellij/issues