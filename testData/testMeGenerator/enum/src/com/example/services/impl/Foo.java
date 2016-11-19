package com.example.services.impl;

import com.example.services.FooType;
import com.example.services.Result;
import com.example.warriers.FooFighter;
import com.example.foes.Fire;

public class Foo{
<caret>
    private FooFighter fooFighter;

    Result result=Result.Draw;

    public Result fight(Fire withFire,FooType fooType) {
        System.out.println(fooType);
        fooFighter.fight(withFire);
        return Result.WinWin;

    }
}
