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

    private TechFighter techFighter;

    @Inject
    private Supplier<Integer> result;

    private Logger logger;

    public String fight(Fire withFire,String foeName) {
        logger.trace("this logger should not be mocked in dependency injection");
        techFighter.initSelfArming("gun");
        String fail = techFighter.fight(withFire);
        ConvertedBean convertedBean = techFighter.surrender(new Fear(), new Ice(), 666);
        convertedBean.setSomeNum(result.get());
        return "returning response from dependency "+ fail + " " + convertedBean.getMyString();
    }

    public void setTechFighter(TechFighter techFighter) {
        this.techFighter = techFighter;
    }

}
