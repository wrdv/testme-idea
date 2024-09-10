package com.weirddev.testme.intellij.generator;

/**
 * Date: 25/12/2017
 *
 * @author Yaron Yamin
 */
public enum OptionalPluginTestDependency {
    Groovy("org.jetbrains.plugins.groovy.GroovyLanguage","enableIdeaGroovyPlugin"),
    Scala("org.jetbrains.plugins.scala.ScalaLanguage","enableIdeaScalaPlugin");

    private String classId;
    private String buildProperty;

    OptionalPluginTestDependency(String classId, String buildProperty) {
        this.classId = classId;
        this.buildProperty = buildProperty;
    }

    public String getClassId() {
        return classId;
    }

    public String getBuildProperty() {
        return buildProperty;
    }
}
