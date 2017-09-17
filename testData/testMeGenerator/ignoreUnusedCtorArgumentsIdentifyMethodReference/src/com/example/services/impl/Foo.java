package com.example.services.impl;

import com.example.beans.BeanByCtor;
import com.example.beans.JavaBean;
import com.example.beans.BigBean;
import com.example.beans.SettersOverCtors;
import com.example.foes.Fire;
import com.example.groovies.ImGroovy;
import com.example.groovies.ImGroovyWithTupleCtor;
import com.example.warriers.FooFighter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Foo {

    private FooFighter fooFighter;

    public BigBean find(List<BeanByCtor> beans, ImGroovy theOne) {
        System.out.println(theOne);
        System.out.println(beans.toString());
        final BigBean bigBean = new BigBean(beans.stream().map(BeanByCtor::getIce).collect(Collectors.toList()).get(0).toString());
        bigBean.setTimes(new Many(beans.get(0).getMyName(),theOne.getGroove().getSomeString(),null));
        return bigBean;
    }

    public Many useGroovyTypeWithCtor(ImGroovyWithTupleCtor groovyTypeArg){
        Many many = new Many(groovyTypeArg.getMyName());
        many.setMembers(groovyTypeArg.getMyBeans().size()+"");
        return many;
    }
    public ImGroovyWithTupleCtor returnGroovyType(ImGroovy grArg){
        ImGroovyWithTupleCtor returnVal=new ImGroovyWithTupleCtor(null,grArg.getIce(),grArg.getMyBeans());
        return returnVal;
    }
}
