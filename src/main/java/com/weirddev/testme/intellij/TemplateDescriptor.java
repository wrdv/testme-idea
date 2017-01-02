package com.weirddev.testme.intellij;

import com.weirddev.testme.intellij.icon.IconTokensReplacerImpl;

/**
 * Date: 10/12/2016
 *
 * @author Yaron Yamin
 */
public class TemplateDescriptor {
    private String tokenizedDisplayName;
    private String displayName;
    private String filename;

    public TemplateDescriptor(String tokenizedDisplayName, String filename) {
        this.tokenizedDisplayName = tokenizedDisplayName;
        this.displayName = IconTokensReplacerImpl.stripTokens(tokenizedDisplayName);
        this.filename = filename;
    }

    public String getTokenizedDisplayName() {
        return tokenizedDisplayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFilename() {
        return filename;
    }

    public String getTestClassFormat() {
        return "%sTest";
    }
}
