package com.example.warriers.impl;

import com.example.foes.Pokemon;

/** Test input class*/
public class HunterImpl implements Hunter {
    @Override
    DeadOrAlive hunt(Pokemon pokey) {
        return new DeadOrAlive();
    }
}
