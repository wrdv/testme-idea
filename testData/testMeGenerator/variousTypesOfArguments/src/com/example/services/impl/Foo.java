package com.example.services.impl;

import com.example.warriers.FooFighter;
import com.example.foes.Fire;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;

public class Foo{

    private FooFighter fooFighter;
    private final byte byteField;
    private final int intField;
    private final long longField;
    private final float floatField;
    private final double doubleField;
    private final boolean booleanField;
    private final char charField;
    private final short shortField;
    private final InnerStaticClass innerStaticClass;

    public Foo(FooFighter fooFighter,byte byteField,
               int intField,
               long longField,
               float floatField,
               double doubleField,
               boolean booleanField,
               char charField,
               short shortField,
               InnerStaticClass innerStaticClass) {
        this.fooFighter = fooFighter;
        this.byteField = byteField;
        this.intField = intField;
        this.longField = longField;
        this.floatField = floatField;
        this.doubleField = doubleField;
        this.booleanField = booleanField;
        this.charField = charField;
        this.shortField = shortField;
        this.innerStaticClass = innerStaticClass;
    }
    public String variousArgs(Fire withFire, String foeName, Byte byteFieldWrapper,
                              Integer intFieldWrapper,
                              Long longFieldWrapper,
                              Float floatFieldWrapper,
                              Double doubleFieldWrapper,
                              Boolean booleanFieldWrapper,
                              Character charFieldWrapper,
                              Short shortFieldWrapper,
                              BigDecimal bigDecimal,
                              BigInteger bigInt,
                              Date date,
                              Instant instant) {
        System.out.println("Running and using primitive Wrappers:" + byteFieldWrapper + shortFieldWrapper + intFieldWrapper + longFieldWrapper + floatFieldWrapper + doubleFieldWrapper + charFieldWrapper + booleanFieldWrapper);
        innerStaticClass.methodOfInnerClass();
        return fooFighter.fight(withFire);
    }


    public static class InnerStaticClass{
        public void methodOfInnerClass(){

        }
    }
}
//<caret>