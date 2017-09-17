package com.example.services.impl;

import com.example.beans.ConvertedBean;
import com.example.beans.InheritingBean;
import com.example.beans.JavaBean;
import com.example.dependencies.ChildWithSetters;
import com.example.foes.Ice;
import com.example.util.FooUtils;
import com.example.warriers.FooFighter;
import com.example.foes.Fire;

public class Foo extends FooConverterAbs{

    private FooFighter fooFighter;

    private static int badPractiseRecursionCounter =20;

    public ConvertedBean convert(Fire withFire, String foeName, InheritingBean inheritingBean) {
        ConvertedBean convertedBean = super.convert(withFire, foeName, inheritingBean);
        fooFighter.fight(withFire);
        convertedBean.setIce(inheritingBean.getIce());
        convertedBean.setMyDate(inheritingBean.getMyDate());
        convertedBean.setMyString(inheritingBean.getMyString());
        convertedBean.setSomeBinaryOption(inheritingBean.isSomeBinaryOption());
        JavaBean javaBean = withFire.getJavaBean();
        convertedBean.setSomeNum(inheritingBean.getSomeNum());
        System.out.println(inheritingBean.getFire());
        return convertedBean;
    }
}
