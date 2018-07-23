package com.example.services.impl;

import com.example.foes.Fire;
import com.example.parents.impl.AbstactFoo;

import java.util.Arrays;
import java.util.List;

public class Foo extends AbstactFoo<Fire>
{
    @Override
    public List<Fire> getMeMyTees(Fire tee) {
        return Arrays.asList(tee,tee);
    }
}
