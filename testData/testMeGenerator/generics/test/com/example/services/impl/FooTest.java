package com.example.services.impl;

import java.util.Set;
import com.example.foes.Ice;
import java.util.Map;
import com.example.foes.Pokemon;
import java.util.List;
import com.example.foes.Fire;
import com.example.warriers.FooFighter;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** created by TestMe integration test on MMXVI */
public class FooTest {
    @Mock
    private Set<Ice> escimoRealEstate;
    @Mock
    private Map<Pokemon,List<Fire>> iFart;
    @Mock
    private FooFighter fooFighter;
    @InjectMocks
    private Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        String result = foo.fight(new ArrayList<Fire>(), "foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testIntoTheVoid() throws Exception {
        foo.intoTheVoid();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://github.com/yaronyam/testme-intellij/issues