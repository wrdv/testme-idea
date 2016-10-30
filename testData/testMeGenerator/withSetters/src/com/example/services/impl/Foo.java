package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.warriers.Fire;
<caret>
public class Foo{

    private FooFighter fooFighter;
    private FooFighter fooFighterProperty;

    public String fight(Fire withFire,String foeName) {
        return fooFighter.fight(withFire);
    }

    public void setFooFighter(FooFighter fooFighter){
        this.fooFighter=fooFighter;
    }
    public void setFooFighterProperty(FooFighter fooFighterProperty){
        this.fooFighterProperty=fooFighterProperty;
    }
    public FooFighter  getFooFighterProperty(){
        return fooFighterProperty;
    }
}
