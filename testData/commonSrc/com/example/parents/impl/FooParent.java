package com.example.parents.impl;

import com.example.foes.Fire;
import com.example.foes.Pokemon;
import com.example.warriers.FooFighter;

/**
 * Date: 06/11/2016
 * @author Yaron Yamin
 */
public class FooParent implements FooInterface{

    private Pokemon pokey;

    private FooFighter fooFighter;

    public String fight(Fire withFire,String foeName) {
        return "kids, enough is enough!";
    }
    protected String fight(String foeName) {
        return "who started?";
    }

    private String truce(String flag){
        return "white " + flag;
    }

    void peace(){
        System.out.println("No more bloodshed");
    }

    @Override
    public String asFather(String asSon) {
        return asSon;
    }
}
