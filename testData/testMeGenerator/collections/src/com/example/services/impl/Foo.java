package com.example.services.impl;

import com.example.foes.Fear;
import com.example.foes.Ice;
import com.example.warriers.FooFighter;
import com.example.foes.Fire;

import java.util.*;

public class Foo{

    private List<FooFighter> fooFighter;

    public List<Fire> fight(List<Fire> fires, Set<Fire> flames, Map<String, Ice> icebergs, Collection<String> strings,Collection<List<String>> collOfLists, Queue<List<Fear>> mindTheGap, Deque<Fear> deque) {
        System.out.println("" + fires.get(0) + flames.iterator().next() + icebergs.get("") + strings.iterator().next() + collOfLists.iterator().next().iterator().next() +mindTheGap.poll()+deque.peek());
        return fires;
    }
    public List<Fire> fightConcreteTypes(ArrayList<Fire> fires, HashSet<Fire> flames, HashMap<String, Ice> icebergs, Vector<List<String>> collOfLists, TreeSet<Fear> mindTheGap, Stack<Fear> stack) {
        System.out.println("" + fires.get(0) + flames.iterator().next() + icebergs.get("")+ collOfLists.iterator().next().iterator().next() +mindTheGap.pollFirst()+stack.peek());
        return fires;
    }

    public List typeless(List fires, Set flames, Map icebergs, Collection strings,Collection<List> collOfLists, Queue<List> mindTheGap) {
        System.out.println("" + fires.get(0) + flames.iterator().next() + icebergs.get("") + strings.iterator().next() + collOfLists.iterator().next().iterator().next() +mindTheGap.poll());
        return fires;
    }

    public NavigableMap<String, Fire> miscColls(NavigableMap<String, Fire> spitfires, NavigableSet<Fire> fires, RandomAccess random, SortedSet<Fire> flames) {
        System.out.println("" + spitfires.get("string") + fires.iterator().next() + random + flames.iterator().next());
        return spitfires;
    }

}
