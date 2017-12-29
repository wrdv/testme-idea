package com.weirddev.testme.intellij.resolvers.to;

/**
 * Date: 23/06/2017
 *
 * @author Yaron Yamin
 */
public class MethodCallArg {
    private final String text;

    public MethodCallArg(String text) {

        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodCallArg)) return false;

        MethodCallArg that = (MethodCallArg) o;

        return text != null ? text.equals(that.text) : that.text == null;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }
}
