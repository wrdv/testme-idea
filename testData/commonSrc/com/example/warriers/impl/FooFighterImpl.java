package com.example.warriers.impl;

import com.example.beans.ConvertedBean;
import com.example.foes.Fear;
import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.warriers.FooFighter;

/** Test input class*/
public class FooFighterImpl implements FooFighter {
    @Override
    public String fight(Fire withFire) {
        return "flames";
    }

    @Override
    public ConvertedBean surrender(Fear fear, Ice ice,int times) {
        System.out.println("times:" + times);
        System.out.println("fear:" + fear.toString());
        return new ConvertedBean();
    }
}
