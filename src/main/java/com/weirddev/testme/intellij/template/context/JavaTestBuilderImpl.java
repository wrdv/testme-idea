package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import com.weirddev.testme.intellij.utils.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2/16/2017
 *
 * @author Yaron Yamin
 */
public class JavaTestBuilderImpl implements LangTestBuilder {
    private static final Logger LOG = Logger.getInstance(JavaTestBuilderImpl.class.getName());
    private static Type DEFAULT_TYPE = new Type("java.lang.String", "String", "java.lang", false, false, false, false, false, new ArrayList<Type>());
    private static final String PARAMS_SEPARATOR = ", ";

    protected final int maxRecursionDepth;
    protected final Method testedMethod;
    /**
     * do not invoke setter or getter for a property not used in tested method
     */
    protected final boolean shouldIgnoreUnusedProperties;
    protected final TestBuilder.ParamRole paramRole; //todo consider removing. not used anymore
    /**
     * Relevant when shouldIgnoreUnusedProperties is true. When the minimum percentage of all interactions with constructed type are via setters/getters or direct property field read/assignment - then the type is considered as a 'data' bean,
     * so a null value is passed for any constructor argument that is bound to a field in the constructed type which is not used in the tested method
     */
    protected final int minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization;

    public JavaTestBuilderImpl(int maxRecursionDepth, Method testedMethod, boolean shouldIgnoreUnusedProperties, TestBuilder.ParamRole paramRole, int minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization) {
        this.maxRecursionDepth = maxRecursionDepth;
        this.testedMethod = testedMethod;
        this.shouldIgnoreUnusedProperties = shouldIgnoreUnusedProperties;
        this.paramRole = paramRole;
        this.minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization = minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization;
    }

    //TODO consider aggregating conf into context object and managing maps outside of template
    @Override
    public String renderJavaCallParams(List<Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) {
        final StringBuilder stringBuilder = new StringBuilder();
        buildCallParams(null, params, replacementTypes, defaultTypeValues, stringBuilder,new Node<Param>(null,null,0));
        return stringBuilder.toString();
    }

    @Override
    public String renderJavaCallParam(Type type, String strValue, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues) {
        final StringBuilder stringBuilder = new StringBuilder();
        buildCallParam(replacementTypes, defaultTypeValues, stringBuilder,new Node<Param>(new SyntheticParam(type, strValue,false),null,0));
        return stringBuilder.toString();
    }

    String resolveType(Type type, Map<String, String> replacementTypes) {
        String canonicalName = type.getCanonicalName();
        String sanitizedCanonicalName = ClassNameUtils.stripGenerics(canonicalName);
        if (replacementTypes != null && replacementTypes.get(sanitizedCanonicalName) != null) {
            String replacedSanitizedCanonicalName = replacementTypes.get(sanitizedCanonicalName);
            canonicalName = replacedSanitizedCanonicalName.replace("<TYPES>", ClassNameUtils.extractGenerics(canonicalName));
        }
        return canonicalName;
    }

    protected void buildCallParam(Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, StringBuilder testBuilder, Node<Param> paramNode) {
        final Type type = paramNode.getData().getType();
        if (type.isArray()) {
            testBuilder.append("new ").append(type.getCanonicalName()).append("[]{");
        }
        final Type parentContainerClass = type.getParentContainerClass();
        if (parentContainerClass != null && !type.isStatic()) {
            final Node<Param> parentContainerNode = new Node<Param>(new SyntheticParam(parentContainerClass, parentContainerClass.getName(), false), null, paramNode.getDepth());
            buildCallParam(replacementTypes, defaultTypeValues, testBuilder,parentContainerNode);
            testBuilder.append(".");
        }
        buildJavaParam(replacementTypes, defaultTypeValues, testBuilder,paramNode);
        if (type.isArray()) {
            testBuilder.append("}");
        }
    }
    protected void buildJavaParam(Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, StringBuilder testBuilder, Node<Param> paramNode) {
        final Type type = paramNode.getData().getType();
        final String canonicalName = type.getCanonicalName();
        if (defaultTypeValues.get(canonicalName) != null) {

            testBuilder.append(defaultTypeValues.get(canonicalName));
        } else if (canonicalName.equals("java.lang.String")) {
            testBuilder.append("\"").append(paramNode.getData().getName()).append("\"");
        } else if (type.getEnumValues().size() > 0) {
            testBuilder.append(canonicalName).append(".").append(type.getEnumValues().get(0));
        } else {
            String typeName = resolveType(type, replacementTypes);
            if (!canonicalName.equals(typeName)) {
                final String[] typeInitExp = typeName.split("<VAL>");
                testBuilder.append(typeInitExp[0]);
                for (int i = 1; i < typeInitExp.length; i++) {
                    Type genericTypeParam;
                    if (type.getComposedTypes().size() >= i) {
                        genericTypeParam = type.getComposedTypes().get(i - 1);
                    } else {
                        genericTypeParam = DEFAULT_TYPE;
                    }
                    if (isLooksLikeObjectKeyInGroovyMap(typeInitExp[i], genericTypeParam.getCanonicalName())) {
                        testBuilder.append("(");
                    }
                    buildCallParam(replacementTypes, defaultTypeValues, testBuilder, new Node<Param>(new SyntheticParam(genericTypeParam, genericTypeParam.getName(), false),paramNode,paramNode.getDepth()));
                    if (isLooksLikeObjectKeyInGroovyMap(typeInitExp[i], genericTypeParam.getCanonicalName())) {
                        testBuilder.append(")");
                    }
                    testBuilder.append(typeInitExp[i]);
                }
            }
            else if (shouldContinueRecursion(paramNode)) {
                final boolean hasEmptyConstructor = hasEmptyConstructor(type);
                Method foundCtor = findValidConstructor(type, replacementTypes, hasEmptyConstructor);
                if (foundCtor == null && !hasEmptyConstructor || !type.isDependenciesResolved()) {
                    testBuilder.append("null");
                } else {
                    testBuilder.append("new ");
                    if (type.getParentContainerClass() != null && !type.isStatic()) {
                        typeName = resolveNestedClassTypeName(typeName);
                    }
                    testBuilder.append(typeName).append("(");
                    buildCallParams(foundCtor,foundCtor==null?new ArrayList<Param>():foundCtor.getMethodParams(), replacementTypes, defaultTypeValues, testBuilder, paramNode);
                    testBuilder.append(")");
                }

            } else {
                testBuilder.append("null");
            }
        }
    }
    protected void buildCallParams(Method constructor, List<? extends Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, StringBuilder testBuilder, Node<Param> ownerParamNode) {
        final int origLength = testBuilder.length();
        if (params != null) {
            final Type ownerType = ownerParamNode.getData()==null?null: ownerParamNode.getData().getType();
            for (Param param : params) {
                final Node<Param> paramNode = new Node<Param>(param, ownerParamNode, ownerParamNode.getDepth() + 1);
                if (shouldIgnoreUnusedProperties && testedMethod != null) {
                    if (isPropertyParam(paramNode.getData()) && ownerType != null && !isPropertyUsed(testedMethod, paramNode.getData(), ownerType)) {
                        LOG.debug("property unused "+paramNode.getData());
                        continue;
                    } else {
                        boolean shouldOptimizeConstructorInitialization = ownerType !=null && constructor!=null && isShouldOptimizeConstructorInitialization(ownerType,constructor,params, ownerType.getCanonicalName());
                        if (shouldOptimizeConstructorInitialization && !param.getType().isPrimitive() && isUnused(ownerType, testedMethod, deductAssignedToFields(constructor, param))) {
                            testBuilder.append("null" + PARAMS_SEPARATOR);
                            LOG.debug("unused param " + param);
                            continue;
                        }
                    }
                }
                buildCallParam(replacementTypes, defaultTypeValues, testBuilder, paramNode);
                testBuilder.append(PARAMS_SEPARATOR);
            }
            if (origLength < testBuilder.length()) {
                testBuilder.delete(testBuilder.length() - PARAMS_SEPARATOR.length(),testBuilder.length());
            }
        }
    }
    protected boolean isPropertyParam(Param param) {
        return param instanceof SyntheticParam && ((SyntheticParam) param).isProperty;
    }

    protected boolean isShouldOptimizeConstructorInitialization(Type ownerType, Method constructor, List<? extends Param> params, String ownerTypeCanonicalName) {
        boolean shouldOptimizeConstructorInitialization = false;
        if (testedMethod != null && params.size() > 0) {
            int nBeanUsages = 0;
            for (Param param : params) {
                if (!isUnused(ownerType, testedMethod, deductAssignedToFields(constructor, param))) {
                    nBeanUsages++;
                }
            }
            int nTypeReferences = countTypeReferences(ownerTypeCanonicalName, testedMethod);
            //            for (MethodCall methodCall : testedMethod.getCalledFamilyMembers()) {
            int nMethodCalls = 0;
            for (MethodCall methodCall : testedMethod.getMethodCalls()) {
                if (!methodCall.getMethod().isConstructor() && isSharedType(ownerType, methodCall.getMethod())) {
                    nMethodCalls++;
                }
            }
            shouldOptimizeConstructorInitialization = shouldOptimizeConstructorInitialization(nTypeReferences + nMethodCalls, nBeanUsages);
            LOG.debug(String.format("shouldOptimizeConstructorInitialization:%s shouldOptimizeConstructorInitialization= %d nBeanUsages %d nTypeReferences %d nMethodCalls %d", shouldOptimizeConstructorInitialization,
                    minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization, nBeanUsages, nTypeReferences, nMethodCalls));
        }
        return shouldOptimizeConstructorInitialization;
    }

    boolean shouldOptimizeConstructorInitialization(int nTotalTypeUsages, int nBeanUsages) {
        return 0 < nBeanUsages && (nTotalTypeUsages * (minPercentOfInteractionWithPropertiesToTriggerConstructorOptimization / 100f) <= ((float)nBeanUsages) );
    }

    protected boolean isUnused(Type ownerType, Method testedMethod, List<Field> fields) {
        int unusedFieldsCount=0;
        for (Field field : fields) {
            if (!isPropertyUsed(testedMethod, new SyntheticParam(field.getType(),field.getName(),true), ownerType)) {
                unusedFieldsCount++;
            }
        }
        if (fields.size() > 0 && fields.size() == unusedFieldsCount) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean isPropertyUsed(@NotNull Method testedMethod, Param propertyParam, Type ownerType) {
        String paramOwnerCanonicalName = ownerType.getCanonicalName();
        if (isReferencedInMethod(testedMethod, propertyParam, paramOwnerCanonicalName)) return true;
        if (isPropertyUsedIndirectly(null, testedMethod, propertyParam, ownerType)) return true;
//        for (MethodCall methodCall : testedMethod.getCalledFamilyMembers()) {
        for (MethodCall methodCall : testedMethod.getMethodCalls()) {
            if (isReferencedInMethod(methodCall.getMethod(), propertyParam, paramOwnerCanonicalName)) return true;
            if (isPropertyUsedIndirectly(methodCall,methodCall.getMethod(), propertyParam, ownerType)) return true;
        }
        return false;
    }
    private boolean isPropertyUsedIndirectly(MethodCall methodCall, @NotNull Method method, Param propertyParam, Type paramOwner) {
        String paramOwnerCanonicalName = paramOwner.getCanonicalName();
        if (methodCall !=null && isSharedType(paramOwner, methodCall.getMethod()) && isConstructorArgumentUsed(propertyParam, paramOwnerCanonicalName, methodCall, methodCall.getMethod())) {
            return true;
        }
        for (MethodCall methodCallArg : method.getMethodCalls()) {
            final Method methodCalled = methodCallArg.getMethod();
            if (isSharedType(paramOwner, methodCalled) && (isGetterUsed(propertyParam, methodCalled) || isSetterUsed(propertyParam, methodCalled) )) {
                LOG.debug("getter or setter are used "+paramOwnerCanonicalName+" - "+propertyParam+" in methodCall "+methodCalled);
                return true;
            }
        }
        return false;
    }

    private boolean isSharedType(Type ownerType, Method methodCalled) {
        for (Method method : ownerType.getMethods()) {
            if (method.getMethodId().equals(methodCalled.getMethodId())) {
                return true;
            }
        }
        return false;
    }

    private boolean isConstructorArgumentUsed(Param propertyParam, String paramOwnerCanonicalName, MethodCall methodCall, Method method) {
        return method.isConstructor() && hasNonNullFieldMapping(methodCall, propertyParam,paramOwnerCanonicalName);
    }

    private boolean isSetterUsed(Param propertyParam, Method calledMethod) {
        return calledMethod.isSetter() && calledMethod.getMethodParams().size()==1 && calledMethod.getMethodParams().get(0).getType().equals(propertyParam.getType()) && propertyParam.getName().equals(calledMethod.getPropertyName());
    }

    private boolean isGetterUsed(Param propertyParam, Method calledMethod) {
        return calledMethod.isGetter() && calledMethod.getReturnType().getCanonicalName().equals(propertyParam.getType().getCanonicalName()) && propertyParam.getName().equals(calledMethod.getPropertyName());
    }
    private boolean hasNonNullFieldMapping(MethodCall methodCall, Param propertyParam, String paramOwnerCanonicalName) {
        for (int i = 0; i < methodCall.getMethod().getMethodParams().size(); i++) {
            for (Field field : deductAssignedToFields(methodCall.getMethod(), methodCall.getMethod().getMethodParams().get(i))) {
                if (methodCall.getMethodCallArguments() != null && methodCall.getMethodCallArguments().size() > i   && !"null".equals(methodCall.getMethodCallArguments().get(i).getText())
                        && field.getName().equals(propertyParam.getName()) && field.getType().equals(propertyParam.getType()) && field.getOwnerClassCanonicalName().equals(paramOwnerCanonicalName)) {
                    LOG.debug("param has non-null field mapping -"+paramOwnerCanonicalName+"#"+propertyParam+"   -  field - " + field);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isReferencedInMethod(@NotNull Method method, Param propertyParam, String paramOwnerTypeCanonicalName) {
        if (method.isConstructor()) { /*assuming a more extensive ctor param usage check already done*/
            return false;
        }
        for (Reference internalReference : method.getInternalReferences()) {
            if (paramOwnerTypeCanonicalName.equals(internalReference.getOwnerType().getCanonicalName()) && propertyParam.getType().equals(internalReference.getReferenceType())
                    && propertyParam.getName().equals(internalReference.getReferenceName())) {
                LOG.debug("property referenced in method "+paramOwnerTypeCanonicalName+"#"+propertyParam+" - method - "+internalReference);
                return true;
            }
        }
        return false;
    }

    protected List<Field> deductAssignedToFields(Method constructor, Param param) {
        final ArrayList<Field> assignedToFields = param.getAssignedToFields();
        if (assignedToFields.size() == 0 ) {
            return deductAffectedFields(constructor,param);
        } else {
            return assignedToFields;
        }
    }

    private List<Field> deductAffectedFields(Method constructor, Param param) {
        final List<Field> affectedFields = new ArrayList<Field>();
        for (Field field : constructor.getIndirectlyAffectedFields()) {
            if (field.getType().equals(param.getType())) {
                affectedFields.add(field);
            }
        }
        return affectedFields;
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


    protected String resolveNestedClassTypeName(String typeName) {
        return ClassNameUtils.extractClassName(typeName);
    }

    private boolean isLooksLikeObjectKeyInGroovyMap(String expFragment, String canonicalTypeName) {
        return ":".equals(expFragment) && !"java.lang.String".equals(canonicalTypeName);
    }

    /**
     * @param type Input assumption: type constructors are sorted in descending order by no of arguments
     */
    @Nullable
    protected Method findValidConstructor(Type type, Map<String, String> replacementTypes, boolean hasEmptyConstructor) {
        Method foundCtor = null;
        for (Method method : type.findConstructors()) {
            if (isValidConstructor(type, method,hasEmptyConstructor,replacementTypes)) {
                foundCtor = method;
                break;
            }
        }
        return foundCtor;
    }

    private boolean shouldContinueRecursion(Node<Param> paramNode) {
        LOG.debug("recursionDepth:"+paramNode.getDepth() +". maxRecursionDepth "+maxRecursionDepth);
        return paramNode.getDepth() <= maxRecursionDepth && !paramNode.hasSameAncestor();
    }

    protected boolean isValidConstructor(Type type, Method constructor, boolean hasEmptyConstructor, Map<String, String> replacementTypes) {
        if (!constructor.isAccessible() || type.isInterface() || type.isAbstract()) return false;
        final List<Param> methodParams = constructor.getMethodParams();
        for (Param methodParam : methodParams) {
            final Type methodParamType = methodParam.getType();
            if (methodParamType.equals(type) && hasEmptyConstructor) {
                return false;
            }
            if ((methodParamType.isInterface() || methodParamType.isAbstract()) && (replacementTypes == null || replacementTypes.get(ClassNameUtils.stripGenerics(methodParamType.getCanonicalName())) == null) && hasEmptyConstructor) {
                return false;
            }
        }
        return true;
    }

    protected boolean hasEmptyConstructor(Type type) {
        if (type.isInterface() || type.isAbstract()) {
            return false;
        }
        if (type.isHasDefaultConstructor()) {
            return true;
        }
        for (Method method : type.findConstructors()) {
            if (method.isAccessible() &&  method.getMethodParams().size() == 0) {
                return true;
            }
        }
        return false;
    }
}
