package com.weirddev.testme.intellij.template;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import com.weirddev.testme.intellij.template.context.Language;
import com.weirddev.testme.intellij.ui.customizedialog.FileTemplateCustomization;

/**
 * Date: 10/19/2016
 *
 * @author Yaron Yamin
 */
public class FileTemplateContext {
    private FileTemplateDescriptor fileTemplateDescriptor;
    private Language language;
    private final Project project;
    private final String targetClass;
    private final PsiPackage targetPackage;
    private final Module srcModule;
    private final Module testModule;
    private final PsiDirectory targetDirectory;
    private final PsiClass srcClass;
    private final FileTemplateConfig fileTemplateConfig;
    private final FileTemplateCustomization fileTemplateCustomization;

    public FileTemplateContext(FileTemplateDescriptor fileTemplateDescriptor, Language language, Project project,
        String targetClass, PsiPackage targetPackage, Module srcModule, Module testModule, PsiDirectory targetDirectory,
        PsiClass srcClass, FileTemplateConfig fileTemplateConfig, FileTemplateCustomization fileTemplateCustomization) {
        this.fileTemplateDescriptor = fileTemplateDescriptor;
        this.language = language;
        this.project = project;
        this.targetClass = targetClass;
        this.targetPackage = targetPackage;
        this.srcModule = srcModule;
        this.testModule = testModule;
        this.targetDirectory = targetDirectory;
        this.srcClass = srcClass;
        this.fileTemplateConfig = fileTemplateConfig;
        this.fileTemplateCustomization = fileTemplateCustomization;
    }

    public Project getProject() {
        return project;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public PsiPackage getTargetPackage() {
        return targetPackage;
    }

    public Module getSrcModule() {
        return srcModule;
    }

    public PsiDirectory getTargetDirectory() {
        return targetDirectory;
    }

    public PsiClass getSrcClass() {
        return srcClass;
    }

    public FileTemplateDescriptor getFileTemplateDescriptor() {
        return fileTemplateDescriptor;
    }
    public Language getLanguage() {
        return language;
    }

    public Module getTestModule() {
        return testModule;
    }

    public FileTemplateConfig getFileTemplateConfig() {
        return fileTemplateConfig;
    }

    public FileTemplateCustomization getFileTemplateCustomization() {
        return fileTemplateCustomization;
    }
}
