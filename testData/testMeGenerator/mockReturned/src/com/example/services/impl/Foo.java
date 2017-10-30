package com.example.services.impl;

import com.example.beans.ConvertedBean;
import com.example.foes.Fear;
import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.warriers.FooFighter;

public class Foo{

    private FooFighter fooFighter;

    public String fight(Fire withFire,String foeName) {
        System.out.println(withFire);
        System.out.println(foeName);
        ConvertedBean convertedBean = fooFighter.surrender(new Fear(), new Ice(), 666);
        System.out.println(convertedBean.getFear());
        System.out.println(convertedBean.getIce());
        System.out.println(convertedBean.getSomeNum());
        return "returning response from dependency "+ convertedBean.getMyString();;
    }
}
