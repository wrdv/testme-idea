package com.example.services.impl

import com.example.SelfReferringType
import com.example.dependencies.SelfishService
import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FooTest {
    @Mock
    SelfishService selfishService
    @InjectMocks
    Foo foo

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testGetSelf() {
        when(selfishService.getSelfishType()).thenReturn(SelfReferringType.ONE)

        String result = foo.getSelf()
        assert result == "replaceMeWithExpectedResult"
    }

    @Test
    void testDoSelf() {
        when(selfishService.getSelfishType()).thenReturn(SelfReferringType.ONE)

        String result = foo.doSelf()
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme