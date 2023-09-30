package com.weirddev.testme.intellij.template.context.impl;

import com.intellij.ide.hierarchy.HierarchyBrowserBaseEx;
import com.intellij.ide.hierarchy.type.SubtypesHierarchyTreeStructure;
import com.intellij.ide.hierarchy.type.TypeHierarchyNodeDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.util.lang.JavaVersion;
import com.weirddev.testme.intellij.generator.TestBuilderUtil;
import com.weirddev.testme.intellij.template.FileTemplateConfig;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.template.context.*;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import com.weirddev.testme.intellij.utils.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Date: 2/16/2017
 *
 * @author Yaron Yamin
 */
public class JavaTestBuilderImpl implements LangTestBuilder {
    private static final Logger LOG = Logger.getInstance(JavaTestBuilderImpl.class.getName());
    private static final Type DEFAULT_STRING_TYPE = new Type("java.lang.String", "String", "java.lang", false, false, false, false, 0, false, new ArrayList<>());
    private static final int JAVA_9_VERSION = 9;
    private final TestBuilder.ParamRole paramRole;
    private final Method testedMethod;
    protected final String NEW_INITIALIZER = "new ";
    private final Module srcModule;
    private final TypeDictionary typeDictionary;
    protected FileTemplateConfig fileTemplateConfig;
    private JavaVersion javaVersion;
    private final Map<String, String> defaultTypeValues;
    private final Map<String, String> typesOverrides;

    public JavaTestBuilderImpl(Method testedMethod, TestBuilder.ParamRole paramRole, FileTemplateConfig fileTemplateConfig, Module srcModule, TypeDictionary typeDictionary, JavaVersion javaVersion, Map<String, String> defaultTypeValues, Map<String, String> typesOverrides) {
        this.testedMethod = testedMethod;
        this.srcModule = srcModule;
        this.typeDictionary = typeDictionary;
        this.paramRole = paramRole;
        this.fileTemplateConfig = fileTemplateConfig;
        this.javaVersion = javaVersion;
        this.defaultTypeValues = defaultTypeValues;
        this.typesOverrides = typesOverrides;
    }

    @Override
    public String renderJavaCallParams(List<Param> params) {
        final StringBuilder stringBuilder = new StringBuilder();
        buildCallParams(null, params, stringBuilder, new Node<>(null, null, 0));
        return stringBuilder.toString();
    }

    @Override
    public String renderJavaCallParam(Type type, String strValue) {
        final StringBuilder stringBuilder = new StringBuilder();
        buildCallParam(stringBuilder, new Node<>(new SyntheticParam(type, strValue), null, 0));
        return stringBuilder.toString();
    }

    protected void buildCallParam(StringBuilder testCodeString, Node<Param> paramNode) {
        final Type type = paramNode.getData().getType();
        int arrayDimensions = type.getArrayDimensions();
        if (type.isArray()) {
            for (int i = arrayDimensions; i > 0; i--) {
                testCodeString.append(NEW_INITIALIZER).append(type.getCanonicalName()).append("[]".repeat(i)).append("{");
            }
        }
        final Type parentContainerClass = type.getParentContainerClass();
        if (parentContainerClass != null && !type.isStatic()) {
            final Node<Param> parentContainerNode = new Node<>(new SyntheticParam(parentContainerClass, parentContainerClass.getName()), null, paramNode.getDepth());
            buildCallParam(testCodeString,parentContainerNode);
            testCodeString.append(".");
        }
        buildJavaParam(testCodeString,paramNode);
        if (type.isArray()) {
            testCodeString.append("}".repeat(arrayDimensions));
        }
    }
    void buildJavaParam(StringBuilder testBuilder, Node<Param> paramNode) {
        final Type type = paramNode.getData().getType();
        final String canonicalName = type.getCanonicalName();
        if (defaultTypeValues.get(canonicalName) != null) {

            testBuilder.append(defaultTypeValues.get(canonicalName));
        } else if (TestBuilderUtil.isStringType(canonicalName)) {
            testBuilder.append("\"").append(resolveName(paramNode)).append("\"");
        } else if (hasEnumValues(type)) {
            renderEnumValue(testBuilder, type);
        } else {
            final Type resolvedType=resolveChildTypeIfNeeded(type,fileTemplateConfig.getMaxRecursionDepth());
            if (!resolvedType.equals(type)) {
                paramNode= new Node<>(new Param(resolvedType, paramNode.getData().getName(), paramNode.getData().getAssignedToFields()), paramNode.getParent(), paramNode.getDepth());
            }
            String typeName = resolveTypeName(resolvedType);
            if (!resolvedType.getCanonicalName().equals(typeName)) {
                final String[] typeInitExp = typeName.split("<VAL>");
                if (typeInitExp.length == 0) {
                    Type genericTypeParam = safeGetComposedTypeAtIndex(resolvedType, 0);
                    buildCallParam(testBuilder, new Node<>(new SyntheticParam(genericTypeParam, genericTypeParam.getName(), SyntheticParam.UsageContext.Generic), paramNode, paramNode.getDepth()));
                }
                else {
                    testBuilder.append(typeInitExp[0]);
                    for (int i = 1; i < typeInitExp.length; i++) {
                        Type genericTypeParam = safeGetComposedTypeAtIndex(resolvedType, i-1);
                        if (TestBuilderUtil.looksLikeObjectKeyInGroovyMap(typeInitExp[i], genericTypeParam.getCanonicalName())) {
                            testBuilder.append("(");
                        }
                        buildCallParam(testBuilder, new Node<>(new SyntheticParam(genericTypeParam, genericTypeParam.getName(), SyntheticParam.UsageContext.Generic), paramNode, paramNode.getDepth()));
                        if (TestBuilderUtil.looksLikeObjectKeyInGroovyMap(typeInitExp[i], genericTypeParam.getCanonicalName())) {
                            testBuilder.append(")");
                        }
                        testBuilder.append(typeInitExp[i]);
                    }
                }
            }
            else if (shouldContinueRecursion(paramNode)) {
                final boolean hasEmptyConstructor = TestBuilderUtil.hasValidEmptyConstructor(resolvedType);
                Method foundCtor = findValidConstructor(resolvedType, hasEmptyConstructor);
                if (foundCtor == null && !hasEmptyConstructor || !resolvedType.isDependenciesResolved()) {
                    testBuilder.append("null");
                } else {
                    testBuilder.append(resolveInitializerKeyword( type,foundCtor));
                    if (resolvedType.getParentContainerClass() != null && !resolvedType.isStatic()) {
                        typeName = resolveNestedClassTypeName(typeName);
                    }
                    testBuilder.append(typeName).append("(");
                    buildCallParams(foundCtor,foundCtor==null? new ArrayList<>():foundCtor.getMethodParams(), testBuilder, paramNode);
                    testBuilder.append(")");
                }

            } else {
                testBuilder.append("null");
            }
        }
    }

    protected boolean hasEnumValues(Type type) {
        return type.getEnumValues().size() > 0;
    }

    protected void renderEnumValue(StringBuilder testBuilder, Type type) {
        final String enumValue = type.getEnumValues().get(0);
        final String canonicalName = type.getCanonicalName();
        testBuilder.append(canonicalName).append(".").append(enumValue);
    }
    private String resolveName(Node<Param> paramNode) {
        if (paramNode.getData() instanceof SyntheticParam && ((SyntheticParam) paramNode.getData()).getUsageContext() == SyntheticParam.UsageContext.Generic && paramNode.getParent()!=null) {
            return resolveName(paramNode.getParent());
        }
        else{
            return paramNode.getData().getName();
        }
    }
    private Type safeGetComposedTypeAtIndex(Type resolvedType, int i) {
        Type genericTypeParam;
        if (resolvedType.getComposedTypes().size() > i) {
            genericTypeParam = resolvedType.getComposedTypes().get(i);
        } else {
            genericTypeParam = DEFAULT_STRING_TYPE;
        }
        return genericTypeParam;
    }

    @NotNull
    protected String resolveInitializerKeyword(Type type, Method foundCtor) {
        return NEW_INITIALIZER;
    }

    private Type resolveChildTypeIfNeeded(Type type, int maxRecursionDepth) {
        if (!isConcreteType(type) && fileTemplateConfig.isReplaceInterfaceParamsWithConcreteTypes()) {
            final Type childType = findChildType(type,maxRecursionDepth);
            if (childType == null) {
                return type;
            } else {
                return childType;
            }
        } else {
            return type;
        }
    }

    private boolean isConcreteType(Type type) {
        return !type.isInterface() && !type.isAbstract();
    }

    private Type findChildType(Type type, int maxRecursionDepth) {
        final PsiClass psiClass = findClassInModule(type.getCanonicalName());
        if (psiClass != null) {
            final Object[] childElements = new SubtypesHierarchyTreeStructure(srcModule.getProject(), psiClass, HierarchyBrowserBaseEx.SCOPE_PROJECT/*"All"*/).getChildElements(new TypeHierarchyNodeDescriptor(srcModule.getProject(),null,psiClass,true));
            if (childElements.length > 0 && childElements.length <= fileTemplateConfig.getMaxNumOfConcreteCandidatesToReplaceInterfaceParam()) {
                for (Object childElement : childElements) {
                    Type childType = null;
                    if (childElement instanceof TypeHierarchyNodeDescriptor) {
                        final TypeHierarchyNodeDescriptor hierarchyNodeDescriptor = ((TypeHierarchyNodeDescriptor) childElement);
                        if (hierarchyNodeDescriptor.getPsiClass() instanceof PsiClass) {
                            final PsiClass childPsiClass = (PsiClass) hierarchyNodeDescriptor.getPsiClass();
                            if (findClassInModule(childPsiClass.getQualifiedName()) != null) {
                                final PsiClassType psiChildType = JavaPsiFacade.getInstance(srcModule.getProject()).getElementFactory().createType(childPsiClass);//todo verify behaviour with groovy classes
                                childType = typeDictionary.getType(psiChildType, fileTemplateConfig.getMaxRecursionDepth(), true);
                            }
                        }
                    }
                    if (childType != null) {
                        if (isConcreteType(childType)) {
                            return childType;
                        } else if(maxRecursionDepth>0){
                            final Type grandChild = findChildType(childType, maxRecursionDepth - 1);
                            if (grandChild != null) {
                                return grandChild;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    private PsiClass findClassInModule(String fqName) {
        return JavaPsiFacade.getInstance(srcModule.getProject()).findClass(fqName, srcModule.getModuleRuntimeScope(true));
    }

    protected void buildCallParams(Method constructor, List<? extends Param> params, StringBuilder testBuilder, Node<Param> ownerParamNode) {
        final int origLength = testBuilder.length();
        if (params != null) {
            final Type ownerType = ownerParamNode.getData()==null?null: ownerParamNode.getData().getType();
            for (Param param : params) {
                final Node<Param> paramNode = new Node<>(param, ownerParamNode, ownerParamNode.getDepth() + 1);
                if (fileTemplateConfig.isIgnoreUnusedProperties() && testedMethod != null) {
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
                buildCallParam(testBuilder, paramNode);
                testBuilder.append(PARAMS_SEPARATOR);
            }
            if (origLength < testBuilder.length()) {
                testBuilder.delete(testBuilder.length() - PARAMS_SEPARATOR.length(),testBuilder.length());
            }
        }
    }
    /**
     * @param type Input assumption: type constructors are sorted in descending order by no of arguments
     */
    @Nullable
    protected Method findValidConstructor(Type type, boolean hasEmptyConstructor) {
        Method foundCtor = null;
        for (Method method : type.findConstructors()) {
            if (isValidConstructor(type, method,hasEmptyConstructor)) {
                foundCtor = method;
                break;
            }
        }
        return foundCtor;
    }

    public boolean isValidConstructor(Type type, Method constructor, boolean hasEmptyConstructor) {
        if (!constructor.isAccessible() || type.isInterface() || type.isAbstract()) return false;
        final List<Param> methodParams = constructor.getMethodParams();
        for (Param methodParam : methodParams) {
            final Type methodParamType = methodParam.getType();
            if (methodParamType.equals(type) && hasEmptyConstructor) {
                return false;
            }
            //todo revise this logic - interface param might be resolved to concrete type
            String canonicalName = methodParamType.getCanonicalName();
            if ((methodParamType.isInterface() || methodParamType.isAbstract()) && resolveConcreteType(ClassNameUtils.stripGenerics(canonicalName)) == null && hasEmptyConstructor) {
                return false;
            }
        }
        return true;
    }

    String resolveTypeName(Type type) {
        String canonicalName = type.getCanonicalName();
        String replacementType = resolveConcreteType(ClassNameUtils.stripGenerics(canonicalName));
        if (replacementType == null) {
            return canonicalName;
        }
        else {
            return replacementType.replace("<TYPES>", ClassNameUtils.extractGenerics(canonicalName));
        }
    }

    private String resolveConcreteType(String canonicalTypeName) {
        if (typesOverrides != null && typesOverrides.get(canonicalTypeName) != null) {
            return typesOverrides.get(canonicalTypeName);
        }
        if (this.paramRole == TestBuilder.ParamRole.Output ) {
            return resolveConcreteTypeForReturn(canonicalTypeName);
        }
        else {
            return resolveConcreteTypeForInput(canonicalTypeName);
        }
    }

    private String resolveConcreteTypeForInput(String canonicalTypeName) {
        if (javaVersion.isAtLeast(JAVA_9_VERSION)) {
            return TestBuilderTypes.getJava9ReplacementTypes().get(canonicalTypeName);
        }
        else {
            return TestBuilderTypes.getLegacyJavaReplacementTypes().get(canonicalTypeName);
        }
    }

    private String resolveConcreteTypeForReturn(String canonicalTypeName) {
        if (javaVersion.isAtLeast(JAVA_9_VERSION)) {
            return TestBuilderTypes.getJava9ReplacementTypesForReturn().get(canonicalTypeName);
        }
        else {
            return TestBuilderTypes.getLegacyJavaReplacementTypesForReturn().get(canonicalTypeName);
        }
    }

    boolean isPropertyParam(Param param) {
        return param instanceof SyntheticParam && ((SyntheticParam) param).getUsageContext() == SyntheticParam.UsageContext.Property;
    }

    private boolean isShouldOptimizeConstructorInitialization(Type ownerType, Method constructor, List<? extends Param> params, String ownerTypeCanonicalName) {
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
                    fileTemplateConfig.getMinPercentOfInteractionWithPropertiesToTriggerConstructorOptimization(), nBeanUsages, nTypeReferences, nMethodCalls));
        }
        return shouldOptimizeConstructorInitialization;
    }

    boolean shouldOptimizeConstructorInitialization(int nTotalTypeUsages, int nBeanUsages) {
        return 0 < nBeanUsages && (nTotalTypeUsages * (fileTemplateConfig.getMinPercentOfInteractionWithPropertiesToTriggerConstructorOptimization() / 100f) <= ((float)nBeanUsages) );
    }

    private boolean isUnused(Type ownerType, Method testedMethod, List<Field> fields) {
        int unusedFieldsCount=0;
        for (Field field : fields) {
            if (!isPropertyUsed(testedMethod, new SyntheticParam(field.getType(),field.getName(), SyntheticParam.UsageContext.Property), ownerType)) {
                unusedFieldsCount++;
            }
        }
        return fields.size() > 0 && fields.size() == unusedFieldsCount;
    }

    private boolean isPropertyUsed(@NotNull Method testedMethod, Param propertyParam, Type ownerType) {
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
        for (Method methodRef : method.getMethodReferences()) {
            if (isSharedType(paramOwner, methodRef) && (isGetterUsed(propertyParam, methodRef) || isSetterUsed(propertyParam, methodRef) )) {
                LOG.debug("getter or setter are used in method ref "+paramOwnerCanonicalName+" - "+propertyParam+" in methodCall "+methodRef);
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

    private List<Field> deductAssignedToFields(Method constructor, Param param) {
        final ArrayList<Field> assignedToFields = param.getAssignedToFields();
        if (assignedToFields.size() == 0 ) {
            return deductAffectedFields(constructor,param);
        } else {
            return assignedToFields;
        }
    }

    private List<Field> deductAffectedFields(Method constructor, Param param) {
        final List<Field> affectedFields = new ArrayList<>();
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

    private boolean shouldContinueRecursion(Node<Param> paramNode) {
        LOG.debug("recursionDepth:"+paramNode.getDepth() +". maxRecursionDepth "+fileTemplateConfig.getMaxRecursionDepth());
        return paramNode.getDepth() <= fileTemplateConfig.getMaxRecursionDepth() && !paramNode.hasSameAncestor();
    }

}
