package com.weirddev.testme.intellij.template;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import com.weirddev.testme.intellij.template.context.Language;

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
    private final boolean reformatCode;
    private final boolean optimizeImports;
    private final int maxRecursionDepth;
    private final boolean replaceFqn;
    private final boolean ignoreUnusedProperties;
    private final int minPercentOfExcessiveSettersToPreferDefaultCtor;

    public FileTemplateContext(FileTemplateDescriptor fileTemplateDescriptor, Language language, Project project, String targetClass, PsiPackage targetPackage, Module srcModule, Module testModule, PsiDirectory targetDirectory, PsiClass srcClass, boolean reformatCode, boolean optimizeImports, int maxRecursionDepth, boolean replaceFqn, boolean ignoreUnusedProperties, int minPercentOfExcessiveSettersToPreferDefaultCtor) {
        this.fileTemplateDescriptor = fileTemplateDescriptor;
        this.language = language;
        this.project = project;
        this.targetClass = targetClass;
        this.targetPackage = targetPackage;
        this.srcModule = srcModule;
        this.testModule = testModule;
        this.targetDirectory = targetDirectory;
        this.srcClass = srcClass;
        this.reformatCode = reformatCode;
        this.optimizeImports = optimizeImports;
        this.maxRecursionDepth = maxRecursionDepth;
        this.replaceFqn = replaceFqn;
        this.ignoreUnusedProperties = ignoreUnusedProperties;
        this.minPercentOfExcessiveSettersToPreferDefaultCtor = minPercentOfExcessiveSettersToPreferDefaultCtor;
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

    public boolean isReformatCode() {
        return reformatCode;
    }

    public boolean isOptimizeImports() {
        return optimizeImports;
    }

    public int getMaxRecursionDepth() {
        return maxRecursionDepth;
    }

    public boolean isReplaceFqn() {
        return replaceFqn;
    }

    public boolean isIgnoreUnusedProperties() {
        return ignoreUnusedProperties;
    }

    public int getMinPercentOfExcessiveSettersToPreferDefaultCtor() {
        return minPercentOfExcessiveSettersToPreferDefaultCtor;
    }

    public Language getLanguage() {
        return language;
    }

    public Module getTestModule() {
        return testModule;
    }
}
