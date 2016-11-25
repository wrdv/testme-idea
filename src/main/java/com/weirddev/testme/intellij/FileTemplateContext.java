package com.weirddev.testme.intellij;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;

/**
 * Date: 10/19/2016
 *
 * @author Yaron Yamin
 */
public class FileTemplateContext {
    private FileTemplateDescriptor fileTemplateDescriptor;
    private final Project project;
    private final String targetClass;
    private final PsiPackage targetPackage;
    private final Module targetModule;
    private final PsiDirectory targetDirectory;
    private final PsiClass srcClass;
    private final boolean reformatCode;
    private final boolean optimizeImports;
    private final int maxRecursionDepth;

    public FileTemplateContext(FileTemplateDescriptor fileTemplateDescriptor, Project project, String targetClass, PsiPackage targetPackage, Module targetModule, PsiDirectory targetDirectory, PsiClass srcClass, boolean reformatCode, boolean optimizeImports, int maxRecursionDepth) {
        this.fileTemplateDescriptor = fileTemplateDescriptor;
        this.project = project;
        this.targetClass = targetClass;
        this.targetPackage = targetPackage;
        this.targetModule = targetModule;
        this.targetDirectory = targetDirectory;
        this.srcClass = srcClass;
        this.reformatCode = reformatCode;
        this.optimizeImports = optimizeImports;
        this.maxRecursionDepth = maxRecursionDepth;
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

    public Module getTargetModule() {
        return targetModule;
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

    public boolean isReformatCode() {
        return reformatCode;
    }

    public boolean isOptimizeImports() {
        return optimizeImports;
    }

    public int getMaxRecursionDepth() {
        return maxRecursionDepth;
    }
}
