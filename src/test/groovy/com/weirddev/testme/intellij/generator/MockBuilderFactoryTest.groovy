package com.weirddev.testme.intellij.generator

import com.intellij.openapi.diagnostic.Logger
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import spock.lang.Specification
import spock.lang.Unroll

class MockBuilderFactoryTest extends Specification {
    @Mock
    Logger logger
    @InjectMocks
    MockBuilderFactory mockBuilderFactory

    def setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Unroll
    def "resolve Mockito Version where classpathJars=#classpathJars then expect: #expectedResult"() {
        given:
        mockBuilderFactory = new MockBuilderFactory()

        expect:
        mockBuilderFactory.resolveMockitoVersion(classpathJars) == expectedResult

        where:
        classpathJars                                                         || expectedResult
        null                                                                  || null
        []                                                                    || null
        ["c:/path/to/mockito-all-4.3.1.jar", "c:/path/to/another-2.1.2.jar"]  || null
        ["c:/path/to/mockito-core-4.3.1.jar", "c:/path/to/another-2.1.2.jar"] || "4.3.1"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme