package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.warriers.Fear;
<caret>
public class Foo{

    private FooFighter fooFighter;
    private FooFighter fooFighterProperty;

    public String fight(Fear urFear,String foeName) {
        return fooFighter.fight(urFear);
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
