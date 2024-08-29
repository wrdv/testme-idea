package com.example.services.impl;

import com.example.beans.ConvertedBean;
import com.example.beans.JavaBean;
import com.example.foes.Fire;

/**
 * Created by Admin on 21/04/2017.
 */
public class FooConverterAbs {
    public ConvertedBean convert(Fire withFire, String foeName, JavaBean javaBean) {
        ConvertedBean convertedBean = new ConvertedBean();
        convertedBean.setFear(javaBean.getFear());
        return convertedBean;
    }
}
