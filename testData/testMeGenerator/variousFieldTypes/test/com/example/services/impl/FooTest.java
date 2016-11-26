package com.example.services.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    private com.example.warriers.FooFighter fooFighter;
    @Mock
    private com.example.warriers.FooFighter fooFighterProtected;
    @Mock
    private com.example.warriers.FooFighter fooFighterDefault;
    @Mock
    private com.example.warriers.FooFighter fooFighterPublic;
    @Mock
    private com.example.warriers.FooFighter fooFighterFinal;
    @Mock
    private com.example.warriers.FooFighter fooFighterStatic;
    //Field byteFieldWrapper of type Byte - was not mocked since Mockito doesn't mock a Final class
    //Field shortFieldWrapper of type Short - was not mocked since Mockito doesn't mock a Final class
    //Field intFieldWrapper of type Integer - was not mocked since Mockito doesn't mock a Final class
    //Field longFieldWrapper of type Long - was not mocked since Mockito doesn't mock a Final class
    //Field floatFieldWrapper of type Float - was not mocked since Mockito doesn't mock a Final class
    //Field doubleFieldWrapper of type Double - was not mocked since Mockito doesn't mock a Final class
    //Field charFieldWrapper of type Character - was not mocked since Mockito doesn't mock a Final class
    //Field booleanFieldWrapper of type Boolean - was not mocked since Mockito doesn't mock a Final class
    @Mock
    private Foo.PublicInnerClass publicInnerClass;
    @Mock
    private Foo.InnerStaticClass innerStaticClass;
    @Mock
    private Foo.PublicInnerClass.InnerOfPublicInnerClass innerOfPublicInnerClass;
    @Mock
    private Foo.InnerClass innerClass;
    @Mock
    private Foo.PublicInnerClass anonymousPublicInnerClass;
    @InjectMocks
    private Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        String result = foo.fight(new com.example.foes.Fire(), "foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://github.com/yaronyam/testme-intellij/issues