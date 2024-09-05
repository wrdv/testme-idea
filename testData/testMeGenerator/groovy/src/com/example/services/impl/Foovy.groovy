package com.example.services.impl

import com.example.beans.JavaBean
import com.example.foes.Ice

/**
 * Created by Admin on 03/03/2017.
 */
class Foovy {
    String myName
    Ice ice
    Collection<JavaBean> myBeans

    String smashing(String somePumpkins,char with,Ice picker){
        somePumpkins
    }

    Foovy() {
        myName="Slim Shady"
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
