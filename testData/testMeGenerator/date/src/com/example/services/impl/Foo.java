package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Instant;
import java.util.Date;

public class Foo{

    private FooFighter fooFighter;

    public Date fight(Fire withFire,Date atDawn) {
        fooFighter.fight(withFire);
        return atDawn;
    }
    public LocalDate fightAnyDay(Fire withFire, LocalDate dDay) {
        fooFighter.fight(withFire);
        return dDay;
    }
    public LocalTime fightAnyTime(Fire withFire, LocalTime now) {
        fooFighter.fight(withFire);
        return now;
    }
    public LocalDateTime fightAnyDate(Fire withFire, LocalDateTime when) {
        fooFighter.fight(withFire);
        return when;
    }
    public Instant maybe(Instant later,Fire fire) {
        fooFighter.fight(fire);
        return later;
    }
}
