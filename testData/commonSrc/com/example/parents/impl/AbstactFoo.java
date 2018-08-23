package com.example.parents.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yaron.yamin
 * Date: 7/22/2018
 * Time: 4:22 PM
 */
public abstract class AbstactFoo<T,U>
{
    public Map<U,T> u2(){
        return new HashMap<U, T>();
    }
    public void iNeedU(U u){
        System.out.println("Thank "+u);
    }
    public String iNeedUT(U u,T t){
        return u.toString() + toString();
    }
    public T getMeMyTee(T tee){
        return tee;
    }
    public List<T> getMeMyTees(T tee){
        return Collections.singletonList(tee);
    }

}
