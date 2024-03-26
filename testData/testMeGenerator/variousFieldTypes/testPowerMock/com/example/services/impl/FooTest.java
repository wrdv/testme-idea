package com.example.services.impl;

import org.junit.Assert;
import org.junit.Test;

import static org.powermock.api.mockito.PowerMockito.*;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.runner.RunWith;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

/**
 * created by TestMe integration test on MMXVI
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Foo.class})
@PowerMockIgnore("javax.management.*")
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
        when(fooFighter.fight(any(com.example.foes.Fire.class))).thenReturn("fightResponse");
        when(fooFighterProtected.fight(any(com.example.foes.Fire.class))).thenReturn("fightResponse");
        when(fooFighterDefault.fight(any(com.example.foes.Fire.class))).thenReturn("fightResponse");
        when(fooFighterPublic.fight(any(com.example.foes.Fire.class))).thenReturn("fightResponse");
        when(fooFighterFinal.fight(any(com.example.foes.Fire.class))).thenReturn("fightResponse");
        when(fooFighterStatic.fight(any(com.example.foes.Fire.class))).thenReturn("fightResponse");
        when(innerOfPublicInnerClass.methodOfInnerClass()).thenReturn(new Foo().new PublicInnerClass().new InnerOfPublicInnerClass());
        String result = foo.fight(new com.example.foes.Fire(), "foeName");
        verify(publicInnerClass).methodOfInnerClass();
        verify(innerStaticClass).methodOfInnerClass();
        verify(innerClass).methodOfInnerClass();
        verify(anonymousPublicInnerClass).methodOfInnerClass();
        Assert.assertEquals("replaceMeWithExpectedResult", result);

    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme