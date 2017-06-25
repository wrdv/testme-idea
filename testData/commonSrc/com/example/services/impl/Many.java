package com.example.services.impl;

/**
 * Created by Yaron Yamin on 24/11/2016.
 */
public class Many {
    private String family;
    private String members;
    private String only;

    private Many(String dontTouchThis, String andThis, String andThat, String noEntry){}
    protected Many(String family,String members,String only){
        this.family = family;
        this.members = members;
        this.only = only;
    }
    public Many(String family){
        this.family = family;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Many many = (Many) o;

        if (family != null ? !family.equals(many.family) : many.family != null) return false;
        if (members != null ? !members.equals(many.members) : many.members != null) return false;
        return !(only != null ? !only.equals(many.only) : many.only != null);

    }

    @Override
    public int hashCode() {
        int result = family != null ? family.hashCode() : 0;
        result = 31 * result + (members != null ? members.hashCode() : 0);
        result = 31 * result + (only != null ? only.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Many{" +
                "family='" + family + '\'' +
                ", members='" + members + '\'' +
                ", only='" + only + '\'' +
                '}';
    }
}

