package com.example.warriers.impl;

import com.example.beans.ConvertedBean;
import com.example.foes.Fear;
import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.warriers.TechFighter;

/** Test input class*/
public class TechFighterImpl implements TechFighter {

    private String weapon;

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

    @Override
    public void initSelfArming(String weapon) {
        this.weapon = weapon;
    }

}
