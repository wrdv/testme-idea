package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
<caret>
public class Foo{

    private FooFighter[] fooFighter;

    public String[] fight(Fire[] withFire,String[] foeName, int[  ] times) {
        return new String[]{fooFighter[0].fight(withFire[0])};
    }
    public Fire[] fireStarter(String[] foeName, int[  ] times) {
        return new Fire[]{new Fire()};
    }
    public int[] fireCounter(Fire[] fires) {
        return new int[]{fires.length};
    }

    public String routerSearch(String dstIp, String[][] ipTable){
        return ipTable[0][0];
    }
    public String a3dimSearch(String dstIp, String[][][] ipTable){
        return ipTable[0][0][0];
    }
}
