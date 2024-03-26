package com.weirddev.testme.intellij.builder;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.MethodSignatureUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.weirddev.testme.intellij.common.utils.LanguageUtils;
import com.weirddev.testme.intellij.common.utils.PsiMethodUtils;
import com.weirddev.testme.intellij.groovy.resolvers.GroovyPsiTreeUtils;
import com.weirddev.testme.intellij.resolvers.to.MethodCallArg;
import com.weirddev.testme.intellij.resolvers.to.ResolvedMethodCall;
import com.weirddev.testme.intellij.resolvers.to.ResolvedReference;
import com.weirddev.testme.intellij.scala.resolvers.ScalaPsiTreeUtils;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.template.context.*;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import com.weirddev.testme.intellij.utils.JavaPsiTreeUtils;
import com.weirddev.testme.intellij.utils.PropertyUtils;
import com.weirddev.testme.intellij.utils.TypeUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class MethodFactory {
    private static final Logger LOG = Logger.getInstance(MethodFactory.class.getName());

    public static Method createMethod(PsiMethod psiMethod, PsiClass srcClass, int maxRecursionDepth, TypeDictionary typeDictionary, @Nullable PsiType ownerClassPsiType) {
        boolean isPrivate = psiMethod.hasModifierProperty(PsiModifier.PRIVATE);
        boolean isProtected = psiMethod.hasModifierProperty(PsiModifier.PROTECTED);
        boolean isDefault = isDefault(psiMethod);
        boolean isPublic = psiMethod.hasModifierProperty(PsiModifier.PUBLIC);
        boolean isAbstract = psiMethod.hasModifierProperty(PsiModifier.ABSTRACT);
        boolean isNative = psiMethod.hasModifierProperty(PsiModifier.NATIVE);
        boolean isStatic = psiMethod.hasModifierProperty(PsiModifier.STATIC);
        String methodName = psiMethod.getName();
        String ownerClassCanonicalType = resolveOwnerClassName(psiMethod);
        boolean isConstructor = psiMethod.isConstructor();
        boolean isPrimaryConstructor = isConstructor && psiMethod.getClass().getSimpleName().contains("PrimaryConstructor");
        boolean isSetter = PropertyUtils.isPropertySetter(psiMethod);
        boolean isGetter = PropertyUtils.isPropertyGetter(psiMethod);
        String propertyName1 = ClassNameUtils.extractTargetPropertyName(methodName, isSetter, isGetter);
        boolean overriddenInChild = isOverriddenInChild(psiMethod, srcClass);
        boolean inherited = isInherited(psiMethod, srcClass);
        boolean isInterface = isInterface(psiMethod);
        String methodId = PsiMethodUtils.formatMethodId(psiMethod);
        boolean accessible = typeDictionary.isAccessible(psiMethod);
        boolean syntheticMethod = isSyntheticMethod(psiMethod);
        boolean testable = isTestable(psiMethod, srcClass);
        Optional<PsiSubstitutor> methodSubstitutor = findMethodSubstitutor(psiMethod, srcClass, ownerClassPsiType);
        Type returnType = resolveReturnType(psiMethod, maxRecursionDepth, typeDictionary, methodSubstitutor);
        List<Param> methodParams = extractMethodParams(psiMethod, isPrimaryConstructor, maxRecursionDepth, typeDictionary, methodSubstitutor);
        String throwsExceptions = extractMethodExceptionTypes(psiMethod);
        return new Method(methodId, methodName, returnType,   ownerClassCanonicalType, methodParams, throwsExceptions,isPrivate, isProtected, isDefault, isPublic, isAbstract, isNative,
                isStatic, isSetter, isGetter, isConstructor,  overriddenInChild, inherited, isInterface, syntheticMethod, propertyName1, accessible,
                isPrimaryConstructor,   testable);

    }
    public static void resolveInternalReferences(@NotNull TypeDictionary typeDictionary, PsiMethod psiMethod, Method method) {
        method.getDirectMethodCalls().addAll(MethodFactory.resolveCalledMethods(psiMethod, typeDictionary));
        method.getMethodCalls().addAll(method.getDirectMethodCalls());
        method.getInternalReferences().addAll(MethodFactory.resolveReferences(psiMethod,typeDictionary));
        method.getMethodReferences().addAll(MethodFactory.resolveMethodReferences(psiMethod,typeDictionary));
    }

    public static List<ResolvedMethodCall> resolvedMethodCalls(PsiMethod psiMethod) {
        if (LanguageUtils.isGroovy(psiMethod.getLanguage())) {
            return GroovyPsiTreeUtils.findMethodCalls(psiMethod).stream().toList();
        }
        else if (LanguageUtils.isScala(psiMethod.getLanguage())) {
            return ScalaPsiTreeUtils.findMethodCalls(psiMethod);
        }
        else {
            return JavaPsiTreeUtils.findMethodCalls(psiMethod);
        }
    }
    private static Set<Reference> resolveReferences(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        Set<Reference> references = new HashSet<>();
        for (ResolvedReference resolvedReference : resolvedReferences(psiMethod)) {
            references.add(new Reference(resolvedReference.getReferenceName(), resolvedReference.getRefType(), resolvedReference.getPsiOwnerType(), typeDictionary));
        }
        return references;
    }

    @NotNull
    private static List<ResolvedReference> resolvedReferences(PsiMethod psiMethod) {
        if (LanguageUtils.isGroovy(psiMethod.getLanguage())) {
            return GroovyPsiTreeUtils.findReferences(psiMethod);
        }
        else {
            return JavaPsiTreeUtils.findReferences(psiMethod);
        }
    }

    private static Set<Method> resolveMethodReferences(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        final Set<Method> methodReferences = new HashSet<>();
        for (PsiMethod resolvedMethodReference : JavaPsiTreeUtils.findMethodReferences(psiMethod)) {
            if (typeDictionary.isRelevant(resolvedMethodReference, null)) {
                methodReferences.add(MethodFactory.createMethod(resolvedMethodReference, resolvedMethodReference.getContainingClass(), 1, typeDictionary, null));
            }
        }
        return methodReferences;
    }

    private static Set<MethodCall> resolveCalledMethods(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        //todo try to pass/support src class in scala/groovy as well. if successful, consider re-implementing with a factory method call
        final Set<MethodCall> directMethodCalls = new HashSet<>();
        List<ResolvedMethodCall> methodCalls = resolvedMethodCalls(psiMethod);
        for (ResolvedMethodCall resolvedMethodCall : methodCalls) {
            if (typeDictionary.isRelevant(resolvedMethodCall.getPsiMethod(), null)) {
                directMethodCalls.add(new MethodCall(MethodFactory.createMethod(resolvedMethodCall.getPsiMethod(), null, 1, typeDictionary, null),convertArgs(resolvedMethodCall.getMethodCallArguments())));
            }
        }
        return directMethodCalls;
    }
    @Nullable
    private static Type resolveReturnType(PsiMethod psiMethod, int maxRecursionDepth, TypeDictionary typeDictionary, Optional<PsiSubstitutor> methodSubstitutor) {
        final PsiType psiType = psiMethod.getReturnType();
        if (psiType == null) {
            return null;
        } else {
            Optional<PsiType> substitutedType = methodSubstitutor.map(psiSubstitutor -> psiSubstitutor.substitute(psiType));
            Object typeElement = null;
            if (LanguageUtils.isScala(psiMethod.getLanguage())) {
                typeElement = ScalaPsiTreeUtils.resolveReturnType(psiMethod);
            }
            return typeDictionary.getType(substitutedType.orElse(psiType), maxRecursionDepth, true,typeElement);
        }
    }

    private static List<MethodCallArgument> convertArgs(List<MethodCallArg> methodCallArguments) {
        final ArrayList<MethodCallArgument> methodCallArgs = new ArrayList<>();
        if (methodCallArguments != null) {
            for (MethodCallArg methodCallArgument : methodCallArguments) {
                methodCallArgs.add(new MethodCallArgument(methodCallArgument.getText()));
            }
        }
        return methodCallArgs;
    }

    private static PsiField resolveLeftHandExpressionAsField(PsiExpression expr) {
        PsiElement parent = PsiTreeUtil.skipParentsOfType(expr, PsiParenthesizedExpression.class);
        if (!(parent instanceof PsiAssignmentExpression psiAssignmentExpression)) {
            return null;
        }
        final PsiReference reference = psiAssignmentExpression.getLExpression().getReference();
        final PsiElement element = reference != null ? reference.resolve() : null;
        return element instanceof PsiField ? (PsiField) element : null;
    }

    private static List<Param> extractMethodParams(PsiMethod psiMethod, boolean shouldResolveAllMethods, int maxRecursionDepth, TypeDictionary typeDictionary, Optional<PsiSubstitutor> methodSubstitutor) {
        ArrayList<Param> params = new ArrayList<>();
        final PsiParameter[] parameters;
        if (LanguageUtils.isScala(psiMethod.getLanguage())) {
            parameters = ScalaPsiTreeUtils.resolveParameters(psiMethod);
        } else {
            parameters = psiMethod.getParameterList().getParameters();
        }
        for (PsiParameter psiParameter : parameters) {
            Optional<PsiType> substitutedType = methodSubstitutor.map(psiSubstitutor -> psiSubstitutor.substitute(psiParameter.getType()));
            final ArrayList<Field> assignedToFields = findMatchingFields(psiParameter, psiMethod);
            params.add(new Param(psiParameter,substitutedType,typeDictionary,maxRecursionDepth,assignedToFields,shouldResolveAllMethods));
        }
        return params;
    }


    //analyze the exception types of the method
    private static String extractMethodExceptionTypes(PsiMethod psiMethod) {
        String throwsExceptions = "";
        PsiReferenceList throwsList = psiMethod.getThrowsList();
        PsiClassType[] referencedTypes = throwsList.getReferencedTypes();

        for (PsiClassType type : referencedTypes) {
            PsiClass resolved = type.resolve();
            if (resolved != null) {
                String exceptionTypeName = getExceptionTypeName(resolved.getQualifiedName());
                throwsExceptions += exceptionTypeName+",";
            }
        }
        if(StringUtils.isNotEmpty(throwsExceptions)){
            throwsExceptions = throwsExceptions.substring(0, throwsExceptions.length() - 1);
        }
        return throwsExceptions;
    }

    private static String getExceptionTypeName(String exceptionTypeName) {
        int lastIndex = exceptionTypeName.lastIndexOf('.');
        return lastIndex != -1 ? exceptionTypeName.substring(lastIndex + 1) :exceptionTypeName;
    }



    private static ArrayList<Field> findMatchingFields(PsiParameter psiParameter, PsiMethod psiMethod) {
        final ArrayList<Field> fields = new ArrayList<>();
        try {
            if (!psiMethod.hasModifierProperty(PsiModifier.STATIC)) {
                LocalSearchScope searchScope = LanguageUtils.isScala(psiMethod.getLanguage()) && psiMethod.getContainingClass()!=null ? new LocalSearchScope(psiMethod.getContainingClass()) : new LocalSearchScope(psiMethod);
                for (PsiReference reference : ReferencesSearch.search(psiParameter, searchScope)) {
                    final PsiElement element = reference.getElement();
                    PsiField psiField = null;
                    if (LanguageUtils.isGroovy(element.getLanguage())) {
                        psiField = GroovyPsiTreeUtils.resolveGrLeftHandExpressionAsField(element);
                    }
                    else if (element instanceof PsiExpression && !PsiUtil.isOnAssignmentLeftHand((PsiExpression) element)) {
                        psiField = resolveLeftHandExpressionAsField((PsiExpression) element);
                    }
                    if (psiField != null && psiField.getContainingClass() != null) {
                        fields.add(new Field(psiField, psiField.getContainingClass(),null, 0));
                    }
                }
            }
        } catch (Throwable e) {
            LOG.warn(String.format("cant search for matching fields for parameter %s in method %s", psiParameter.getName(), psiMethod.getName()),e);
        }
        return fields;
    }

    private static boolean isInterface(PsiMethod psiMethod) {
//            //method inherited from an interface but implemented by this interface should not be considered as interface method
//            return psiMethod.hasModifierProperty("abstract") || psiMethod.getContainingClass() != null && psiMethod.getContainingClass().isInterface();
        return psiMethod.hasModifierProperty(PsiModifier.ABSTRACT);
    }

    private static boolean isSyntheticMethod(PsiMethod psiMethod) {
        if (LanguageUtils.isScala(psiMethod.getLanguage())) {
            return ScalaPsiTreeUtils.isSyntheticMethod(psiMethod);
        } else {
            return psiMethod instanceof SyntheticElement; //originally added to cover use case of ClassInnerStuffCache.EnumSyntheticMethod. consider limiting to that use case only if causes issues
        }
    }

    private static boolean hasGenericType(PsiMethod psiMethod) {
        return Stream.concat(Stream.of(psiMethod.getParameterList().getParameters()).map(PsiVariable::getType), Stream.of(psiMethod.getReturnType())).anyMatch(MethodFactory::mayContainTypeParameter);
    }

    private static boolean mayContainTypeParameter(PsiType psiType) {
        return psiType instanceof PsiClassReferenceType/* && ((PsiClassReferenceType) psiType).resolve() instanceof PsiTypeParameter */;
    }

    private static boolean isOverriddenInChild(PsiMethod method, @Nullable PsiClass srcClass) {
        if (srcClass == null) {
            return false;
        }
        String srcQualifiedName = srcClass.getQualifiedName();
        String methodClsQualifiedName = method.getContainingClass()==null?null:method.getContainingClass().getQualifiedName();
        if (srcQualifiedName == null || methodClsQualifiedName == null || srcQualifiedName.equals(methodClsQualifiedName)) {
            return false;
        }
        else{
            final PsiMethod childMethod = MethodSignatureUtil.findMethodBySuperMethod(srcClass, method, false);
            return childMethod != null;
        }
    }

    private static boolean isInherited(PsiMethod method, @Nullable PsiClass srcClass) {
        if(srcClass == null){
            return false;
        }
        String srcQualifiedName = srcClass.getQualifiedName();
        String methodClsQualifiedName = method.getContainingClass()==null?null:method.getContainingClass().getQualifiedName();
        return (srcQualifiedName!=null && methodClsQualifiedName!=null &&  !srcQualifiedName.equals(methodClsQualifiedName));
    }

    private static boolean isDefault(PsiMethod psiMethod) {
        return psiMethod.hasModifierProperty(PsiModifier.DEFAULT) || psiMethod.hasModifierProperty(PsiModifier.PACKAGE_LOCAL);
    }

    @Nullable
    private static String resolveOwnerClassName(PsiMethod psiMethod) {
        return psiMethod.getContainingClass() == null ? null : psiMethod.getContainingClass().getQualifiedName();
    }

    private static boolean isTestable(PsiMethod psiMethod, @Nullable PsiClass srcClass){
        return !TypeUtils.isLanguageBaseClass(resolveOwnerClassName(psiMethod))  && !PropertyUtils.isPropertySetter(psiMethod) && (!PropertyUtils.isPropertyGetter(psiMethod) || srcClass!=null && srcClass.isEnum()) &&
                !psiMethod.isConstructor() && isVisibleForTest(psiMethod, srcClass) && !isOverriddenInChild(psiMethod, srcClass)
                && !isInterface(psiMethod) && !psiMethod.hasModifierProperty(PsiModifier.ABSTRACT) && !isSyntheticMethod(psiMethod);
    }

    private static boolean isVisibleForTest(PsiMethod psiMethod, PsiClass srcClass) {
        return (isDefault(psiMethod) || psiMethod.hasModifierProperty(PsiModifier.PROTECTED)) &&
                !isInherited(psiMethod, srcClass) || psiMethod.hasModifierProperty(PsiModifier.PUBLIC);
    }

    private static  Optional<PsiSubstitutor> findMethodSubstitutor(PsiMethod psiMethod, PsiClass srcClass, @Nullable  PsiType ownerClassPsiType) {
        if (isInherited(psiMethod, srcClass) && isTestable(psiMethod, srcClass) && hasGenericType(psiMethod)) {
            //todo debug simpler case, check if logic can be simplified without relying to method name first
            List<Pair<PsiMethod, PsiSubstitutor>> methodsSubstitutors = srcClass.findMethodsAndTheirSubstitutorsByName(psiMethod.getName(), true);
            return methodsSubstitutors.stream()
                    .filter(psiMethodPsiSubstitutorPair -> psiMethodPsiSubstitutorPair.first.equals(psiMethod))
                    .map(psiMethodPsiSubstitutorPair -> psiMethodPsiSubstitutorPair.second)
                    .findFirst();
        }
        else if(ownerClassPsiType instanceof PsiClassType){
            return Optional.of(((PsiClassType) ownerClassPsiType).resolveGenerics().getSubstitutor());
        }
        else {
            return Optional.empty();
        }
    }

}
