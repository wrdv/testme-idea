package com.example.parents.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
import com.example.foes.Pokemon;

/**
 * Date: 06/11/2016
 *
 * @author Yaron Yamin
 */
public class FooParent {

    private Pokemon pokey;

    private FooFighter fooFighter;

    public String fight(Fire withFire,String friendOrFoe) {
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

}
