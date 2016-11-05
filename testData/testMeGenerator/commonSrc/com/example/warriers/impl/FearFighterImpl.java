package com.example.warriers.impl;

import com.example.warriers.FearFighter;
import com.example.foes.Fear;

/** Test input class*/
public class FearFighterImpl implements FearFighter {
    @Override
    public String fight(Fear urFear) {
        return "nofear";
    }
}
