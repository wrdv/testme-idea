package com.example.services.impl;

import com.example.beans.BigBean;
import com.example.services.impl.Many;

import java.util.Date;

/**
 * Created by Yaron Yamin on 24/11/2016.
 */
public class DoneThat {
    private final int severalTimes;
    private final String aDay;
    private final Many time;
    private final Date now;
    private final BigBean bean;
    private final String privateProperty;
    private final String bewareOfTheDog;
    private final String ifYourInDaHood;

    public DoneThat(int severalTimes, String aDay, Many time, Date now, BigBean bean) {
        this.severalTimes = severalTimes;
        this.aDay = aDay;
        this.time = time;
        this.now = now;
        this.bean = bean;
    }
    DoneThat(int severalTimes, String aDay, Many time, Date now, BigBean bean,String ifYourInDaHood) {
        this.severalTimes = severalTimes;
        this.aDay = aDay;
        this.time = time;
        this.now = now;
        this.bean = bean;
        this.ifYourInDaHood = ifYourInDaHood;
    }
    private DoneThat(int severalTimes, String aDay, Many time, Date now, BigBean bean,String privateProperty,String bewareOfTheDog) {
        this.severalTimes = severalTimes;
        this.aDay = aDay;
        this.time = time;
        this.now = now;
        this.bean = bean;
        this.privateProperty = privateProperty;
        this.bewareOfTheDog = bewareOfTheDog;
    }
}
