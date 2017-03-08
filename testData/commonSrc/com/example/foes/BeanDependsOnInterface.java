package com.example.foes;

import com.example.dependencies.MasterInterface;
/**
 * Created by Admin on 07/03/2017.
 */
public class BeanDependsOnInterface {

    private final MasterInterface master;

    public BeanDependsOnInterface(MasterInterface master){
        this.master = master;
    }

    @Override
    public String BeanDependsOnInterface() {
        return "Ace{" +
                "master=" + master +
                '}';
    }
}
