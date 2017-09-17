package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
import com.example.warriers.impl.FinalCountdown;
import java.math.BigDecimal;

public class Foo{

    private FinalCountdown finalCountdown;

    public BigDecimal count(Integer start) {
        return finalCountdown.count(start);
    }

}
