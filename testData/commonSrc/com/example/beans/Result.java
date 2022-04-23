package com.example.beans;

public class Result<T> {
    private T result;

    public Result(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }
}
