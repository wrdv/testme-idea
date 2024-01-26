package com.example.services.impl

import com.example.foes.Fire
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
    void testFight() {
        File result = foo.fight(new Fire(), new File(getClass().getResource("/com/example/services/impl/PleaseReplaceMeWithTestFile.txt").getFile()))
        assert result == new File(getClass().getResource("/com/example/services/impl/PleaseReplaceMeWithTestFile.txt").getFile())
    }

    @Test
    void testStudy() {
        Class result = foo.study(Class.forName("com.example.services.impl.Foo"))
        assert result == Class.forName("com.example.services.impl.Foo")
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme