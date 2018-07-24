package com.example.services.impl;

import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.parents.impl.AbstactFoo;

import java.util.Arrays;
import java.util.List;

public class Foo extends AbstactFoo<Fire,Ice>
{
    @Override
    public Fire getMeMyTee(Fire tee) {
        return tee;
    }
}
