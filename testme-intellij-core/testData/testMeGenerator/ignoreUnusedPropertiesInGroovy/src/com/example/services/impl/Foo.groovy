package com.example.services.impl

import com.example.beans.JavaBean
import com.example.foes.Ice
import com.example.beans.ConvertedBean

/**
 * Created by Admin on 03/03/2017.
 */
class Foo {
    String myName
    Ice ice
    Collection<JavaBean> myBeans

    String smashing(FooBro fooBro,JavaBean javaBean){
//        println fooBro.iCanBeAccessedDirectly //todo - problematic use case, when uncommented, iCanBeAccessedDirectly property should be set in map constructor of generated groovy test, but property access is not identified , probably because groovy compiler generated getiCanBeAccessedDirectly() instead of getICanBeAccessedDirectly()
        println fooBro.antoherDirectlyAccessedInt
        callMe(fooBro, javaBean)
        javaBean   .   getFire()
        return javaBean  /*comment should be ignored */  .   ice  .  toString()
    }

    ConvertedBean convertWhileSettingPropsInline(FooBro fooBro,JavaBean javaBean){
        ConvertedBean convertedBean = new ConvertedBean(ice:javaBean.getIce(),myDate:javaBean.getMyDate(),myString:javaBean.getMyString(),someBinaryOption:javaBean.isSomeBinaryOption(),someNum:javaBean.getSomeNum())
        println fooBro.getAnotherProp()
        System.out.println(javaBean.getFire())
        return convertedBean
    }

    private void callMe(FooBro fooBro, JavaBean javaBean) {
        println fooBro.propInSamePackage
        println javaBean.fear
    }

    String getMyName() {
        return myName
    }

    void setMyName(String myName) {
        this.myName = myName
    }

    Ice getIce() {
        return ice
    }

    void setIce(Ice ice) {
        this.ice = ice
    }

    Collection<JavaBean> getMyBeans() {
        return myBeans
    }

    void setMyBeans(Collection<JavaBean> myBeans) {
        this.myBeans = myBeans
    }

}
