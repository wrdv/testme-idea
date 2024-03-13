package com.weirddev.testme.intellij.template.context;

import lombok.Getter;

@Getter
public enum DiFieldAnnotationEnum {
    INJECT("javax.inject.Inject"),
    NAMED("javax.inject.Named"),
    QUALIFIER("javax.inject.Qualifier"),
    QUALIFIER_SPRING("org.springframework.beans.factory.annotation.Qualifier"),
    AUTOWIRED("org.springframework.beans.factory.annotation.Autowired"),
    RESOURCE("javax.annotation.Resource"),
    FOO_RESOURCE("com.example.annotations.FooResource")// for integration test
    ;
    private final String canonicalName;

    DiFieldAnnotationEnum(String canonicalName) {
        this.canonicalName = canonicalName;
    }

}
