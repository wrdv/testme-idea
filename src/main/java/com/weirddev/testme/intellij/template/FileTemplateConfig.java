package com.weirddev.testme.intellij.template;

/**
 * Date: 25/09/2017
 *
 * @author Yaron Yamin
 */
public class FileTemplateConfig {
    public static final int DEFAULT_MAX_RECURSION_DEPTH = 9;
    private int maxRecursionDepth = DEFAULT_MAX_RECURSION_DEPTH;
    private boolean reformatCode = true;
    private boolean optimizeImports = true;
    private boolean replaceFqn = true;
    /**
     * do not invoke setter or getter for a property not used in tested method
     */
    private boolean ignoreUnusedProperties = true;
    /**
     * replace interface/abstract argument types with concrete types if exists in project. otherwise pass null.
     */
    private boolean replaceInterfaceParamsWithConcreteTypes = true;
    /**
     * Relevant when shouldIgnoreUnusedProperties is true. When the minimum percentage of all interactions with constructed type are via setters/getters or direct property field read/assignment - then the type is considered as a 'data' bean,
     * so a null value is passed for any constructor argument that is bound to a field in the constructed type which is not used in the tested method
     */
    private int minPercentOfExcessiveSettersToPreferMapCtor = 50;
    /**
     * Relevant when shouldIgnoreUnusedProperties is true. When the minimum percentage of all interactions with constructed type are via setters/getters or direct property field read/assignment - then the type is considered as a 'data' bean,
     * so a null value is passed for any constructor argument that is bound to a field in the constructed type which is not used in the tested method
     */
    protected int minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization = 66;

    public FileTemplateConfig() { }

    public FileTemplateConfig(boolean reformatCode, boolean optimizeImports, int maxRecursionDepth, boolean replaceFqn, boolean ignoreUnusedProperties, boolean replaceInterfaceParamsWithConcreteTypes, int
            minPercentOfExcessiveSettersToPreferMapCtor) {
        this.reformatCode = reformatCode;
        this.optimizeImports = optimizeImports;
        this.maxRecursionDepth = maxRecursionDepth;
        this.replaceFqn = replaceFqn;
        this.ignoreUnusedProperties = ignoreUnusedProperties;
        this.replaceInterfaceParamsWithConcreteTypes = replaceInterfaceParamsWithConcreteTypes;
        this.minPercentOfExcessiveSettersToPreferMapCtor = minPercentOfExcessiveSettersToPreferMapCtor;
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

    public boolean isReplaceInterfaceParamsWithConcreteTypes() {
        return replaceInterfaceParamsWithConcreteTypes;
    }

    public int getMinPercentOfExcessiveSettersToPreferMapCtor() {
        return minPercentOfExcessiveSettersToPreferMapCtor;
    }

    public void setMaxRecursionDepth(int maxRecursionDepth) {
        this.maxRecursionDepth = maxRecursionDepth;
    }

    public void setReformatCode(boolean reformatCode) {
        this.reformatCode = reformatCode;
    }

    public void setOptimizeImports(boolean optimizeImports) {
        this.optimizeImports = optimizeImports;
    }

    public void setReplaceFqn(boolean replaceFqn) {
        this.replaceFqn = replaceFqn;
    }

    public void setIgnoreUnusedProperties(boolean ignoreUnusedProperties) {
        this.ignoreUnusedProperties = ignoreUnusedProperties;
    }

    public void setReplaceInterfaceParamsWithConcreteTypes(boolean replaceInterfaceParamsWithConcreteTypes) {
        this.replaceInterfaceParamsWithConcreteTypes = replaceInterfaceParamsWithConcreteTypes;
    }

    public void setMinPercentOfExcessiveSettersToPreferMapCtor(int minPercentOfExcessiveSettersToPreferMapCtor) {
        this.minPercentOfExcessiveSettersToPreferMapCtor = minPercentOfExcessiveSettersToPreferMapCtor;
    }

    public int getMinPercentOfInteractionWithPropertiesToTriggerConstructorOptimization() {
        return minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization;
    }

    public void setMinPercentOfInteractionWithPropertiesToTriggerConstructorOptimization(int minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization) {
        this.minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization = minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization;
    }
}
