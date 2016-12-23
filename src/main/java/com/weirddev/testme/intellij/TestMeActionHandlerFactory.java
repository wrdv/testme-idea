package com.weirddev.testme.intellij;

/**
 * Date: 16/12/2016
 *
 * @author Yaron Yamin
 */
public class TestMeActionHandlerFactory {
    public static TestMeActionHandler create() { //todo - get rid of this, no longer needed. init directly
        return new TestMeActionHandler();
    }
}
