package com.example.services.impl;

import com.example.beans.ConvertedBean;
import com.example.beans.JavaBean;
import com.example.dependencies.ChildWithSetters;
import com.example.foes.Ice;
import com.example.util.FooUtils;
import com.example.warriers.FooFighter;
import com.example.foes.Fire;

public class Foo extends FooConverterAbs{

    private FooFighter fooFighter;

    private static int badPractiseRecursionCounter =20;

    public ConvertedBean convert(Fire withFire, String foeName, JavaBean javaBean) {
        ConvertedBean convertedBean = super.convert(withFire, foeName, javaBean);
        fooFighter.fight(withFire);
        convertedBean.setIce(javaBean.getIce());
        convertedBean.setMyDate(javaBean.getMyDate());
        convertedBean.setMyString(javaBean.getMyString());
        convertedBean.setSomeBinaryOption(javaBean.isSomeBinaryOption());
        JavaBean javaBean1 = withFire.getJavaBean();
        convertedBean.setSomeNum(javaBean.getSomeNum());
        System.out.println(javaBean.getFire());
        return convertedBean;
    }

    public ChildWithSetters callOthers(ChildWithSetters childWithSetters) {
        System.out.println(childWithSetters.toString());
        System.out.println(childWithSetters.getStrField());
        privateMethod(childWithSetters);
        recursive(childWithSetters.getFire());
        childWithSetters.setStrField("asdfasdf");
        Long calculated = new InnerCalculator(childWithSetters.getFire().getJavaBean()).calc(childWithSetters.getFire().getJavaBean().isSomeBinaryOption());
        System.out.println(calculated);
        System.out.println(FooUtils.callPooh(childWithSetters.getFire().getJavaBean()));
        return childWithSetters;
    }

    private void recursive(FireBall fire) {
        JavaBean javaBean = fire.getJavaBean();
        Ice ice = javaBean.getIce();
        if (badPractiseRecursionCounter > 0) {
            badPractiseRecursionCounter--;
            recursive(fire);
        }
    }

    private void privateMethod(ChildWithSetters childWithSetters) {
        System.out.println(childWithSetters.getSomeNumber());
        System.out.println(childWithSetters.getParentProp());
    }
    public static class InnerCalculator{
        private JavaBean javaBean;

        public InnerCalculator(JavaBean javaBean) {

            this.javaBean = javaBean;
        }

        Long calc(boolean someBinaryOption) {
            if (someBinaryOption) {
                return javaBean.getSomeLongerNum();
            }
            return (long) 666;
        }
    }
}
