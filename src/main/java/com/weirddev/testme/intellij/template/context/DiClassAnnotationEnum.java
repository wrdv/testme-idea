package com.weirddev.testme.intellij.template.context;

import lombok.Getter;

@Getter
public enum DiClassAnnotationEnum {
    SINGLETON("javax.inject.Singleton"),
    SERVICE("org.springframework.stereotype.Service"),
    COMPONENT("org.springframework.stereotype.Component"),
    REPOSITORY("org.springframework.stereotype.Repository"),
    CONTROLLER("org.springframework.stereotype.Controller"),
    REST_CONTROLLER("org.springframework.web.bind.annotation.RestController"),
    CONFIGURATION("org.springframework.context.annotation.Configuration"),
    FOO_SERVICE("com.example.annotations.FooService")// for integration test
    ;
    private final String canonicalName;

    DiClassAnnotationEnum(String canonicalName) {
        this.canonicalName = canonicalName;
    }

}
