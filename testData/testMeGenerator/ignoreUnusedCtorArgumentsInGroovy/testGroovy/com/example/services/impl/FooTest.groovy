package com.example.services.impl

import com.example.beans.BeanByCtor
import com.example.beans.BigBean
import com.example.beans.JavaBean
import com.example.foes.Ice
import com.example.groovies.Groove
import com.example.groovies.ImGroovy
import com.example.groovies.ImGroovyWithTupleCtor
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
    void testFind() {
        BigBean result = foo.find([new BeanByCtor("myName", null, null, 0d)], new ImGroovy(groove: new Groove(someString: "someString")))
        assert result == new BigBean(null, new Many("family", "members", null), null)
    }

    @Test
    void testUseGroovyTypeWithCtor() {
        Many result = foo.useGroovyTypeWithCtor(new ImGroovyWithTupleCtor("myName", null, [new JavaBean()]))
        assert result == new Many("family", "members", null)
    }

    @Test
    void testReturnGroovyType() {
        ImGroovyWithTupleCtor result = foo.returnGroovyType(new ImGroovy(ice: new Ice(), myBeans: [new JavaBean()]))
        assert result == new ImGroovyWithTupleCtor(null, new Ice(), [new JavaBean()])
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme