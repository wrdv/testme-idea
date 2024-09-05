package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
import com.example.beans.JavaBean;

public class Foo{

    private final String myString;
    private FooFighter[] fooFighter;
    private String myHero;
    private String thereGoes;

    Foo(String thereGoes,String myHero,JavaBean javaBean) {
        this.myHero = myHero;
        this.thereGoes = thereGoes;
        System.out.println(javaBean);
        myString = javaBean.getMyString();
    }

    public String fight(Fire[] withFire,String[] foeName, int[  ] times) {
        System.out.println(myString);
        return thereGoes+" "+myHero+" Watch him as he goes";
    }

}
