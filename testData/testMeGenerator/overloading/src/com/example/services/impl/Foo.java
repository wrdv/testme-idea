package com.example.services.impl;

import com.example.foes.Pokemon;
import com.example.warriers.FooFighter;
import com.example.foes.Fire;
import com.example.foes.Ice;

public class Foo{

    private FooFighter fooFighter;

    public String fight(Fire withFire,String foeName) {
        return fooFighter.fight(withFire);
    }

    String fight(Fire withFire,Ice andIce ,String foeName) {
        System.out.println(andIce+" ice ice baby");
        return fooFighter.fight(withFire);
    }
    String fight(Fire withFire,Ice andIce ,Ice onTheRocks ,String foeName) {
        System.out.println("french "+andIce);
        return fooFighter.fight(withFire);
    }

    private Pokemon poke(int times) {
        return new Pokemon();
    }
    protected String fight(int times) {
        return String.valueOf(times);
    }
    String fold(String foeName) {
        System.out.println(foeName+" friends?");
        return "truce";
    }
    String fold(String foeName,String truce) {
        System.out.println(foeName+" "+truce);
        return "white flag";
    }

}
