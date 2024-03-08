package com.weirddev.testme.intellij.template.context;

import lombok.Getter;

@Getter
public enum DiFieldAnnotationEnum {
    AUTOWIRED("org.springframework.beans.factory.annotation.Autowired"),
    RESOURCE("javax.annotation.Resource"),
    FOO_RESOURCE("com.example.annotations.FooResource")// for integration test
    ;
    private final String canonicalName;

    DiFieldAnnotationEnum(String canonicalName) {
        this.canonicalName = canonicalName;
    }

}
