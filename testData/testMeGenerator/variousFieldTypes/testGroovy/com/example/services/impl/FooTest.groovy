package com.example.services.impl

import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest {
    @Mock
    com.example.warriers.FooFighter fooFighter
    @Mock
    com.example.warriers.FooFighter fooFighterProtected
    @Mock
    com.example.warriers.FooFighter fooFighterDefault
    @Mock
    com.example.warriers.FooFighter fooFighterPublic
    @Mock
    com.example.warriers.FooFighter fooFighterFinal
    @Mock
    com.example.warriers.FooFighter fooFighterStatic
    @Mock
    com.example.services.impl.Foo.PublicInnerClass publicInnerClass
    @Mock
    com.example.services.impl.Foo.InnerStaticClass innerStaticClass
    @Mock
    com.example.services.impl.Foo.PublicInnerClass.InnerOfPublicInnerClass innerOfPublicInnerClass
    @Mock
    com.example.services.impl.Foo.InnerClass innerClass
    @Mock
    com.example.services.impl.Foo.PublicInnerClass anonymousPublicInnerClass
    @InjectMocks
    com.example.services.impl.Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testFight() {
        when(fooFighter.fight(any())).thenReturn("fightResponse")
        when(fooFighterProtected.fight(any())).thenReturn("fightResponse")
        when(fooFighterDefault.fight(any())).thenReturn("fightResponse")
        when(fooFighterPublic.fight(any())).thenReturn("fightResponse")
        when(fooFighterFinal.fight(any())).thenReturn("fightResponse")
        when(fooFighterStatic.fight(any())).thenReturn("fightResponse")
        when(innerOfPublicInnerClass.methodOfInnerClass()).thenReturn(new com.example.services.impl.Foo.PublicInnerClass.InnerOfPublicInnerClass(new com.example.services.impl.Foo.PublicInnerClass(new com.example.services.impl.Foo())))

        java.lang.String result = foo.fight(new com.example.foes.Fire(), "foeName")
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme