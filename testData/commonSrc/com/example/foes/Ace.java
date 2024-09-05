package com.example.foes;

import java.util.Arrays;

/**
 * Created by Admin on 07/03/2017.
 */
public class Ace {

    private final Ice[] ofSpades;

    public Ace(Ice... ofSpades){
        this.ofSpades = ofSpades;
    }

    @Override
    public String toString() {
        return "Ace{" +
                "ofSpades=" + Arrays.toString(ofSpades) +
                '}';
    }
}
