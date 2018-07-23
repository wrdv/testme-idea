package com.example.parents.impl;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yaron.yamin
 * Date: 7/22/2018
 * Time: 4:22 PM
 */
public abstract class AbstactFoo<T>
{
    public T getMeMyTee(T tee){
        return tee;
    }
    public List<T> getMeMyTees(T tee){
        return Collections.singletonList(tee);
    }

}
