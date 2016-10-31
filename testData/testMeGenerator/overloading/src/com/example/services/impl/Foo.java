package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.weapons.Fire;
import com.example.weapons.Ice;
<caret>
public class Foo{

    private FooFighter fooFighter;

    public String fight(Fire withFire,String foeName) {
        return fooFighter.fight(withFire);
    }

    String fight(Fire withFire,Ice andIce ,String foeName) {
        System.out.println(andIce+" ice ice baby");
        return fooFighter.fight(withFire);
    }

}
