package com.example.services.impl;

import com.example.foes.Fear;
import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.warriers.FooFighter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.*;
/** created by TestMe integration test on MMXVI */
public class FooTest {
    @Mock
    List<FooFighter> fooFighter;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        List<Fire> result = foo.fight(Arrays.<Fire>asList(new Fire()), new HashSet<Fire>(Arrays.asList(new Fire())), new HashMap<String,Ice>(){{put("String",new Ice());}}, Arrays.<String>asList("String"), Arrays.<List<String>>asList(Arrays.<String>asList("String")), new LinkedList<List<Fear>>(Arrays.asList(Arrays.<Fear>asList(new Fear()))), new LinkedList<Fear>(Arrays.asList(new Fear())));
        Assert.assertEquals(Arrays.<Fire>asList(new Fire()), result);
    }

    @Test
    public void testTypeless() throws Exception {
        List result = foo.typeless(Arrays.asList("String"), new HashSet(Arrays.asList("String")), new HashMap(){{put("String","String");}}, Arrays.asList("String"), Arrays.<List>asList(Arrays.asList("String")), new LinkedList<List>(Arrays.asList(Arrays.asList("String"))));
        Assert.assertEquals(Arrays.asList("String"), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme