package com.example.services.impl

/**
 * Created by Admin on 07/05/2017.
 */
class FooBro {
    String propInSamePackage
    int iCanBeAccessedDirectly
    int antoherDirectlyAccessedInt
    Date anotherProp

    Date getAnotherProp() {
        return anotherProp
    }

    void setAnotherProp(Date anotherProp) {
        this.anotherProp = anotherProp
    }
}
