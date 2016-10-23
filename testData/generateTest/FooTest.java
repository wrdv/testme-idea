package com.example.services.impl;

import com.example.services.FooFighter;
import com.example.wepons.Fire;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {

    @Mock private FooFighter fooFighter;

    @InjectMocks private Foo foo;

    @Before public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        String result = foo.fight(new Fire(), "someFoe");
        Assert.assertEquals(result,"<caret>expectedResult");
    }
}

/**
 * Generated with love by TestMe :) For reporting issues and submitting feature requests please visit: https://github.com/yaronyam/testme-intellij/issues
 */