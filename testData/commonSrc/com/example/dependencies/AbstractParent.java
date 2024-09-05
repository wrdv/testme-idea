package com.example.dependencies;

/**
 * Created by Admin on 02/03/2017.
 */
public abstract class AbstractParent implements MasterInterface{
    @Override
    public void intMethod() {
        System.out.println(imAbstract());
        System.out.println("implemented in abstract");
    }

    public abstract String imAbstract();
}
