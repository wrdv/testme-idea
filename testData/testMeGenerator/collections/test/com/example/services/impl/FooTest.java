package com.example.services.impl;

import com.example.foes.Fear;
import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.warriers.FooFighter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
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
        List<Fire> result = foo.fight(List.of(new Fire()), Set.of(new Fire()), Map.of("icebergs",new Ice()), List.of("strings"), List.of(List.of("collOfLists")), new LinkedList<>(List.of(List.of(new Fear()))), new LinkedList<>(List.of(new Fear())));
        Assert.assertEquals(List.of(new Fire()), result);
    }

    @Test
    public void testFightConcreteTypes() throws Exception {
        List<Fire> result = foo.fightConcreteTypes(new ArrayList<>(List.of(new Fire())), new HashSet<>(List.of(new Fire())), new HashMap<>(Map.of("icebergs",new Ice())), new Vector<>(List.of(List.of("collOfLists"))), new TreeSet<>(List.of(new Fear())), new Stack<>(){{push(new Fear());}});
        Assert.assertEquals(List.of(new Fire()), result);
    }

    @Test
    public void testTypeless() throws Exception {
        List result = foo.typeless(List.of("fires"), Set.of("flames"), Map.of("icebergs","icebergs"), List.of("strings"), List.of(List.of("collOfLists")), new LinkedList<>(List.of(List.of("mindTheGap"))));
        Assert.assertEquals(List.of("replaceMeWithExpectedResult"), result);
    }

    @Test
    public void testMiscColls() throws Exception {
        NavigableMap<String, Fire> result = foo.miscColls(new TreeMap<>(Map.of("spitfires",new Fire())), new TreeSet<>(List.of(new Fire())), new Vector<>(List.of("random")), new TreeSet<>(List.of(new Fire())));
        Assert.assertEquals(new TreeMap<>(Map.of("replaceMeWithExpectedResult",new Fire())), result);
    }

    @Test
    public void testObjectMaps() throws Exception {
        HashMap<Fire, Fire> result = foo.objectMaps(new TreeMap<>(Map.of(Boolean.TRUE,"earth")), Map.of(Integer.valueOf(0),Boolean.TRUE), new HashMap<>(Map.of(new Fire(),new Fire())));
        Assert.assertEquals(new HashMap<>(Map.of(new Fire(),new Fire())), result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme