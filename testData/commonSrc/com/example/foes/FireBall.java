package com.example.foes;

import com.example.beans.JavaBean;

/** Test input class*/
public class FireBall implements Comparable{

    private String message = "great balls of";
    JavaBean javaBean;
    public String toString(){
        return message;
    }

    public JavaBean getJavaBean() {
        return javaBean;
    }

    public FireBall(JavaBean javaBean) {
        this.javaBean = javaBean;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FireBall fire = (FireBall) o;

        return !(message != null ? !message.equals(fire.message) : fire.message != null);

    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof FireBall) {
            return message.compareTo(((FireBall) o).message);
        } else {
            return -1;
        }
    }
}