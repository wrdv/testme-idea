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
        List<Fire> result = foo.fight([new Fire()], [new Fire()] as Set<Fire>, ["String":new Ice()], ["String"], [["String"]], new LinkedList<List<Fear>>([[new Fear()]]), new LinkedList([new Fear()]))
        assert result == [new Fire()]
    }

    @Test
    void testFightConcreteTypes() {
        List<Fire> result = foo.fightConcreteTypes([new Fire()], [new Fire()] as HashSet, ["String":new Ice()], new Vector([["String"]]), [new Fear()] as TreeSet, new Stack<Fear>(){{push(new Fear())}})
        assert result == [new Fire()]
    }

    @Test
    void testTypeless() {
        List result = foo.typeless(["String"], ["String"] as Set, ["String":"String"], ["String"], [["String"]], new LinkedList<List>([["String"]]))
        assert result == ["String"]
    }

    @Test
    void testMiscColls() {
        NavigableMap<String, Fire> result = foo.miscColls(new TreeMap(["String":new Fire()]), new TreeSet([new Fire()]), new Vector(["String"]), [new Fire()] as SortedSet<Fire>)
        assert result == new TreeMap(["String":new Fire()])
    }

    @Test
    void testObjectMaps() {
        HashMap<Fire, Fire> result = foo.objectMaps(new TreeMap([(Boolean.TRUE):"String"]), [(0):Boolean.TRUE], [(new Fire()):new Fire()])
        assert result == [(new Fire()):new Fire()]
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme