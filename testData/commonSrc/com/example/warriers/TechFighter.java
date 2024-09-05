package com.example.warriers;

import com.example.beans.ConvertedBean;
import com.example.foes.Fear;
import com.example.foes.Fire;
import com.example.foes.Ice;

/** Test input class*/
public interface TechFighter {
    void initSelfArming(String weapon)
    String fight(Fire withFire);
    ConvertedBean surrender(Fear fear, Ice ice,int times);
}