package com.weirddev.testme.intellij.template.context;

import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.weirddev.testme.intellij.common.utils.PsiMethodUtils;
import com.weirddev.testme.intellij.groovy.resolvers.GroovyPsiTreeUtils;
import com.weirddev.testme.intellij.common.utils.LanguageUtils;
import com.weirddev.testme.intellij.resolvers.to.MethodCallArg;
import com.weirddev.testme.intellij.resolvers.to.ResolvedMethodCall;
import com.weirddev.testme.intellij.resolvers.to.ResolvedReference;
import com.weirddev.testme.intellij.scala.resolvers.ScalaPsiTreeUtils;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import com.weirddev.testme.intellij.utils.JavaPsiTreeUtils;
import com.weirddev.testme.intellij.utils.PropertyUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Method {
    private final Type returnType;
    private final String name;
    private final String ownerClassCanonicalType;
    private final List<Param> methodParams;
    private final boolean isPrivate;
    private final boolean isProtected;
    private final boolean isDefault;
    private final boolean isPublic;
    private final boolean isAbstract;
    private final boolean isNative;
    private final boolean isStatic;
    private final boolean isSetter;
    private final boolean isGetter;
    private final boolean constructor;
    private final boolean overridden;
    private final boolean inherited;
    private final boolean isInInterface;
    /**
     * true, if method is synthetically generated. currently only relevant for scala methods
     */
    private final boolean isSynthetic;
    private final String propertyName;
    /**
     *true - when accessible from class under test
     */
    private final boolean accessible;
    /**
     * true - is Primary Constructor (relevant for Scala)
     */
    private final boolean primaryConstructor;
    /**
     * methods called directly from this method
     */
    private Set<MethodCall> directMethodCalls = new HashSet<MethodCall>();
    /**
     * methods called directly from this method or on the call stack from this method via other methods belonging to the same type hierarchy
     */
    private Set<MethodCall> methodCalls = new HashSet<MethodCall>();
    /**
     * methods referenced from this method. i.e.  SomeClassName::someMethodName
     */
    private Set<Method> methodReferences = new HashSet<Method>();
    /**
     *  method calls of methods in this owner's class type or one of it's ancestor type. including indirectly called methods up to max method call search depth. ResolvedMethodCall objects of the class under test are deeply resolved
     *  @deprecated not used. might be removed
     */
    private final Set<MethodCall> calledFamilyMembers=new HashSet<MethodCall>(); /*todo consider removing*/
    private Set<Reference> internalReferences = new HashSet<Reference>();
    private final String methodId;
    private final Set<Field> indirectlyAffectedFields = new HashSet<Field>(); //Fields affected(assigned to ) by methods called from this method. calculated only for ctors. i.e. when delegating to other ctors

    public Method(PsiMethod psiMethod, PsiClass srcClass, int maxRecursionDepth,TypeDictionary typeDictionary) {
        isPrivate = psiMethod.hasModifierProperty(PsiModifier.PRIVATE);
        isProtected = psiMethod.hasModifierProperty(PsiModifier.PROTECTED);
        isDefault = psiMethod.hasModifierProperty(PsiModifier.DEFAULT) || psiMethod.hasModifierProperty(PsiModifier.PACKAGE_LOCAL);
        isPublic = psiMethod.hasModifierProperty(PsiModifier.PUBLIC);
        isAbstract = psiMethod.hasModifierProperty(PsiModifier.ABSTRACT);
        isNative = psiMethod.hasModifierProperty(PsiModifier.NATIVE);
        isStatic = psiMethod.hasModifierProperty(PsiModifier.STATIC);
        name = psiMethod.getName();
        this.returnType = resolveReturnType(psiMethod, maxRecursionDepth, typeDictionary);
        ownerClassCanonicalType = psiMethod.getContainingClass() == null ? null : psiMethod.getContainingClass().getQualifiedName();
        methodParams = extractMethodParams(typeDictionary, maxRecursionDepth,psiMethod);
        isSetter = PropertyUtils.isPropertySetter(psiMethod);
        isGetter = PropertyUtils.isPropertyGetter(psiMethod);
//        final PsiField psiField = PropertyUtil.findPropertyFieldByMember(psiMethod);
//        propertyName = psiField == null ? null : psiField.getName();
        propertyName = ClassNameUtils.extractTargetPropertyName(name,isSetter,isGetter);
        constructor = psiMethod.isConstructor();
        primaryConstructor = constructor && psiMethod.getClass().getSimpleName().contains("PrimaryConstructor");
        if (srcClass != null) {
            overridden = isOverriddenInChild(psiMethod, srcClass);
            inherited = isInherited(psiMethod, srcClass);
        } else {
            overridden = false;
            inherited = false;
        }
        isInInterface = psiMethod.getContainingClass() != null && psiMethod.getContainingClass().isInterface();
        methodId = PsiMethodUtils.formatMethodId(psiMethod);
        accessible = typeDictionary.isAccessible(psiMethod);
        isSynthetic = isSyntheticMethod(psiMethod);
    }

    private boolean isSyntheticMethod(PsiMethod psiMethod) {
        if (LanguageUtils.isScala(psiMethod.getLanguage())) {
            return ScalaPsiTreeUtils.isSyntheticMethod(psiMethod);
        } else {
            return false;
        }
    }

    @Nullable
    public Type resolveReturnType(PsiMethod psiMethod, int maxRecursionDepth, TypeDictionary typeDictionary) {
        final PsiType psiType = psiMethod.getReturnType();
        if (psiType == null) {
            return null;
        } else {
            Object typeElement = null;
            if (LanguageUtils.isScala(psiMethod.getLanguage())) {
                typeElement = ScalaPsiTreeUtils.resolveReturnType(psiMethod);
            }
            return typeDictionary.getType(psiType, maxRecursionDepth, true,typeElement);
        }
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
            } else if(ownerClass!=null && ownerClass.getQualifiedName()!=null){
                JavaPsiFacade facade = JavaPsiFacade.getInstance( ownerClass.getProject());
                PsiClass[] possibleClasses = facade.findClasses(ownerClass.getQualifiedName(), GlobalSearchScope.projectScope(( ownerClass.getProject())));
                if (possibleClasses.length == 0) {
                    isRelevant = false;
                }
            }
        }
        return isRelevant;
    }

    public void resolveInternalReferences(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        if (!isLanguageInherited(getOwnerClassCanonicalType())) {
            resolveCalledMethods(psiMethod, typeDictionary);
            resolveReferences(psiMethod,typeDictionary);
            resolveMethodReferences(psiMethod,typeDictionary);
        }
    }

    private void resolveReferences(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        if (LanguageUtils.isGroovy(psiMethod.getLanguage())) {
            for (ResolvedReference resolvedReference : GroovyPsiTreeUtils.findReferences(psiMethod)) {
                internalReferences.add(new Reference(resolvedReference.getReferenceName(), resolvedReference.getRefType(), resolvedReference.getPsiOwnerType(), typeDictionary));
            }
        }
        else {
            for (ResolvedReference resolvedReference : JavaPsiTreeUtils.findReferences(psiMethod)) {
                internalReferences.add(new Reference(resolvedReference.getReferenceName(), resolvedReference.getRefType(), resolvedReference.getPsiOwnerType(), typeDictionary));
            }
        }
    }
    private void resolveMethodReferences(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        for (PsiMethod resolvedMethodReference : JavaPsiTreeUtils.findMethodReferences(psiMethod)) {
            if (isRelevant(resolvedMethodReference.getContainingClass(), resolvedMethodReference)) {
                this.methodReferences.add(new Method(resolvedMethodReference, resolvedMethodReference.getContainingClass(), 1, typeDictionary));
            }
        }
    }
    private void resolveCalledMethods(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        if (LanguageUtils.isGroovy(psiMethod.getLanguage())) {
            for (ResolvedMethodCall resolvedMethodCall : GroovyPsiTreeUtils.findMethodCalls(psiMethod)) {
                if (isRelevant(resolvedMethodCall.getPsiMethod().getContainingClass(), resolvedMethodCall.getPsiMethod())) {
                    this.directMethodCalls.add(new MethodCall(new Method(resolvedMethodCall.getPsiMethod(), null, 1, typeDictionary),convertArgs(resolvedMethodCall.getMethodCallArguments())));
                }
            }
        } else {
            for (ResolvedMethodCall methodCalled : JavaPsiTreeUtils.findMethodCalls(psiMethod)) {
                if (isRelevant(methodCalled.getPsiMethod().getContainingClass(), methodCalled.getPsiMethod())) {
                    this.directMethodCalls.add(new MethodCall(new Method(methodCalled.getPsiMethod(), methodCalled.getPsiMethod().getContainingClass(), 1, typeDictionary),convertArgs(methodCalled.getMethodCallArguments())));
                }
            }
        }
        methodCalls = this.directMethodCalls;
    }

    private List<MethodCallArgument> convertArgs(List<MethodCallArg> methodCallArguments) {
        final ArrayList<MethodCallArgument> methodCallArgs = new ArrayList<MethodCallArgument>();
        if (methodCallArguments != null) {
            for (MethodCallArg methodCallArgument : methodCallArguments) {
                methodCallArgs.add(new MethodCallArgument(methodCallArgument.getText()));
            }
        }
        return methodCallArgs;
    }

    private boolean isOverriddenInChild(PsiMethod method, PsiClass srcClass) {
        String srcQualifiedName = srcClass.getQualifiedName();
        String methodClsQualifiedName = method.getContainingClass()==null?null:method.getContainingClass().getQualifiedName();
        return (srcQualifiedName!=null && methodClsQualifiedName!=null &&  !srcQualifiedName.equals(methodClsQualifiedName)) && srcClass.findMethodsBySignature(method, false).length > 0;
    }
    private boolean isInherited(PsiMethod method, PsiClass srcClass) {
        String srcQualifiedName = srcClass.getQualifiedName();
        String methodClsQualifiedName = method.getContainingClass()==null?null:method.getContainingClass().getQualifiedName();
        return (srcQualifiedName!=null && methodClsQualifiedName!=null &&  !srcQualifiedName.equals(methodClsQualifiedName));
    }

    private List<Param> extractMethodParams(TypeDictionary typeDictionary, int maxRecursionDepth, PsiMethod psiMethod) {
        ArrayList<Param> params = new ArrayList<Param>();
        final PsiParameter[] parameters;
        if (LanguageUtils.isScala(psiMethod.getLanguage())) {
            parameters = ScalaPsiTreeUtils.resolveParameters(psiMethod);
        } else {
            parameters = psiMethod.getParameterList().getParameters();
        }
        for (PsiParameter psiParameter : parameters) {
            final ArrayList<Field> assignedToFields = findMatchingFields(psiParameter, psiMethod);
            params.add(new Param(psiParameter,typeDictionary,maxRecursionDepth,assignedToFields));
        }
        return params;
    }
    private static ArrayList<Field> findMatchingFields(PsiParameter psiParameter, PsiMethod psiMethod) {
        final ArrayList<Field> fields = new ArrayList<Field>();
        if (!psiMethod.hasModifierProperty(PsiModifier.STATIC)) {
            for (PsiReference reference : ReferencesSearch.search(psiParameter, new LocalSearchScope(new PsiMethod[]{psiMethod}))) {
                final PsiElement element = reference.getElement();
                PsiField psiField = null;
                if (LanguageUtils.isGroovy(element.getLanguage())) {
                    psiField = GroovyPsiTreeUtils.resolveGrLeftHandExpressionAsField(element);
                }
                else if (element instanceof PsiExpression && !PsiUtil.isOnAssignmentLeftHand((PsiExpression) element)) {
                    psiField = resolveLeftHandExpressionAsField((PsiExpression) element);
                }
                if (psiField != null) {
                    fields.add(new Field(psiField, psiField.getContainingClass(),null, 0));
                }
            }
        }
        return fields;
    }

    public static PsiField resolveLeftHandExpressionAsField(@NotNull PsiExpression expr) {
        PsiElement parent = PsiTreeUtil.skipParentsOfType(expr, PsiParenthesizedExpression.class);
        if (!(parent instanceof PsiAssignmentExpression)) {
            return null;
        }
        final PsiAssignmentExpression psiAssignmentExpression = (PsiAssignmentExpression) parent;
        final PsiReference reference = psiAssignmentExpression.getLExpression().getReference();
        final PsiElement element = reference != null ? reference.resolve() : null;
        return element == null || !(element instanceof PsiField) ? null : (PsiField)element ;
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<Param> getMethodParams() {
        return methodParams;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public boolean isNative() {
        return isNative;
    }

    public boolean isStatic() {
        return isStatic;
    }
    @Nullable
    public String getOwnerClassCanonicalType() {
        return ownerClassCanonicalType;
    }

    public boolean isSetter() {
        return isSetter;
    }

    public boolean isGetter() {
        return isGetter;
    }

    public boolean isConstructor() {
        return constructor;
    }

    public boolean isOverridden() {
        return overridden;
    }

    public boolean isInherited() {
        return inherited;
    }

    public boolean isInInterface() {
        return isInInterface;
    }

    public boolean isTestable(){
        return !isLanguageInherited(ownerClassCanonicalType) && !isSetter() && !isGetter() && !isConstructor() &&((isDefault()|| isProtected() ) && !isInherited() || isPublic()) && !isOverridden() && !isInInterface() && !isAbstract() && !isSynthetic();
    }

    public static boolean isLanguageInherited(String ownerClassCanonicalType) {
        return "java.lang.Object".equals(ownerClassCanonicalType) || "java.lang.Class".equals(ownerClassCanonicalType) || "groovy.lang.GroovyObjectSupport".equals(ownerClassCanonicalType);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Set<MethodCall> getMethodCalls() {
        return methodCalls;
    }

    public String getMethodId() {
        return methodId;
    }

    public Set<Reference> getInternalReferences() {
        return internalReferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Method)) return false;

        Method method = (Method) o;

        return methodId.equals(method.methodId);
    }

    @Override
    public int hashCode() {
        return methodId.hashCode();
    }

    public Set<MethodCall> getCalledFamilyMembers() {//todo get rid of this
        return calledFamilyMembers;
    }

    @Override
    public String toString() {
        return "Method{" + "methodId='" + methodId + '\'' + '}';
    }

    public Set<Field> getIndirectlyAffectedFields() {
        return indirectlyAffectedFields;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public Set<Method> getMethodReferences() {
        return methodReferences;
    }

    public boolean hasReturn(){
        return returnType != null && !"void".equals(returnType.getName());
    }

    public boolean isPrimaryConstructor() {
        return primaryConstructor;
    }

    public boolean isSynthetic() {
        return isSynthetic;
    }
}
