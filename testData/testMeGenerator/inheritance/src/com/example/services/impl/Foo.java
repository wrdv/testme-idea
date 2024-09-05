package com.example.services.impl;

import com.example.parents.impl.FooParent;
import com.example.parents.impl.FooInterfaceExt;
import com.example.warriers.FooFighter;
import com.example.foes.Fire;

public class Foo extends FooParent implements FooInterfaceExt{

    private FooFighter fooFighter;

    public String fight(Fire withFire,String foeName) {
        return fooFighter.fight(withFire);
    }

    void iDefault(){
        System.out.println("iDefault");
    }
}
