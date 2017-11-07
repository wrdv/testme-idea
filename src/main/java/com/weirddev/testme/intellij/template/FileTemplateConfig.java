package com.weirddev.testme.intellij.template;

/**
 * Date: 25/09/2017
 *
 * @author Yaron Yamin
 */
public class FileTemplateConfig {
    public static final int DEFAULT_MAX_RECURSION_DEPTH = 9;
    /**
     * Test parameters generator feature. limit the maximum depth of recursive nested parameters initialization and recursion of tested class structure inspection.
     * Warning: raising this value above 9 may result in severe performance degradation during test generation, to a point where the IDE is not responsive for over a minute.
     * Valid values:1-20
     * Default:9
     */
    private int maxRecursionDepth = DEFAULT_MAX_RECURSION_DEPTH;
    /**
     * Test generator styling feature. reformat generated test code according to relevant language styling settings in IDEA
     * Valid values:true,false
     * Default:true
     */
    private boolean reformatCode = true;
    /**
     * Test generator styling feature. replace fully qualified class names with import statements where possible in generated test
     * Valid values:true,false
     * Default:true
     */
    private boolean replaceFqn = true;
    /**
     * Test generator styling feature. optimize imports in generated test
     * Valid values:true,false
     * Default:true
     */
    private boolean optimizeImports = true;
    /**
     * Test mocks generator feature. generate mocked method call response in case mocks that return a value are called by the tested method and the calling code is part of the tested method
     * Valid values:true,false
     * Default:true
     */
    private boolean stubMockMethodCallsReturnValues = true;
    /**
     * Test parameters generator optimization. when true - heuristically identify and ignore unused properties by the tested method, so null is passed for constructor arguments that initialize unused properties. In case a Groovy map constructor used - property will not be initialized
     * Valid values:true,false
     * Default:true
     */
    private boolean ignoreUnusedProperties = true;
    /**
     * Test parameters generator feature.replace interface/abstract argument types with concrete types if exists in project. otherwise pass null.
     * Valid values:true,false
     * Default:true
     */
    private boolean replaceInterfaceParamsWithConcreteTypes = true;

    /**
     * Test parameters generator feature. only relevant when replaceInterfaceParamsWithConcreteTypes is true.
     * If the number of concrete implementations found in project sources is over this limit - then Interface param will not be initialized. otherwise a random selection of the found concrete types will be used.
     * This ia a heuristic number, assuming too many implementations is an indicator to an interface that is too generic (i.e. comparator) - so an arbitrary implementation should not be selected in such case
     * Valid values:1-100
     * Default:2
     */
    private int maxNumOfConcreteCandidatesToReplaceInterfaceParam = 5;
    /**
     * Test parameters generator optimization. Relevant for Groovy tests.
     * When the amount of a given type setters exceeds by this percentage value over of the number of arguments in the type's biggest constructor ( constructor that has the maximum number of arguments) - then a map constructor is used to initialize the type.
     * If the type's biggest constructor has 0 arguments - map constructor will be used anyway if possible
     * Valid values:0-100
     * Default:50
     */
    private int minPercentOfExcessiveSettersToPreferMapCtor = 50;
    /**
     * Relevant when shouldIgnoreUnusedProperties is true. When the minimum percentage of all interactions with constructed type are via setters/getters or direct property field read/assignment - then the type is considered as a 'data' bean,
     * so a null value is passed for any constructor argument that is bound to a field in the constructed type which is not used in the tested method. in case a map constructor being used - than the property will not be initialized.
     * Valid values:0-100
     * Default:66
     */
    private int minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization = 66;

    public FileTemplateConfig() { }
    public static FileTemplateConfig getInstance()  {
        return new FileTemplateConfig(
                Integer.valueOf(System.getProperties().getProperty("testMe.generator.maxRecursionDepth", FileTemplateConfig.DEFAULT_MAX_RECURSION_DEPTH + "")),
                Boolean.valueOf(System.getProperties().getProperty("testMe.style.reformatCode", "true")),
                Boolean.valueOf(System.getProperties().getProperty("testMe.style.replaceFqn", "true")),
                Boolean.valueOf(System.getProperties().getProperty("testMe.style.optimizeImports", "true")),
                Boolean.valueOf(System.getProperties().getProperty("testMe.generator.ignoreUnusedProperties", "true")),
                Boolean.valueOf(System.getProperties().getProperty("testMe.generator.replaceInterfaceParamsWithConcreteTypes", "true")),
                Boolean.valueOf(System.getProperties().getProperty("testMe.generator.stubMockMethodCallsReturnValues", "true")),
                Integer.valueOf(System.getProperties().getProperty("testMe.generator.maxNumOfConcreteCandidatesToReplaceInterfaceParam", "5")),
                Integer.valueOf(System.getProperties().getProperty("testMe.generator.minPercentOfExcessiveSettersToPreferMapCtor", "50")),
                Integer.valueOf(System.getProperties().getProperty("testMe.generator.minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization", "66"))
        );

    }

    public FileTemplateConfig(int maxRecursionDepth, boolean reformatCode, boolean replaceFqn, boolean optimizeImports, boolean ignoreUnusedProperties, boolean replaceInterfaceParamsWithConcreteTypes, boolean stubMockMethodCallsReturnValues,
                              int maxNumOfConcreteCandidatesToReplaceInterfaceParam, int minPercentOfExcessiveSettersToPreferMapCtor, int minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization) {
        this.maxRecursionDepth = maxRecursionDepth;
        this.reformatCode = reformatCode;
        this.replaceFqn = replaceFqn;
        this.optimizeImports = optimizeImports;
        this.stubMockMethodCallsReturnValues = stubMockMethodCallsReturnValues;
        this.ignoreUnusedProperties = ignoreUnusedProperties;
        this.replaceInterfaceParamsWithConcreteTypes = replaceInterfaceParamsWithConcreteTypes;
        this.maxNumOfConcreteCandidatesToReplaceInterfaceParam = maxNumOfConcreteCandidatesToReplaceInterfaceParam;
        this.minPercentOfExcessiveSettersToPreferMapCtor = minPercentOfExcessiveSettersToPreferMapCtor;
        this.minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization = minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization;
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

    public boolean isStubMockMethodCallsReturnValues() {
        return stubMockMethodCallsReturnValues;
    }

    public int getMaxNumOfConcreteCandidatesToReplaceInterfaceParam() {
        return maxNumOfConcreteCandidatesToReplaceInterfaceParam;
    }
}
