package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;

public class Foo{

    public static final String CONSTANTINUS = "CONSTANTINUS";
    public final int COUNTDOWN= 10;
    public int ONE= 1;
    public String MUTABLE = "MUTABLE";
    public String MUTABLE_INITIALIZED_IN_CTOR;
    public String MUTABLE_INITIALIZED_IN_DEFAULT_CTOR;

    public Foo(String MUTABLE_INITIALIZED_IN_CTOR) {
        this.MUTABLE_INITIALIZED_IN_CTOR = MUTABLE_INITIALIZED_IN_CTOR;
    }
    public Foo() {
        this.MUTABLE_INITIALIZED_IN_DEFAULT_CTOR = "Initialized";
    }

    private FooFighter fooFighter;

    public String fight(Fire withFire,String foeName) {
        return fooFighter.fight(withFire);
    }

}
