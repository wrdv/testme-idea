package com.example.services.impl;

import com.example.beans.BeanByCtor;
import com.example.beans.BigBean;
import com.example.groovies.ImGroovy;
import com.example.warriers.FooFighter;
import com.example.foes.Fire;

import java.util.List;

public class Foo {

    private FooFighter fooFighter;

    public ContainNestedPropertyWithCtor buildWithNestedCtorCalls(List<BeanByCtor> beans, ImGroovy theOne) {
        System.out.println(theOne);
        System.out.println(beans.toString());
        return buildBean(beans, theOne);
    }

    private ContainNestedPropertyWithCtor buildBean(List<BeanByCtor> beans, ImGroovy theOne) {
        final BigBean bigBean = new BigBean(beans.get(0).getIce().toString());
        bigBean.setTimes(new Many(beans.get(0).getMyName(),theOne.getGroove().getSomeString(),null));
        DelegateCtor delegateCtor = new DelegateCtor(beans.get(0).getMyName(), theOne.getGroove().getSomeString(), null,new Fire());
        return new ContainNestedPropertyWithCtor(delegateCtor, beans.get(0).getMyName(), null);
    }
}
