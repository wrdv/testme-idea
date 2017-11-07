package com.example.services.impl;

import com.example.dependencies.SelfishService;

/**
 * Testing dependency containing reference to a type that has internal references to himself. motivation: verify there's no infinite recursion issues
 */
public class Foo{

    private SelfishService selfishService;

    /**
     * verify tested method is generated for this - method is not mistakenly identified as a getter
     */
    public String getSelf() {
        return selfishService.getSelfishType().toString();
    }
    public String doSelf() {
        return selfishService.getSelfishType().toString();
    }
}
