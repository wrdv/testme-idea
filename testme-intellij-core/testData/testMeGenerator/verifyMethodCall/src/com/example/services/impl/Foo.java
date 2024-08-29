package com.example.services.impl;

import com.example.beans.ConvertedBean;
import java.util.function.Supplier;
import com.example.dependencies.Logger;
import com.example.foes.Fear;
import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.warriers.TechFighter;

public class Foo{

    private TechFighter techFighter;
    private Supplier<Integer> result;

    public String fight(Fire withFire,String foeName) {
        techFighter.initSelfArming("gun");
        String fail = techFighter.fight(withFire);
        ConvertedBean convertedBean = techFighter.surrender(new Fear(), new Ice(), 666);
        convertedBean.setSomeNum(result.get());
        return "returning response from dependency "+ fail + " " + convertedBean.getMyString();
    }
}
