package com.example.parents.impl;

import com.example.foes.Fire;

/**
 * Date: 06/11/2016
 * @author Yaron Yamin
 */
public interface FooInterfaceExt extends FooInterface{

    String fight(Fire withFire, String foeName);
    String asFather(String asSon);

}
