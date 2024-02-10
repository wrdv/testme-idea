package com.weirddev.testme.intellij.builder;

import com.intellij.openapi.diagnostic.Logger;
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
import com.weirddev.testme.intellij.utils.JavaPsiTreeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class MethodBuilder {
    private static final Logger LOG = Logger.getInstance(MethodBuilder.class.getName());
    public static Set<Reference> resolveReferences(PsiMethod psiMethod, TypeDictionary typeDictionary) {
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

    public static Set<Method> resolveMethodReferences(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        final Set<Method> methodReferences = new HashSet<>();
        for (PsiMethod resolvedMethodReference : JavaPsiTreeUtils.findMethodReferences(psiMethod)) {
            if (isRelevant(resolvedMethodReference.getContainingClass(), resolvedMethodReference)) {
                methodReferences.add(new Method(resolvedMethodReference, resolvedMethodReference.getContainingClass(), 1, typeDictionary, null));
            }
        }
        return methodReferences;
    }
    public static Set<MethodCall> resolveCalledMethods(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        //todo try to pass/support src class in scala/groovy as well. if successful, consider re-implementing with a factory method call
        final Set<MethodCall> directMethodCalls = new HashSet<>();
        List<ResolvedMethodCall> methodCalls = resolvedMethodCalls(psiMethod);
        for (ResolvedMethodCall resolvedMethodCall : methodCalls) {
            if (isRelevant(resolvedMethodCall.getPsiMethod().getContainingClass(), resolvedMethodCall.getPsiMethod())) {
                directMethodCalls.add(new MethodCall(new Method(resolvedMethodCall.getPsiMethod(), null, 1, typeDictionary, null),convertArgs(resolvedMethodCall.getMethodCallArguments())));
            }
        }
        return directMethodCalls;
    }
    @Nullable
    public static Type resolveReturnType(PsiMethod psiMethod, int maxRecursionDepth, TypeDictionary typeDictionary, Optional<PsiSubstitutor> methodSubstitutor) {
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

    private static List<ResolvedMethodCall> resolvedMethodCalls(PsiMethod psiMethod) {
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
    private static List<MethodCallArgument> convertArgs(List<MethodCallArg> methodCallArguments) {
        final ArrayList<MethodCallArgument> methodCallArgs = new ArrayList<>();
        if (methodCallArguments != null) {
            for (MethodCallArg methodCallArgument : methodCallArguments) {
                methodCallArgs.add(new MethodCallArgument(methodCallArgument.getText()));
            }
        }
        return methodCallArgs;
    }
    public static boolean isRelevant(PsiClass psiClass, PsiMethod psiMethod) {
        boolean isRelevant = true;
        final PsiClass containingClass = psiMethod.getContainingClass();
        final PsiClass ownerClass = containingClass == null ? psiClass : containingClass;
        if (ownerClass != null && isLanguageInherited(ownerClass.getQualifiedName())) {
            isRelevant = false;
        } else {
            final String methodId = PsiMethodUtils.formatMethodId(psiMethod);
            if (LanguageUtils.isGroovy(psiMethod.getLanguage())
                    && (psiMethod.getClass().getCanonicalName().contains("GrGdkMethodImpl") || methodId.endsWith(".invokeMethod(java.lang.String,java.lang.Object)") || methodId.endsWith(".getProperty(java.lang.String)") || methodId
                    .endsWith(".setProperty(java.lang.String,java.lang.Object)"))) {
                isRelevant = false;
            }
            // Need to resolve methods of mocked dependencies from imported libs. consider performance hit..
            /* else if(ownerClass!=null && ownerClass.getQualifiedName()!=null){
                JavaPsiFacade facade = JavaPsiFacade.getInstance( ownerClass.getProject());
                PsiClass[] possibleClasses = facade.findClasses(ownerClass.getQualifiedName(), GlobalSearchScope.projectScope(ownerClass.getProject()));// todo - test with GlobalSearchScope.allScope(ownerClass.getProject()). Alt. skip the check ?
                if (possibleClasses.length == 0) {
                    isRelevant = false;
                }
            }*/
        }
        return isRelevant;
    }
    public static boolean isLanguageInherited(String ownerClassCanonicalType) {
        return "java.lang.Object".equals(ownerClassCanonicalType) || "java.lang.Class".equals(ownerClassCanonicalType) || "groovy.lang.GroovyObjectSupport".equals(ownerClassCanonicalType);
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

    public static List<Param> extractMethodParams(PsiMethod psiMethod, boolean shouldResolveAllMethods, int maxRecursionDepth, TypeDictionary typeDictionary, Optional<PsiSubstitutor> methodSubstitutor) {
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

    public static boolean isInterface(PsiMethod psiMethod) {
//            //method inherited from an interface but implemented by this interface should not be considered as interface method
//            return psiMethod.hasModifierProperty("abstract") || psiMethod.getContainingClass() != null && psiMethod.getContainingClass().isInterface();
        return psiMethod.hasModifierProperty(PsiModifier.ABSTRACT);
    }

    public static boolean isSyntheticMethod(PsiMethod psiMethod) {
        if (LanguageUtils.isScala(psiMethod.getLanguage())) {
            return ScalaPsiTreeUtils.isSyntheticMethod(psiMethod);
        } else {
            return false;
        }
    }

    public static boolean hasGenericType(PsiMethod psiMethod) {
        return Stream.concat(Stream.of(psiMethod.getParameterList().getParameters()).map(PsiVariable::getType), Stream.of(psiMethod.getReturnType())).anyMatch(MethodBuilder::mayContainTypeParameter);
    }

    private static boolean mayContainTypeParameter(PsiType psiType) {
        return psiType instanceof PsiClassReferenceType/* && ((PsiClassReferenceType) psiType).resolve() instanceof PsiTypeParameter */;
    }

    public static boolean isOverriddenInChild(PsiMethod method, PsiClass srcClass) {
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

    public static boolean isInherited(PsiMethod method, PsiClass srcClass) {
        String srcQualifiedName = srcClass.getQualifiedName();
        String methodClsQualifiedName = method.getContainingClass()==null?null:method.getContainingClass().getQualifiedName();
        return (srcQualifiedName!=null && methodClsQualifiedName!=null &&  !srcQualifiedName.equals(methodClsQualifiedName));
    }
}
