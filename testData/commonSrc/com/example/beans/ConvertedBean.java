package com.example.beans;

import com.example.foes.Fear;
import com.example.foes.Fire;
import com.example.foes.Ice;

import java.util.Date;

/**
 * Created by Admin on 21/04/2017.
 */
public class ConvertedBean {
    String myString;
    Date myDate;
    private int someNum;
    Long someLongerNum;
    Fear fear;
    private Ice ice;
    private boolean someBinaryOption;

    public ConvertedBean( )
    {
    }

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

    public Ice getIce() {
        return ice;
    }

    public void setIce(Ice ice) {
        this.ice = ice;
    }
    public boolean isSomeBinaryOption() {
        return someBinaryOption;
    }

    public void setSomeBinaryOption(boolean someBinaryOption) {
        this.someBinaryOption = someBinaryOption;
    }

    @Override
    public String toString() {
        return "ConvertedBean{" +
                "myString='" + myString + '\'' +
                ", myDate=" + myDate +
                ", someNum=" + someNum +
                ", someLongerNum=" + someLongerNum +
                ", fear=" + fear +
                ", ice=" + ice +
                ", someBinaryOption=" + someBinaryOption +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConvertedBean that = (ConvertedBean) o;

        if (someNum != that.someNum) return false;
        if (someBinaryOption != that.someBinaryOption) return false;
        if (myString != null ? !myString.equals(that.myString) : that.myString != null) return false;
        if (myDate != null ? !myDate.equals(that.myDate) : that.myDate != null) return false;
        if (someLongerNum != null ? !someLongerNum.equals(that.someLongerNum) : that.someLongerNum != null) return false;
        if (fear != null ? !fear.equals(that.fear) : that.fear != null) return false;
        return !(ice != null ? !ice.equals(that.ice) : that.ice != null);

    }

    @Override
    public int hashCode() {
        int result = myString != null ? myString.hashCode() : 0;
        result = 31 * result + (myDate != null ? myDate.hashCode() : 0);
        result = 31 * result + someNum;
        result = 31 * result + (someLongerNum != null ? someLongerNum.hashCode() : 0);
        result = 31 * result + (fear != null ? fear.hashCode() : 0);
        result = 31 * result + (ice != null ? ice.hashCode() : 0);
        result = 31 * result + (someBinaryOption ? 1 : 0);
        return result;
    }
}
