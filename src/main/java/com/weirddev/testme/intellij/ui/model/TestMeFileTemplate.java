package com.weirddev.testme.intellij.ui.model;

import com.intellij.ide.fileTemplates.impl.FileTemplateBase;
import org.jetbrains.annotations.NotNull;

/**
 * @author : Yaron Yamin
 * @since : 6/22/20
 **/
public class TestMeFileTemplate extends FileTemplateBase { //todo migrate away from dependency on IDEA SDK

    private String name;

    private String displayName;
    /**
     * true - when this is an OOB template (readonly)
     */
    private boolean isDefault = false;

    private String description;

    private String extension;

    public TestMeFileTemplate(String htmlDisplayName, String extension) {
        this.name = htmlDisplayName;
        this.extension = extension;
        this.isDefault = true;
    }


    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "TODO define html description with referenced links";
    }

    @NotNull
    @Override
    public String getExtension() {
        return extension;
    }

    @Override
    public void setExtension(@NotNull String extension) {
        this.extension = extension;
    }
}
