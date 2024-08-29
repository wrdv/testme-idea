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
    private boolean isDefault;

    private String description;

    private String extension;

    public TestMeFileTemplate(String name, String extension, boolean isDefault) {
        this.name = name;
        this.extension = extension;
        this.isDefault = isDefault;
    }


    @NotNull
    @Override
    public String getName() {
        return name;
    }
//    @NotNull
//    public String getNormalizedName() {
//        return  FTManager.encodeFileName(name, extension);
//    }

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
        return description;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName == null? name: displayName;
    }

    @Override
    public String toString() {
        return "TestMeFileTemplate{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", isDefault=" + isDefault +
                ", extension='" + extension + '\'' +
                '}';
    }
}
