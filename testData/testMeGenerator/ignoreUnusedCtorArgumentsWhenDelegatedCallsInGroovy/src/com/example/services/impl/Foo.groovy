package com.example.services.impl

import com.example.beans.BeanByCtor
import com.example.beans.BigBean
import com.example.foes.Fire
import com.example.groovies.ImGroovy
import com.example.warriers.FooFighter

class Foo {

    private FooFighter fooFighter

    DelegateCtor findWithDelegatedCalls(List<BeanByCtor> beans, ImGroovy theOne) {
        println theOne
        println beans.toString()
        buildBean(beans, theOne)
    }

    private DelegateCtor buildBean(List<BeanByCtor> beans, ImGroovy theOne) {
        BigBean bigBean = new BigBean(beans[0].getIce().toString())
        bigBean.setTimes(new Many(beans[0].getMyName(),theOne.getGroove().getSomeString(),null))
        DelegateCtor delegateCtor = new DelegateCtor(beans[0].getMyName(), theOne.getGroove().getSomeString(), null,new Fire())
        delegateCtor
    }
}
