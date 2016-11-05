package com.example.services.impl;


import com.example.warriers.FooFighter;
import com.example.foes.Fire;


public class Foo{

    private FooFighter fooFighter;

    public String fight(Fire fireOfFoe, com.example.hole.Fire inTheHole) {
        System.out.println(inTheHole);
        return fooFighter.fight(fireOfFoe);
    }
}