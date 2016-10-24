package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.weapons.Fire;
<caret>
public class Foo{

    private FooFighter fooFighter;

    public String fight(Fire withFire,String someFoe) {
        return fooFighter.fight(withFire);
    }

}
