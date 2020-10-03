package com.weirddev.testme.intellij.template.context;

import lombok.Getter;

/**
 * Represents a Method call argument.
 * Date: 23/06/2017
 *
 * @author Yaron Yamin
 */
public class MethodCallArgument {
    /**
     * textual expression of the argument passed to the method call
     */
    @Getter private final String text;

    public MethodCallArgument(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodCallArgument)) return false;

        MethodCallArgument that = (MethodCallArgument) o;

        return text != null ? text.equals(that.text) : that.text == null;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }
}
