package com.example.services.impl

import com.example.beans.JavaBean
import com.example.foes.Ice

/**
 * Created by Admin on 03/03/2017.
 */
class Foo {
    String myName
    Ice ice
    Collection<JavaBean> myBeans

    String smashing(FooBro fooBro,JavaBean javaBean){
        println fooBro.iCanBeAccessedDirectly
        callMe(fooBro, javaBean)
        javaBean.getFire()
        return javaBean.ice.toString()
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
