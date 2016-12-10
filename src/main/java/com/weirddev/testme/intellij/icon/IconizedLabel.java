package com.weirddev.testme.intellij.icon;

import javax.swing.*;

/**
 * Date: 10/15/2016
 *
 * @author Yaron Yamin
 */
public class IconizedLabel {
    private String text;
    private Icon icon;

    public IconizedLabel(String text, Icon icon) {
        this.text = text;
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IconizedLabel)) return false;

        IconizedLabel that = (IconizedLabel) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return icon != null ? icon.equals(that.icon) : that.icon == null;

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IconizedLabel{" +
                "text='" + text + '\'' +
                ", icon=" + icon +
                '}';
    }
}
