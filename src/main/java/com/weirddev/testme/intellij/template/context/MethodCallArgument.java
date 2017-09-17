package com.weirddev.testme.intellij.template.context;

/**
 * Date: 23/06/2017
 *
 * @author Yaron Yamin
 */
public class MethodCallArgument {
    private final String text;

    public MethodCallArgument(String text) {

        this.text = text;
    }

    public String getText() {
        return text;
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
