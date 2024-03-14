package com.weirddev.testme.intellij.template.context;

import lombok.Getter;

import java.util.*;

@Getter
public enum DiClassAnnotationEnum {
    SINGLETON("javax.inject.Singleton"),
    SERVICE("org.springframework.stereotype.Service"),
    COMPONENT("org.springframework.stereotype.Component"),
    REPOSITORY("org.springframework.stereotype.Repository"),
    CONTROLLER("org.springframework.stereotype.Controller"),
    REST_CONTROLLER("org.springframework.web.bind.annotation.RestController"),
    CONFIGURATION("org.springframework.context.annotation.Configuration"),
    ;
    private final String canonicalName;

    private static final List<String> annStrList = Arrays.stream(DiClassAnnotationEnum.values())
        .map(DiClassAnnotationEnum::getCanonicalName).toList();

    public static boolean isDiClassAnnotation(String annName) {
        return annStrList.contains(annName);
    }

    DiClassAnnotationEnum(String canonicalName) {
        this.canonicalName = canonicalName;
    }

}
