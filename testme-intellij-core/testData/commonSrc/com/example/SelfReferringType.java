package com.example;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 10/31/2017.
 */

public enum SelfReferringType {
    ONE,
    TWO,
    THREE;

    private static List<SelfReferringType> firstTwo = Arrays.asList(ONE,TWO);

    public List<SelfReferringType> getFirstTwo() {
        return firstTwo;
    }
}
