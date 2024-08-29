package com.example.util;

import com.example.beans.JavaBean;

/**
 * Created by Admin on 21/04/2017.
 */
public class FooUtils {
    public static String callPooh(JavaBean javaBean) {
        return "Wheres my Honey ?" + javaBean.getSomeNum();
    }
}
