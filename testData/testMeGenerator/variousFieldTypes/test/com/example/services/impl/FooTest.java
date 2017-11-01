package com.example.services.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    com.example.warriers.FooFighter fooFighter;
    @Mock
    com.example.warriers.FooFighter fooFighterProtected;
    @Mock
    com.example.warriers.FooFighter fooFighterDefault;
    @Mock
    com.example.warriers.FooFighter fooFighterPublic;
    @Mock
    com.example.warriers.FooFighter fooFighterFinal;
    @Mock
    com.example.warriers.FooFighter fooFighterStatic;
    @Mock
    Foo.PublicInnerClass publicInnerClass;
    @Mock
    Foo.InnerStaticClass innerStaticClass;
    @Mock
    Foo.PublicInnerClass.InnerOfPublicInnerClass innerOfPublicInnerClass;
    @Mock
    Foo.InnerClass innerClass;
    @Mock
    Foo.PublicInnerClass anonymousPublicInnerClass;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        when(fooFighter.fight(any())).thenReturn("fightResponse");
        when(fooFighterProtected.fight(any())).thenReturn("fightResponse");
        when(fooFighterDefault.fight(any())).thenReturn("fightResponse");
        when(fooFighterPublic.fight(any())).thenReturn("fightResponse");
        when(fooFighterFinal.fight(any())).thenReturn("fightResponse");
        when(fooFighterStatic.fight(any())).thenReturn("fightResponse");
        when(innerOfPublicInnerClass.methodOfInnerClass()).thenReturn(new Foo().new PublicInnerClass().new InnerOfPublicInnerClass());

        String result = foo.fight(new com.example.foes.Fire(), "foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme