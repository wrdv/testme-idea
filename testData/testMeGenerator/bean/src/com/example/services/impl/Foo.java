package com.example.services.impl;

import com.example.beans.JavaBean;
import com.example.dependencies.ChildWithSetters;
import com.example.warriers.FooFighter;
import com.example.foes.Fire;

public class Foo{

    private FooFighter fooFighter;

    public JavaBean fight(Fire withFire, String foeName, JavaBean javaBean) {
        fooFighter.fight(withFire);
        return withFire.getJavaBean();
    }

    public ChildWithSetters fight(ChildWithSetters childWithSetters) {
        System.out.println(childWithSetters.toString());
        return childWithSetters;
    }
}
