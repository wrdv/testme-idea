package com.example.dependencies;

import com.example.foes.FireBall;

/**
 * Created by Admin on 02/03/2017.
 */
public class ChildWithSetters extends AbstractParent{

    String strField;
    int someNumber;
    FireBall fire;

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
        return "implemented in child";
    }

    public void setStrField(String strField) {
        this.strField = strField;
    }

    public void setStrField(int strField) {
        this.strField = strField+"";
    }

    public void setSomeNumber(int someNumber) {
        this.someNumber = someNumber;
    }

    public void setFire(FireBall fire) {
        this.fire = fire;
    }

    public String getStrField() {
        return strField;
    }

    public int getSomeNumber() {
        return someNumber;
    }

    public FireBall getFire() {
        return fire;
    }
}
