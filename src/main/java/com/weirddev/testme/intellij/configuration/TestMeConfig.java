package com.weirddev.testme.intellij.configuration;

/**
 * Date: 29/07/2018
 *
 * @author Yaron Yamin
 */

public class TestMeConfig {
    private Boolean generateTestsForInherited = true;
    private Boolean reformatCode= true;
    private Boolean replaceFullyQualifiedNames= true;
    private Boolean optimizeImports= true;

    public Boolean getGenerateTestsForInherited() {
        return generateTestsForInherited;
    }

    public void setGenerateTestsForInherited(Boolean generateTestsForInherited) {
        this.generateTestsForInherited = generateTestsForInherited;
    }

    public Boolean getReformatCode() {
        return reformatCode;
    }

    public void setReformatCode(Boolean reformatCode) {
        this.reformatCode = reformatCode;
    }

    public Boolean getReplaceFullyQualifiedNames() {
        return replaceFullyQualifiedNames;
    }

    public void setReplaceFullyQualifiedNames(Boolean replaceFullyQualifiedNames) {
        this.replaceFullyQualifiedNames = replaceFullyQualifiedNames;
    }

    public Boolean getOptimizeImports() {
        return optimizeImports;
    }

    public void setOptimizeImports(Boolean optimizeImports) {
        this.optimizeImports = optimizeImports;
    }
}
