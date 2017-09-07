package com.example.services.impl;

import com.example.foes.Fire;
import com.example.warriers.FooFighter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    FooFighter fooFighter;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        File result = foo.fight(new Fire(), new File(getClass().getResource("/com/example/services/impl/PleaseReplaceMeWithTestFile.txt").getFile()));
        Assert.assertEquals(new File(getClass().getResource("/com/example/services/impl/PleaseReplaceMeWithTestFile.txt").getFile()), result);
    }

    @Test
    public void testStudy() throws Exception {
        Class result = foo.study(Class.forName("com.example.services.impl.Foo"));
        Assert.assertEquals(Class.forName("com.example.services.impl.Foo"), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme