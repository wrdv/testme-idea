package com.example.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
class FooTest {
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFight() {
        String result = foo.fight(new com.example.foes.Fire(), "foeName");
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme