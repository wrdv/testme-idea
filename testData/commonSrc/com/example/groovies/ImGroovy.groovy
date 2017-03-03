package com.example.groovies

import com.example.beans.JavaBean
import com.example.foes.Ice

/**
 * Created by Admin on 03/03/2017.
 */
class ImGroovy {
    String myName
    Ice ice
    Collection<JavaBean> myBeans

    ImGroovy() {
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

    @Override
    public String toString() {
        return "ImGroovy{" +
                "myName='" + myName + '\'' +
                ", ice=" + ice +
                '}';
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        ImGroovy imGroovy = (ImGroovy) o

        if (myName != imGroovy.myName) return false

        return true
    }

    int hashCode() {
        return (myName != null ? myName.hashCode() : 0)
    }
}
