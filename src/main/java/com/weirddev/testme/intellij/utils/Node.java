package com.weirddev.testme.intellij.utils;

/**
 * Date: 05/03/2017
 *
 * @author Yaron Yamin
 */
public class Node<T> {
    final private T data;
    final private Node<T> parent;
    final private int depth;

    public Node(T data, Node<T> parent, int depth) {
        this.data = data;
        this.parent = parent;
        this.depth = depth;
    }

    public T getData() {
        return data;
    }

    public Node<T> getParent() {
        return parent;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isAncestor(T data) {
        if (parent == null || parent.data==null) {
            return false;
        } else{
            return parent.data.equals(data) || parent.isAncestor(data);
        }
    }
}
