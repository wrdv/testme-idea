package com.example.services.impl;

import com.example.beans.BeanThere;
import com.example.beans.BigBean;
import com.example.dependencies.MasterInterface;
import com.example.foes.Ace;
import com.example.foes.BeanDependsOnInterface;
import com.example.foes.Ice;
import com.example.warriers.FooFighter;
import com.example.foes.Fire;

import java.util.List;

public class Foo{

    private FooFighter fooFighter;

    public BigBean fight(Fire withFire, List<String> foeName, BigBean bigBean, BeanThere beanThere) {
        System.out.println(fooFighter.fight(withFire));
        return bigBean;
    }
    public Ice[] varargs(Fire fire, Ace ace, Ice... onTheRocks) {
        System.out.println(fire);
        System.out.println(onTheRocks.length);
        return onTheRocks;
    }
    public MasterInterface jack(BeanDependsOnInterface andTheBeanstalk,MasterInterface masterInterface) {
        return masterInterface;
    }
}
