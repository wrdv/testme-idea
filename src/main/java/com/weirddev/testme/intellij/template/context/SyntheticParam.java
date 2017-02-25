package com.weirddev.testme.intellij.template.context;

/**
 * Date: 25/02/2017
 *
 * @author Yaron Yamin
 */
public class SyntheticParam extends Param {
    final boolean isProperty;
    public SyntheticParam(Type type, String name, boolean isProperty) {
        super(type, name);
        this.isProperty = isProperty;
    }
    public boolean isProperty() {
        return isProperty;
    }
}
