package com.weirddev.testme.intellij.template.context;

import java.util.Map;

/**
 * High level builder for composing test class implementation fragments.
 * Date: 22/04/2017
 *
 * @author Yaron Yamin
 */
@SuppressWarnings("unused")
public interface TestBuilder {
    String RESULT_VARIABLE_NAME = "expectedResult";

    /**
     * construct a formatted string of values passed to method invocation
     * @param method method being called
     * @param replacementTypes type - value map where key is the fully qualified type name to be replaced with a template of initialized type, for arguments in method call. typically to replace interface type with concrete type initialization
     * @param defaultTypeValues default types - value map to be used for relevant type arguments in method call
     * @return formatted string of method values
     * @throws Exception
     */
    String renderMethodParams(Method method, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception;

    /**
     * build constructs for composing a parameterized test call
     * @param method mehtod being tested
     * @param replacementTypesForReturn possible replacement types for method returned type. typically to verify a returned interface type with concrete value
     * @param replacementTypes possible replacement types for method call values
     * @param defaultTypeValues default values to pass as method call parameters
     * @return string constructs for composing a parameterized test call
     * @throws Exception
     */
    ParameterizedTestComponents buildPrameterizedTestComponents(Method method, Map<String, String> replacementTypesForReturn, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception;

    /**
     * builds a string representing the expression returning a resulted value to be verified or mocked
     * @param testedMethod method being called
     * @param type return type
     * @param defaultName default string value to be used if type is a string or couldn't be resolved. should typically be a meaningful name
     * @param replacementTypes type - value map where key is the fully qualified type name to be replaced with a template of initialized type, for arguments in method call. typically to replace interface type with concrete type initialization
     * @param defaultTypeValues default types - value map to be used for relevant type arguments in method call
     * @return a string representing the expression returning a resulted value to be verified
     * @throws Exception
     */
    String renderReturnParam(Method testedMethod, Type type, String defaultName, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception;

    /**
     * constructs a type initialization expression
     * @param type type being initialized
     * @param defaultName efault string value to be used if type is a string or couldn't be resolved. should typically be a meaningful name
     * @param replacementTypes type - value map where key is the fully qualified type name to be replaced with a template of initialized type, for arguments in method call. typically to replace interface type with concrete type initialization
     * @param defaultTypeValues default types - value map to be used for relevant type arguments in method call
     * @return type initialization expression
     * @throws Exception
     */
    String renderInitType(Type type, String defaultName, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) throws Exception;

    /**
     * Parameter role
     */
    enum ParamRole {
        /**
         * input parameter - a method argument
         */
        Input,
        /**
         * output parameter - method return value
         */
        Output
    }
}
