package com.example.services.impl;

import java.util.List;
import com.example.warriers.FooFighter;
import com.example.foes.Fire;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import com.example.foes.Ice;
import java.util.LinkedList;
import com.example.foes.Fear;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** created by TestMe integration test on MMXVI */
public class FooTest {
    @Mock
    private List<FooFighter> fooFighter;
    @InjectMocks
    private Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        List<Fire> result = foo.fight(new ArrayList<Fire>(), new HashSet<Fire>(), new HashMap<String, Ice>(), new ArrayList<String>(), new ArrayList<List<String>>(), new LinkedList<List<Fear>>());
        Assert.assertEquals(new ArrayList<Fire>(), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://github.com/yaronyam/testme-intellij/issues