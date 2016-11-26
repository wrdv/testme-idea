package com.example.services.impl;

import com.example.beans.BeanThere;
import com.example.beans.BigBean;
import com.example.warriers.FooFighter;
import com.example.foes.Fire;

import java.util.List;

public class Foo{

    private FooFighter fooFighter;

    public BigBean fight(Fire withFire, List<String> foeName, BigBean bigBean, BeanThere beanThere) {
        System.out.println(fooFighter.fight(withFire));
        return bigBean;
    }
}
