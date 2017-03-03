package com.example.dependencies;

import com.example.foes.Fire;

/**
 * Created by Admin on 02/03/2017.
 */
public class ChildWithSetters extends AbstractParent{

    String strField;
    int someNumber;
    Fire fire;

    public ChildWithSetters() {
    }
    public ChildWithSetters(ChildWithSetters childWithSetters) {
    }
    public ChildWithSetters(AbstractParent abstractParent) {
    }
    public ChildWithSetters(MasterInterface masterInterface) {
    }

    @Override
    public String imAbstract() {
        return "impImplemented in child";
    }

    public void setStrField(int strField) {
        this.strField = strField+"";
    }

    public void setStrField(String strField) {
        this.strField = strField;
    }

    public void setSomeNumber(int someNumber) {
        this.someNumber = someNumber;
    }

    public void setFire(Fire fire) {
        this.fire = fire;
    }
}
