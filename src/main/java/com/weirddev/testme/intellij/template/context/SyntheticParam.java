package com.weirddev.testme.intellij.template.context;

import java.util.ArrayList;

/**
 * Represents a synthetic parameter. not coded explicitly
 *
 * Date: 25/02/2017
 * @author Yaron Yamin
 */
public class SyntheticParam extends Param {

    private final boolean isProperty;

    public SyntheticParam(Type type, String name, boolean isProperty) {
        super(type, name,new ArrayList<Field>());
        this.isProperty = isProperty;
    }

    /**
     *
     * @return true when represents a bean property
     */
    public boolean isProperty() {
        return isProperty;
    }
}
