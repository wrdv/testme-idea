package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.services.impl.Foo.PublicInnerClass;
import com.example.services.impl.Foo.InnerStaticClass;
import com.example.services.impl.Foo.PublicInnerClass.InnerOfPublicInnerClass;
import com.example.services.impl.Foo.InnerClass;
import com.example.foes.Fire;
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
    private FooFighter fooFighter;
    @Mock
    private FooFighter fooFighterProtected;
    @Mock
    private FooFighter fooFighterDefault;
    @Mock
    private FooFighter fooFighterPublic;
    @Mock
    private FooFighter fooFighterFinal;
    @Mock
    private FooFighter fooFighterStatic;
    //TODO Field byteFieldWrapper of type Byte - was not mocked since Mockito doesn't mock a Final class
    //TODO Field shortFieldWrapper of type Short - was not mocked since Mockito doesn't mock a Final class
    //TODO Field intFieldWrapper of type Integer - was not mocked since Mockito doesn't mock a Final class
    //TODO Field longFieldWrapper of type Long - was not mocked since Mockito doesn't mock a Final class
    //TODO Field floatFieldWrapper of type Float - was not mocked since Mockito doesn't mock a Final class
    //TODO Field doubleFieldWrapper of type Double - was not mocked since Mockito doesn't mock a Final class
    //TODO Field charFieldWrapper of type Character - was not mocked since Mockito doesn't mock a Final class
    //TODO Field booleanFieldWrapper of type Boolean - was not mocked since Mockito doesn't mock a Final class
    @Mock
    private PublicInnerClass publicInnerClass;
    @Mock
    private InnerStaticClass innerStaticClass;
    @Mock
    private InnerOfPublicInnerClass innerOfPublicInnerClass;
    @Mock
    private InnerClass innerClass;
    @Mock
    private PublicInnerClass anonymousPublicInnerClass;
    @InjectMocks
    private Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFight() throws Exception {
        String result = foo.fight(new Fire(), "foeName");
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://github.com/yaronyam/testme-intellij/issues