package com.example.warriers.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fear;

/** Test input class*/
public class FooFighterImpl implements FooFighter {
    @Override
    public String fight(Fear urFear) {
        return "nofear";
    }
}
