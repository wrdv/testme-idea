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
    private boolean isDefault = false; //todo - is needed?

    private String description;

    private String extension;

    public TestMeFileTemplate(String htmlDisplayName, String extension) {
        this.name = htmlDisplayName;
        this.extension = extension;
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
        return false;
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
