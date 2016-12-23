package com.example.warriers.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;

/** Test input class*/
public class FooFighterImpl implements FooFighter {
    @Override
    public String fight(Fire withFire) {
        return "flames";
    }
}
