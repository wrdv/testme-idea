package com.weirddev.testme.intellij.action.helpers;

/**
 * Date: 18/03/2017
 *
 * @author Yaron Yamin
 */
public class ClassNameSelection {
    private final String className;
    private final UserDecision userDecision;

    public ClassNameSelection(String className, UserDecision userDecision) {
        this.className = className;
        this.userDecision = userDecision;
    }

    public enum UserDecision{
        New,Goto,Abort
    }

    public String getClassName() {
        return className;
    }

    public UserDecision getUserDecision() {
        return userDecision;
    }
}
