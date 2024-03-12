package com.weirddev.testme.intellij.icon;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Date: 10/15/2016
 *
 * @author Yaron Yamin
 */
public interface Icons {
    Icon GROOVY = getIcon("/icons/groovy.png");
    Icon SCALA = getIcon("/icons/scala.png");
    Icon MOCKITO = getIcon("/icons/mockito.png");
    Icon POWERMOCK = getIcon("/icons/powermock.png");
    Icon JUNIT4 = getIcon("/icons/junit.png");
    Icon JUNIT5 = getIcon("/icons/junit5.png");
    Icon JUNIT4_DARK = getIcon("/icons/junit_dark.png");
    Icon TEST_ME = getIcon("/icons/TestMe.png");
    Icon TESTNG = getIcon("/icons/testNG.png");

    @NotNull
    private static Icon getIcon(String path) {
        return IconLoader.getIcon(path, Icons.class.getClassLoader());
    }

}
