package com.example.services.impl;
public enum Foo {
    One("i"),
    Two("ii");

    private final String value;

    EnumClass(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
