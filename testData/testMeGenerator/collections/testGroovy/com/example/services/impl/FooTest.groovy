package com.example.services.impl

import com.example.foes.Fear
import com.example.foes.Fire
import com.example.foes.Ice
import com.example.warriers.FooFighter
import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest {
    @Mock
    List<FooFighter> fooFighter
    @InjectMocks
    Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testFight() {
        List<Fire> result = foo.fight([new Fire()], [new Fire()] as Set<Fire>, ["icebergs":new Ice()], ["strings"], [["collOfLists"]], new LinkedList<List<Fear>>([[new Fear()]]), new LinkedList([new Fear()]))
        assert result == [new Fire()]
    }

    @Test
    void testFightConcreteTypes() {
        List<Fire> result = foo.fightConcreteTypes([new Fire()], [new Fire()] as HashSet, ["icebergs":new Ice()], new Vector([["collOfLists"]]), [new Fear()] as TreeSet, new Stack<Fear>(){{push(new Fear())}})
        assert result == [new Fire()]
    }

    @Test
    void testTypeless() {
        List result = foo.typeless(["fires"], ["flames"] as Set, ["icebergs":"icebergs"], ["strings"], [["collOfLists"]], new LinkedList<List>([["mindTheGap"]]))
        assert result == ["replaceMeWithExpectedResult"]
    }

    @Test
    void testMiscColls() {
        NavigableMap<String, Fire> result = foo.miscColls(new TreeMap(["spitfires":new Fire()]), new TreeSet([new Fire()]), new Vector(["random"]), [new Fire()] as SortedSet<Fire>)
        assert result == new TreeMap(["replaceMeWithExpectedResult":new Fire()])
    }

    @Test
    void testObjectMaps() {
        HashMap<Fire, Fire> result = foo.objectMaps(new TreeMap([(Boolean.TRUE):"earth"]), [(0):Boolean.TRUE], [(new Fire()):new Fire()])
        assert result == [(new Fire()):new Fire()]
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme