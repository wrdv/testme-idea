package com.example.groovies

import com.example.beans.JavaBean
import com.example.foes.Ice

/**
 * Created by Admin on 03/03/2017.
 */
class ImGroovyWithTupleCtor {
    String myName
    Ice ice
    Collection<JavaBean> myBeans

    String smashing(String somePumpkins,char with,Ice picker){
        somePumpkins
    }

    ImGroovyWithTupleCtor(String myName, Ice ice, Collection<JavaBean> myBeans) {
        this.myName = myName
        this.ice = ice
        this.myBeans = myBeans
    }

    ImGroovyWithTupleCtor() {
        myName="Slim Shady"
    }

    String getMyName() {
        return myName
    }

    void setMyName(String myName) {
        this.myName = myName
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

        ImGroovyWithTupleCtor imGroovy = (ImGroovyWithTupleCtor) o

        if (myName != imGroovy.myName) return false

        return true
    }

    int hashCode() {
        return (myName != null ? myName.hashCode() : 0)
    }
}
