package com.example.services.impl

import com.example.beans.BeanByCtor
import com.example.beans.JavaBean
import com.example.beans.BigBean
import com.example.beans.SettersOverCtors
import com.example.foes.Fire
import com.example.groovies.ImGroovy
import com.example.groovies.ImGroovyWithTupleCtor
import com.example.warriers.FooFighter

import java.util.Collection
import java.util.List

class Foo {

    FooFighter fooFighter

    BigBean find(List<BeanByCtor> beans, ImGroovy theOne) {
        println theOne
        System.out.println(beans.toString())
        BigBean bigBean = new BigBean(beans[0].getIce().toString())
        bigBean.setTimes(new Many(beans.get(0).getMyName(), theOne.getGroove().getSomeString(), null))
        bigBean
    }

    Many useGroovyTypeWithCtor(ImGroovyWithTupleCtor groovyTypeArg) {
        Many many = new Many(groovyTypeArg.getMyName())
        many.setMembers(groovyTypeArg.getMyBeans().size() + "")
        many
    }

    ImGroovyWithTupleCtor returnGroovyType(ImGroovy grArg) {
        ImGroovyWithTupleCtor returnVal = new ImGroovyWithTupleCtor(null, grArg.getIce(), grArg.getMyBeans())
        returnVal
    }
}
