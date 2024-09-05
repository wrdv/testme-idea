package com.example.services.impl;

import com.example.beans.ConvertedBean;
import java.util.function.Supplier;
import com.example.dependencies.Logger;
import com.example.foes.Fear;
import com.example.foes.Fire;
import com.example.foes.Ice;
import com.example.warriers.FooFighter;

public class Foo{

    private FooFighter fooFighter;
    private Supplier<Integer> result;
    private Logger logger = Logger.getLogger(Foo.class.getName());;

    public String fight(Fire withFire,String foeName) {
        logger.trace("this method does not return a value so it should not be stubbed");
        System.out.println(withFire);
        System.out.println(foeName);
        ConvertedBean convertedBean = fooFighter.surrender(new Fear(), new Ice(), 666);
        convertedBean.setSomeNum(result.get());
        System.out.println(convertedBean.getFear());
        System.out.println(convertedBean.getIce());
        System.out.println(convertedBean.getSomeNum());
        return "returning response from dependency "+ convertedBean.getMyString();
    }
}
