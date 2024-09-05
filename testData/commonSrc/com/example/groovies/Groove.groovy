package com.example.groovies

import com.example.beans.JavaBean
import com.example.foes.Ice

/**
 * Created by Admin on 03/03/2017.
 */
class Groove {
    String someString
    Ice ice

    void setSomeString(String someString) {
        this.someString = someString
    }

    void setIce(Ice ice) {
        this.ice = ice
    }

    @Override
    public String toString() {
        return "Groove{" +
                "someString='" + someString + '\'' +
                ", ice=" + ice +
                '}';
    }
}
