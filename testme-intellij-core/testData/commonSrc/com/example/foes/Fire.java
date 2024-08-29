package com.example.foes;
/** Test input class*/
public class Fire{

    private String location = "in da house";

    public String toString(){
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fire fire = (Fire) o;

        return !(location != null ? !location.equals(fire.location) : fire.location != null);

    }

    @Override
    public int hashCode() {
        return location != null ? location.hashCode() : 0;
    }
}