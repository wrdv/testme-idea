package com.example.services.impl;

import com.example.foes.Ice;
import com.example.warriers.FooFighter;
import com.example.foes.Fire;

import java.util.*;

public class Foo{

    private List<FooFighter> fooFighter;

    public List<String> fight(List<Fire> fires, Set<Fire> flames, Map<String,Ice> icebergs,Collection<String> strings,Queue<List<Fear>> mindTheGap) {
        List<String> list = new ArrayList<String>();
        list.add(fooFighter.get(0).fight(fires.get(0)));
        return list;
    }

}
