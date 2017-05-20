package com.example.services.impl;

import com.example.beans.JavaBean;
import com.example.foes.Fire;
import com.example.groovies.ImGroovy;
import com.example.beans.SettersOverCtors;
import com.example.warriers.FooFighter;

import java.util.Collection;

public class Foo {

    private FooFighter fooFighter;

    public Collection<SettersOverCtors> find(Collection<SettersOverCtors> beans, ImGroovy theOne) {
        System.out.println(theOne);
        System.out.println(beans.toString());
        return beans;
    }

}
