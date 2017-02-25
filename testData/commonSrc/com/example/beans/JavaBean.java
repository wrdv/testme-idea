package com.example.beans;

import com.example.foes.Fear;
import com.example.foes.Fire;
import com.example.foes.Ice;

import java.util.Date;

/** Test input class*/
public class JavaBean {
    String myString;
    Date myDate;
    private int someNum;
    Long someLongerNum;
    Fear fear;
    Fire fire;
    private Ice ice;
    private boolean someBinaryOption;

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public Date getMyDate() {
        return myDate;
    }

    public void setMyDate(Date myDate) {
        this.myDate = myDate;
    }

    public int getSomeNum() {
        return someNum;
    }

    public void setSomeNum(int someNum) {
        this.someNum = someNum;
    }

    public Long getSomeLongerNum() {
        return someLongerNum;
    }

    public void setSomeLongerNum(Long someLongerNum) {
        this.someLongerNum = someLongerNum;
    }

    public Fear getFear() {
        return fear;
    }

    public void setFear(Fear fear) {
        this.fear = fear;
    }

    public Fire getFire() {
        return fire;
    }

    public void setFire(Fire fire) {
        this.fire = fire;
    }

    public Ice getIce() {
        return ice;
    }

    public void setIce(Ice ice) {
        this.ice = ice;
    }

    @Override
    public String toString() {
        return "JavaBean{" +
                "myString='" + myString + '\'' +
                ", myDate=" + myDate +
                ", someNum=" + someNum +
                ", someLongerNum=" + someLongerNum +
                ", fear=" + fear +
                ", fire=" + fire +
                ", ice=" + ice +
                ", someBinaryOption=" + someBinaryOption +
                '}';
    }

    public boolean isSomeBinaryOption() {
        return someBinaryOption;
    }

    public void setSomeBinaryOption(boolean someBinaryOption) {
        this.someBinaryOption = someBinaryOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaBean javaBean = (JavaBean) o;

        if (someNum != javaBean.someNum) return false;
        if (someBinaryOption != javaBean.someBinaryOption) return false;
        if (myString != null ? !myString.equals(javaBean.myString) : javaBean.myString != null) return false;
        if (myDate != null ? !myDate.equals(javaBean.myDate) : javaBean.myDate != null) return false;
        if (someLongerNum != null ? !someLongerNum.equals(javaBean.someLongerNum) : javaBean.someLongerNum != null)
            return false;
        if (fear != null ? !fear.equals(javaBean.fear) : javaBean.fear != null) return false;
        if (fire != null ? !fire.equals(javaBean.fire) : javaBean.fire != null) return false;
        return !(ice != null ? !ice.equals(javaBean.ice) : javaBean.ice != null);

    }

    @Override
    public int hashCode() {
        int result = myString != null ? myString.hashCode() : 0;
        result = 31 * result + (myDate != null ? myDate.hashCode() : 0);
        result = 31 * result + someNum;
        result = 31 * result + (someLongerNum != null ? someLongerNum.hashCode() : 0);
        result = 31 * result + (fear != null ? fear.hashCode() : 0);
        result = 31 * result + (fire != null ? fire.hashCode() : 0);
        result = 31 * result + (ice != null ? ice.hashCode() : 0);
        result = 31 * result + (someBinaryOption ? 1 : 0);
        return result;
    }
}
