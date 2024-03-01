package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
<caret>
public class Foo {

    private static final FooFighter fooFighter = new FearFighterImpl();

    public static String fight(Fire withFire,String foeName) {
        return foeName;
    }
    
    private Foo() {

    }

}
