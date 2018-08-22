package com.weirddev.testme.intellij.configuration;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.application.ex.ApplicationInfoEx;

/**
 * Date: 18/08/2018
 *
 * @author Yaron Yamin
 */
public class TestMeHelpManager{
    public void invokeHelp() {
        BrowserUtil.browse(TestMeWebHelpProvider.SETTINGS_DOCS_URL+ ideaVersion());
    }

    private String ideaVersion() {
        ApplicationInfoEx info = ApplicationInfoEx.getInstanceEx();
        String minorVersion = info.getMinorVersion();
        int dot = minorVersion.indexOf('.');
        if (dot != -1) {
            minorVersion = minorVersion.substring(0, dot);
        }
        return info.getMajorVersion() + "." + minorVersion;
    }
}
