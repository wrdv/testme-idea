package com.weirddev.testme.intellij.generator;

import com.weirddev.testme.intellij.template.Field;
import com.weirddev.testme.intellij.template.Method;
import com.weirddev.testme.intellij.template.Param;
import com.weirddev.testme.intellij.template.Type;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassElementsLocator {
    Set<String> filterTypesInDefaultPackage(List<Method> methods, List<Field> fields) {
        HashSet<String> typesInDefaultPackage = new HashSet<String>();
        for (Field field : fields) {
            addTypesInDefaultPackage(typesInDefaultPackage, field.getType());
        }
        for (Method method : methods) {
            for (Param param : method.getMethodParams()) {
                addTypesInDefaultPackage(typesInDefaultPackage, param.getType());
            }
        }
        return typesInDefaultPackage;
    }

    void addTypesInDefaultPackage(HashSet<String> typesInDefaultPackage, Type type) {
        if ((type.getPackageName() == null || type.getPackageName().isEmpty()) && !type.isPrimitive()) {
            typesInDefaultPackage.add(type.getName());
        }
    }
}