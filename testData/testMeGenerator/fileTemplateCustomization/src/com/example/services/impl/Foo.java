package com.example.services.impl;

import com.example.beans.ConvertedBean;
import java.util.function.Supplier;
import com.example.dependencies.Logger;
import com.example.foes.Fear;
import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.warriers.TechFighter;
import com.example.dependencies.Logger;
import javax.inject.Singleton;
import javax.inject.Inject;

@Singleton
public class Foo{

    @Inject
    private TechFighter techFighter;

    @Inject
    private Supplier<Integer> result;

    private Logger logger;

    public String fight(Fire withFire,String foeName) {
        logger.trace("logger should not mocked when it not selected to mock");
        logger.trace("this method should be tested when it selected to test");
        techFighter.initSelfArming("gun");
        String fail = techFighter.fight(withFire);
        ConvertedBean convertedBean = techFighter.surrender(new Fear(), new Ice(), 666);
        convertedBean.setSomeNum(result.get());
        return "returning response from dependency "+ fail + " " + convertedBean.getMyString();
    }

    public String fightSuccess(Fire withFire) {
        logger.trace("this method should not be tested when it not selected to test");
        String success = techFighter.fight(withFire);
        return "returning response from dependency " + success;
    }
}
