package com.example.services.impl;

import com.example.foes.Fire;
import com.example.warriers.FooFighter;

import java.math.BigDecimal;

public class Foo{

    private FooFighter fooFighter;

    public String fight(Fire withFire,String foeName,
                        byte byteParam,
                        short shortParam,
                        int intParam,
                        long longParam,
                        float floatParam,
                        double doubleParam,
                        char charParam,
                        boolean booleanParam,
                        Byte byteParamWrapper,
                        Short shortParamWrapper,
                        Integer intParamWrapper,
                        Long longParamWrapper,
                        Float floatParamWrapper,
                        Double doubleParamWrapper,
                        Character charParamWrapper,
                        Boolean booleanParamWrapper,
                        BigDecimal bigDecimalParam
    ) {
        return fooFighter.fight(withFire);
    }
}
