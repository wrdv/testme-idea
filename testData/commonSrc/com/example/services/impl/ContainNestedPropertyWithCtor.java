package com.example.services.impl;

/**
 * Created by Admin on 01/09/2017.
 */
public class ContainNestedPropertyWithCtor {
    private DelegateCtor delegateCtor;
    private String someStr;
    private Integer someNum;

    public ContainNestedPropertyWithCtor(DelegateCtor delegateCtor, String someStr, Integer someNum) {
        this.delegateCtor = delegateCtor;
        this.someStr = someStr;
        this.someNum = someNum;
    }

    @Override
    public String toString() {
        return "ContainNestedPropertyWithCtor{" +
                "delegateCtor=" + delegateCtor +
                ", someStr='" + someStr + '\'' +
                ", someNum=" + someNum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContainNestedPropertyWithCtor that = (ContainNestedPropertyWithCtor) o;

        if (delegateCtor != null ? !delegateCtor.equals(that.delegateCtor) : that.delegateCtor != null) return false;
        if (someStr != null ? !someStr.equals(that.someStr) : that.someStr != null) return false;
        return !(someNum != null ? !someNum.equals(that.someNum) : that.someNum != null);

    }

    @Override
    public int hashCode() {
        int result = delegateCtor != null ? delegateCtor.hashCode() : 0;
        result = 31 * result + (someStr != null ? someStr.hashCode() : 0);
        result = 31 * result + (someNum != null ? someNum.hashCode() : 0);
        return result;
    }
}
