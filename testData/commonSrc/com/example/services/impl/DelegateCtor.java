package com.example.services.impl;

/**
 * Created by Admin on 28/06/2017.
 */
public class DelegateCtor {
    private String youre;
    private String asCold;
    private Ice asIce;

    protected DelegateCtor(String youre,String asCold,Ice asIce){
        this.youre = youre;
        DelegateCtor(asCold, asIce);
    }

    private void DelegateCtor(String asCold, String asIce) {
        this.asCold = asCold;
        this.asIce = asIce;
    }

    @Override
    public String toString() {
        return "DelegateCtor{" +
                "youre='" + youre + '\'' +
                ", asCold='" + asCold + '\'' +
                ", asIce='" + asIce + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DelegateCtor that = (DelegateCtor) o;

        if (youre != null ? !youre.equals(that.youre) : that.youre != null) return false;
        if (asCold != null ? !asCold.equals(that.asCold) : that.asCold != null) return false;
        return !(asIce != null ? !asIce.equals(that.asIce) : that.asIce != null);

    }
}
