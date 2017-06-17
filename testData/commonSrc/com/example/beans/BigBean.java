package com.example.beans;

import com.example.services.impl.DoneThat;
import com.example.services.impl.Many;

/**
 *
 * Created by Yaron Yamin on 24/11/2016.
 */
public class BigBean {
    DoneThat doneThat;
    Many times;
    DoneThat too;
    String doIKnowYou;

    public BigBean(String andThere) {
    }
    public BigBean(DoneThat doneThat) {
    }
    public BigBean(DoneThat doneThat, Many times, DoneThat too) {
        this.doneThat = doneThat;
        this.times = times;
        this.too = too;
    }
    protected BigBean(DoneThat doneThat, Many times, DoneThat too,String doIKnowYou) {
    }
    public BigBean(DoneThat doneThat,Many times) {
    }

    public void setTimes(Many times) {
        this.times = times;
    }

    public void setDoIKnowYou(String doIKnowYou) {
        this.doIKnowYou = doIKnowYou;
    }
}
