package com.example.services.impl;

import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.foes.Pokemon;
import com.example.warriers.FooFighter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Foo{

    Set<Ice> escimoRealEstate;

    Map<Pokemon, List<Fire>> hotPokeys;

    private FooFighter fooFighter=new FooFighter() {
        @Override
        public String fight(Fire withFire) {
            return "unknown soldier";
        }
    };

    public String fight(ArrayList<Fire> withFire, String foeName) {
        return fooFighter.fight(withFire.get(0));
    }

    public void intoTheVoid(){
        System.out.println("Solitude");
    }
}
