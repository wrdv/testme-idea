package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;
<caret>
public class Foo{

    private FooFighter fooFighter;
    public int intField=5;
    protected double doubleField=11.22;
    long longField;
    char c;
    byte byteField;
    boolean boolField;
    float floatField;
    short shortField;

    public Integer intWrapperField=5;
    protected Double doubleWrapperField=11.22;
    Long longWrapperField;
    Character charWrapperField;
    Byte byteWrapperField;
    Boolean boolWrapperField;
    Float floatWrapperField;
    Short shortWrapperField;

    static String staticField;
    final String finalField="Im Final";

    public String fight(Fire withFire,String foeName) {
        double sum = intField + doubleField + intWrapperField + doubleWrapperField;
        System.out.println("sum:"+sum);
        return fooFighter.fight(withFire);
    }

}
