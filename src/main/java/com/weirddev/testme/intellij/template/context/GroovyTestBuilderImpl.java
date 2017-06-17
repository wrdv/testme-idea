package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.utils.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 24/02/2017
 *
 * @author Yaron Yamin
 */
public class GroovyTestBuilderImpl extends JavaTestBuilderImpl {
    private static final Logger LOG = Logger.getInstance(GroovyTestBuilderImpl.class.getName());
    public static final String PARAMS_SEPERATOR = ", ";
    private final TestBuilder.ParamRole paramRole;//todo consider removing. not used anymore
    /**
     * do not invoke setter or getter for a property not used in tested method
     */
    private boolean shouldIgnoreUnusedProperties;
    /**
     * If constructed type has more than this minimum percentage of setters over number of arguments in biggest constructor and default constructor is accessible
     * then use initialize the type with default constructor and setters instead of biggest constructor
     */
    private final int minPercentOfExcessiveSettersToPreferDefaultCtor;
    /**
     * Relevant when shouldIgnoreUnusedProperties is true. When the minimum percentage of all interactions with constructed type are via setters/getters or direct property field read/assignment - then the type is considered as a 'data' bean,
     * so a null value is passed for any constructor argument that is bound to a field in the constructed type which is not used in the tested method
     */
    private final int minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization;

    public GroovyTestBuilderImpl(int maxRecursionDepth, Method method, boolean shouldIgnoreUnusedProperties, TestBuilder.ParamRole paramRole, int minPercentOfExcessiveSettersToPreferDefaultCtor, int
            minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization) {
        super(maxRecursionDepth, method);
        this.shouldIgnoreUnusedProperties = shouldIgnoreUnusedProperties;
        this.paramRole = paramRole;
        this.minPercentOfExcessiveSettersToPreferDefaultCtor = minPercentOfExcessiveSettersToPreferDefaultCtor;
        this.minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization = minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization;
    }

    @Override
    protected void buildCallParam(Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, StringBuilder testBuilder, Node<Param> paramNode) {
        final Type type = paramNode.getData().getType();
        if (isPropertyParam(paramNode.getData())) {
            testBuilder.append(paramNode.getData().getName()).append(" : ");
        }
        if (type.isArray()) {
            testBuilder.append("[");
        }
        buildJavaParam(replacementTypes, defaultTypeValues, testBuilder, paramNode);
        if (type.isArray()) {
            testBuilder.append("] as ").append(type.getCanonicalName()).append("[]");
        }
    }

    private boolean isPropertyParam(Param param) {
        return param instanceof SyntheticParam && ((SyntheticParam) param).isProperty;
    }

    @Override
    protected void buildCallParams(List<? extends Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, StringBuilder testBuilder, Node<Param> ownerParamNode) {
        final Type parentContainerClass = ownerParamNode.getData()!=null?ownerParamNode.getData().getType().getParentContainerClass():null;
        final boolean isNonStaticNestedClass = parentContainerClass != null && !ownerParamNode.getData().getType().isStatic();
        if (params != null && params.size()>0 || isNonStaticNestedClass) {
            if (isNonStaticNestedClass) {
                final Node<Param> parentContainerNode = new Node<Param>(new SyntheticParam(parentContainerClass, parentContainerClass.getName(), false), null, ownerParamNode.getDepth());
                buildCallParam(replacementTypes, defaultTypeValues, testBuilder,parentContainerNode);
                testBuilder.append(",");
            }
            buildGroovyCallParams(params, replacementTypes, defaultTypeValues, testBuilder, ownerParamNode);
        } else if(ownerParamNode.getData()!=null){
            List<SyntheticParam> syntheticParams = findProperties(ownerParamNode.getData().getType());
            if (syntheticParams.size() > 0) {
                buildCallParams(syntheticParams, replacementTypes, defaultTypeValues, testBuilder, ownerParamNode);
            }
        }
    }
    protected void buildGroovyCallParams(List<? extends Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, StringBuilder testBuilder, Node<Param> ownerParamNode) {
        final int origLength = testBuilder.length();
        if (params != null) {
            final String ownerTypeCanonicalName = ownerParamNode.getData()==null?null:ownerParamNode.getData().getType().getCanonicalName();
            boolean shouldOptimizeConstructorInitialization = ownerTypeCanonicalName!=null&& isShouldOptimizeConstructorInitialization(params, ownerTypeCanonicalName);
            for (Param param : params) {
                final Node<Param> paramNode = new Node<Param>(param, ownerParamNode, ownerParamNode.getDepth() + 1);
                if (shouldIgnoreUnusedProperties && testedMethod != null) {
                    if (isPropertyParam(paramNode.getData()) && ownerTypeCanonicalName != null && !isPropertyUsed(testedMethod, paramNode.getData(), ownerTypeCanonicalName)) {
                        continue;
                    } else if (shouldOptimizeConstructorInitialization && !param.getType().isPrimitive() && isUnused(testedMethod, param.getAssignedToFields())) {
                        testBuilder.append("null"+PARAMS_SEPERATOR);
                        continue;
                    }
                }
                buildCallParam(replacementTypes, defaultTypeValues, testBuilder, paramNode);
                testBuilder.append(PARAMS_SEPERATOR);
            }
            if (origLength < testBuilder.length()) {
                testBuilder.delete(testBuilder.length() - PARAMS_SEPERATOR.length(),testBuilder.length());
            }
        }
    }

    private boolean isShouldOptimizeConstructorInitialization(List<? extends Param> params, String ownerTypeCanonicalName) {
        boolean shouldOptimizeConstructorInitialization = false;
        if (shouldIgnoreUnusedProperties && testedMethod != null && params.size() > 0) {
            int nBeanUsages = 0;
            int nTotalTypeUsages = 0;
            for (Param param : params) {
                if (!isUnused(testedMethod, param.getAssignedToFields())) {
                    nBeanUsages++;
                }
            }
            nTotalTypeUsages += countTypeReferences(ownerTypeCanonicalName, testedMethod);
            for (Method method : testedMethod.getCalledFamilyMembers()) {
                nTotalTypeUsages += countTypeReferences(ownerTypeCanonicalName, method);
            }
//            for (Method calledMethod : testedMethod.getCalledMethods()) {
//                if (ownerTypeCanonicalName.equals(calledMethod.getOwnerClassCanonicalType()) ) {
//                    nTotalTypeUsages++;
//                }
//            }
            shouldOptimizeConstructorInitialization = shouldOptimizeConstructorInitialization(nTotalTypeUsages, nBeanUsages);
        }
        return shouldOptimizeConstructorInitialization;
    }

    private int countTypeReferences(String ownerTypeCanonicalName, Method method) {
        int nTotalTypeReferences = 0;
        if (method != null) {
            for (Reference internalReference : method.getInternalReferences()) {
                if (ownerTypeCanonicalName.equals(internalReference.getOwnerType().getCanonicalName())) {
                    nTotalTypeReferences++;
                }
            }
        }
        return nTotalTypeReferences;
    }

    private boolean isUnused(Method testedMethod, ArrayList<Field> fields) {
        int unusedFieldsCount=0;
        for (Field field : fields) {
            if (!isPropertyUsed(testedMethod, new SyntheticParam(field.getType(),field.getName(),true),field.getOwnerClassCanonicalName())) {
                unusedFieldsCount++;
            }
        }
        if (fields.size() > 0 && fields.size() == unusedFieldsCount) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPropertyUsed(@NotNull Method testedMethod, Param propertyParam, String paramOwnerCanonicalName) {
        //todo migrate the logic of identifying setters/getters calls to JavaTestBuilder ( direct references)
        if (isReferencedInMethod(testedMethod, propertyParam, paramOwnerCanonicalName)) return true;
        for (Method calledMethod : testedMethod.getCalledMethods()) {
            if (paramOwnerCanonicalName.equals(calledMethod.getOwnerClassCanonicalType()) &&
                    (calledMethod.isGetter() && calledMethod.getReturnType().getCanonicalName().equals(propertyParam.getType().getCanonicalName()) ||
                     calledMethod.isSetter() && calledMethod.getMethodParams().size()==1 && calledMethod.getMethodParams().get(0).getType().equals(propertyParam.getType()) ) && propertyParam.getName().equals(calledMethod.getPropertyName()) ) {
                return true;
            }
        }
        for (Method method : testedMethod.getCalledFamilyMembers()) {
            if (isReferencedInMethod(method, propertyParam, paramOwnerCanonicalName)) return true;
        }
        return false;
    }

    private boolean isReferencedInMethod(@NotNull Method testedMethod, Param propertyParam, String paramOwnerTypeCanonicalName) {
        for (Reference internalReference : testedMethod.getInternalReferences()) {
            if (paramOwnerTypeCanonicalName.equals(internalReference.getOwnerType().getCanonicalName()) && propertyParam.getType().equals(internalReference.getReferenceType()) && propertyParam.getName().equals(internalReference.getReferenceName())) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    private List<SyntheticParam> findProperties(Type type) {
        final List<Method> methods = type.getMethods();
        Map<String,SyntheticParam> syntheticParams=new LinkedHashMap<String,SyntheticParam>();
        for (Method method : methods) {
            if (method.isSetter()&&  method.getMethodParams().size()>0 &&method.getPropertyName()!=null) {
                final SyntheticParam syntheticParam = syntheticParams.get(method.getPropertyName());
                if (syntheticParam == null || !propertyMatchesField(type,syntheticParam)) {
                    syntheticParams.put(method.getPropertyName(),new SyntheticParam(method.getMethodParams().get(0).getType(), method.getPropertyName(),true));
                }
            }
        }
        return new ArrayList<SyntheticParam>(syntheticParams.values());
    }

    private boolean propertyMatchesField(Type type, SyntheticParam syntheticParam) {
        Field field = findFieldByName(type, syntheticParam.getName());
        return field != null && field.getType().getCanonicalName().equals(syntheticParam.getType().getCanonicalName());
    }

    @Nullable
    private Field findFieldByName(Type type, String propertyName) {
        for (Field field : type.getFields()) {
            if (propertyName.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    @Override
    protected String resolveNestedClassTypeName(String typeName) {
        return typeName;
    }

    @Nullable
    @Override
    protected Method findValidConstructor(Type type, Map<String, String> replacementTypes, boolean hasEmptyConstructor) {
        final Method constructor = super.findValidConstructor(type, replacementTypes, hasEmptyConstructor);
        if (constructor == null) {
            return null;
        } else if (hasEmptyConstructor) {
            int noOfCtorArgs = constructor.getMethodParams().size();
            int noOfSetters = countSetters(type);
            if (shouldPreferSettersOverCtor(noOfCtorArgs, noOfSetters)) {
                return null; //Prefer default ctor and use setters instead
            } else {
                return constructor;
            }
        } else {
            return constructor;
        }
    }

    boolean shouldPreferSettersOverCtor(int noOfCtorArgs, int noOfSetters) {
        return 0 < noOfSetters && (noOfCtorArgs * ((minPercentOfExcessiveSettersToPreferDefaultCtor + 100f) / 100f) <= ((float)noOfSetters) );
    }
    boolean shouldOptimizeConstructorInitialization(int nTotalTypeUsages, int nBeanUsages) {
        return 0 < nBeanUsages && (nTotalTypeUsages * (minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization / 100f) <= ((float)nBeanUsages) );
    }

    private int countSetters(Type type) {
        int noOfSetters = 0;
        for (Method method : type.getMethods()) {
            if (method.isSetter()) {
                noOfSetters++;
            }
        }
        return noOfSetters;
    }
}
