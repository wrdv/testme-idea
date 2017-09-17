package com.example.services.impl;

import com.example.beans.BeanByCtor;
import com.example.beans.BigBean;
import com.example.beans.JavaBean;
import com.example.foes.Ice;
import com.example.groovies.ImGroovy;
import com.example.groovies.ImGroovyWithTupleCtor;
import com.example.warriers.FooFighter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * created by TestMe integration test on MMXVI
 */
public class FooTest {
    @Mock
    FooFighter fooFighter;
    @InjectMocks
    Foo foo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFind() throws Exception {
        BigBean result = foo.find(Arrays.<BeanByCtor>asList(new BeanByCtor("myName", new Ice(), null, 0d)), new ImGroovy());
        Assert.assertEquals(new BigBean(null, new Many("family", "members", null), null), result);
    }

    @Test
    public void testUseGroovyTypeWithCtor() throws Exception {
        Many result = foo.useGroovyTypeWithCtor(new ImGroovyWithTupleCtor("myName", null, Arrays.<JavaBean>asList(new JavaBean())));
        Assert.assertEquals(new Many("family", "members", null), result);
    }

    @Test
    public void testReturnGroovyType() throws Exception {
        ImGroovyWithTupleCtor result = foo.returnGroovyType(new ImGroovy());
        Assert.assertEquals(new ImGroovyWithTupleCtor(null, new Ice(), Arrays.<JavaBean>asList(new JavaBean())), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme