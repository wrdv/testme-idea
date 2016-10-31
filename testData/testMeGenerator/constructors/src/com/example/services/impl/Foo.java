package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
<caret>
public class Foo{

    private FooFighter fooFighter;

    private String numFive="55555";

    public String fight(Fire withFire,String foeName) {
        System.out.println("I am number "+numFive.length());
        return fooFighter.fight(withFire);
    }

    public Foo(FooFighter fooFighter,String numFive){
        this.fooFighter=fooFighter;
        this.numFive=numFive;
    }
    public Foo(FooFighter fooFighter){
        this.fooFighter=fooFighter;
        this.numFive=numFive;
    }

}
