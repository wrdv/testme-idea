package com.weirddev.testme.intellij.template.context;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum DiFieldAnnotationEnum {
    INJECT("javax.inject.Inject"),
    NAMED("javax.inject.Named"),
    QUALIFIER("javax.inject.Qualifier"),
    QUALIFIER_SPRING("org.springframework.beans.factory.annotation.Qualifier"),
    AUTOWIRED("org.springframework.beans.factory.annotation.Autowired"),
    RESOURCE("javax.annotation.Resource"),
    ;
    private final String canonicalName;

    private static final List<String> annStrList = Arrays.stream(DiFieldAnnotationEnum.values())
        .map(DiFieldAnnotationEnum::getCanonicalName).toList();

    public static boolean isDiFieldAnnotation(String annName) {
        return annStrList.contains(annName);
    }

    DiFieldAnnotationEnum(String canonicalName) {
        this.canonicalName = canonicalName;
    }

}
