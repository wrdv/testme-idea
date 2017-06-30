package com.example.services.impl;

/**
 * Created by Admin on 28/06/2017.
 */
public class DelegateCtor {
    private String family;
    private String members;
    private String only;

    protected DelegateCtor(String family,String members,String only){
        this.family = family;
        DelegateCtor(members, only);
    }

    private void DelegateCtor(String members, String only) {
        this.members = members;
        this.only = only;
    }

    @Override
    public String toString() {
        return "DelegateCtor{" +
                "family='" + family + '\'' +
                ", members='" + members + '\'' +
                ", only='" + only + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DelegateCtor that = (DelegateCtor) o;

        if (family != null ? !family.equals(that.family) : that.family != null) return false;
        if (members != null ? !members.equals(that.members) : that.members != null) return false;
        return !(only != null ? !only.equals(that.only) : that.only != null);

    }
}
