package com.example.services.impl

import com.example.beans.JavaBean
import com.example.foes.Ice
import org.junit.Test
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import static org.mockito.Mockito.*

/** created by TestMe integration test on MMXVI */
class FoovyTest {
    @Mock
    Ice ice
    @Mock
    Collection<JavaBean> myBeans
    @InjectMocks
    Foovy foovy

    @Before
    void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    void testSmashing() {
        String result = foovy.smashing("somePumpkins", (char) 'a', new Ice())
        assert result == "replaceMeWithExpectedResult"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme