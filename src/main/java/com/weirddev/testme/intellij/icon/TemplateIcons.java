package com.weirddev.testme.intellij.icon;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.net.URL;

/**
 * @author : Yaron Yamin
 * @since : 6/20/20
 **/
public enum TemplateIcons {
    JUnit4( IconFiles.JUNIT4, IconFiles.JUNIT4_DARK),
    JUnit5( IconFiles.JUNIT5, IconFiles.JUNIT5),
    Mockito( IconFiles.MOCKITO, IconFiles.MOCKITO),
    Powermock( IconFiles.POWERMOCK, IconFiles.POWERMOCK),
    Groovy( IconFiles.GROOVY, IconFiles.GROOVY),
    Scala( IconFiles.SCALA, IconFiles.SCALA),
    TestNG( IconFiles.TESTNG, IconFiles.TESTNG),
    ;

    private static class IconFiles {
        private static final String GROOVY = "/icons/groovy.png";
        private static final String SCALA = "/icons/scala.png";
        private static final String MOCKITO = "/icons/mockito.png";
        private static final String JUNIT4 = "/icons/junit.png";
        private static final String JUNIT5 = "/icons/junit5.png";
        private static final String JUNIT4_DARK = "/icons/junit_dark.png";
        private static final String TESTNG = "/icons/testNG.png";
        private static final String POWERMOCK = "/icons/powermock.png";
    }

    private final Icon icon;
    private final Icon darkIcon;
    private final String iconClasspath;
    private final String darkIconClasspath;

    TemplateIcons(String iconClasspath, String darkIconFilename) {
        this.iconClasspath = iconClasspath;
        this.darkIconClasspath = darkIconFilename;
        this.icon = IconLoader.getIcon(iconClasspath, TemplateIcons.class.getClassLoader());
        this.darkIcon = IconLoader.getIcon(darkIconFilename, TemplateIcons.class.getClassLoader());
    }

    public Icon getIcon() {
        return icon;
    }

    public Icon getDarkIcon() {
        return darkIcon;
    }

    public URL getIconUrl() {
        return TemplateIcons.class.getResource(iconClasspath);
    }

    public URL getDarkIconUrl() {
        return TemplateIcons.class.getResource(darkIconClasspath);
    }
    public String asHtml(){//todo decide if dark version should be used
        return "<img src='"+ iconClasspath +"'>";
    }

}
