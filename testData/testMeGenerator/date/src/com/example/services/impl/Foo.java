package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
import java.util.Date;
<caret>
public class Foo{

    private FooFighter fooFighter;

    public Date fight(Fire withFire,Date atDawn) {
        fooFighter.fight(withFire);
        return atDawn;
    }

}
