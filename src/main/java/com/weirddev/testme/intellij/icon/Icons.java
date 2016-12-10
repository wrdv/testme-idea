package com.weirddev.testme.intellij.icon;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Date: 10/15/2016
 *
 * @author Yaron Yamin
 */
public interface Icons {
    Icon MOCKITO = IconLoader.getIcon("/icons/mockito.png");
    Icon JUNIT4 = IconLoader.getIcon("/icons/junit.png");
    Icon JUNIT5 = IconLoader.getIcon("/icons/junit5.png");
    Icon JUNIT4_DARK = IconLoader.getIcon("/icons/junit_dark.png"); //TODO replace with better matching icon for dark background
    Icon TEST_ME = IconLoader.getIcon("/icons/TestMe.png"); //TODO replace with custom made
}
