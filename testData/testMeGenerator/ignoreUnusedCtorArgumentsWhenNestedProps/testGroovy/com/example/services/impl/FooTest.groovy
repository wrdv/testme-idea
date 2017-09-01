package com.example.services.impl

import com.example.beans.BeanByCtor
import com.example.foes.Fire
import com.example.foes.Ice
import com.example.groovies.Groove
import com.example.groovies.ImGroovy
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
    FooFighter fooFighter
    @InjectMocks
    Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testBuildWithNestedCtorCalls() {
        ContainNestedPropertyWithCtor result = foo.buildWithNestedCtorCalls([new BeanByCtor("myName", new Ice(), null, 0d)], new ImGroovy(groove: new Groove(someString: "someString")))
        assert result == new ContainNestedPropertyWithCtor(new DelegateCtor("youre", "asCold", null, new Fire()), "someStr", null)
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme