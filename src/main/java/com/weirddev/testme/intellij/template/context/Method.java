package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.weirddev.testme.intellij.builder.MethodBuilder;
import com.weirddev.testme.intellij.common.utils.PsiMethodUtils;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import com.weirddev.testme.intellij.utils.PropertyUtils;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A class Method.
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Method {
    /**
     * Method's return type
     */
    @Getter private final Type returnType;
    /**
     * method name
     */
    @Getter private final String name;
    /**
     * method owner type cannonical name
     */
    @Getter private final String ownerClassCanonicalType;
    /**
     * method arguments
     */
    @Getter private final List<Param> methodParams;
    /**
     * true - if method has private modifier
     */
    @Getter private final boolean isPrivate;
    /**
     * true - if method has protected modifier
     */
    @Getter private final boolean isProtected;
    /**
     * true - if method has default (package-private access modifier)
     */
    @Getter private final boolean isDefault;
    /**
     * true - if method has public modifier
     */
    @Getter private final boolean isPublic;
    /**
     * true - if method is abstract
     */
    @Getter private final boolean isAbstract;
    /**
     * true - if method defined as native
     */
    @Getter private final boolean isNative;
    /**
     * true - if this is a static method
     */
    @Getter private final boolean isStatic;
    /**
     * true - if method is a setter
     */
    @Getter private final boolean isSetter;
    /**
     * true - if method is a getter
     */
    @Getter private final boolean isGetter;
    /**
     * true - if method is a constructor
     */
    @Getter private final boolean constructor;
    /**
     * true - if method is overridden in child class
     */
    @Getter private final boolean overridden;
    /**
     * true - if method is inherited from parent class
     */
    @Getter private final boolean inherited;
    /**
     * true - if owner type is an interface
     */
    @Getter private final boolean isInInterface;
    /**
     * true -  if method is synthetically generated. common for scala methods
     */
    @Getter private final boolean isSynthetic;
    /**
     * the underlying field property name. relevant for getter/setter
     */
    @Getter private final String propertyName;
    /**
     *true - when accessible from class under test
     */
    @Getter private final boolean accessible;
    /**
     * true - is Primary Constructor (relevant for Scala)
     */
    @Getter private final boolean primaryConstructor;
    /**
     * methods called directly from this method
     */
    @Getter private final Set<MethodCall> directMethodCalls = new HashSet<>();
    /**
     * methods called directly from this method or on the call stack from this method via other methods belonging to the same type hierarchy
     */
    @Getter private Set<MethodCall> methodCalls = new HashSet<>();
    /**
     * methods referenced from this method. i.e.  SomeClassName::someMethodName
     */
    @Getter private final Set<Method> methodReferences = new HashSet<>();
    /*
     *  method calls of methods in this owner's class type or one of it's ancestor type. including indirectly called methods up to max method call search depth. ResolvedMethodCall objects of the class under test are deeply resolved
     *  @deprecated not used. might be removed
     */
//   @Getter  private final Set<MethodCall> calledFamilyMembers=new HashSet<MethodCall>();
    /**
     * references included in this method's implementation
     */
    @Getter private final Set<Reference> internalReferences = new HashSet<>();
    /**
     * formatted method id. a string used to uniquely discriminate this method from others
     */
    @Getter private final String methodId;
    /**
     *  Fields affected (assigned to) by methods called from this method. currently calculated only for constructors. i.e. when delegating to other constructors
     */
    @Getter private final Set<Field> indirectlyAffectedFields = new HashSet<>();

    //todo need to refactor. use a Factory method to create Method instances
    public Method(PsiMethod psiMethod, PsiClass srcClass, int maxRecursionDepth, TypeDictionary typeDictionary, @Nullable PsiType ownerClassPsiType) {
        isPrivate = psiMethod.hasModifierProperty(PsiModifier.PRIVATE);
        isProtected = psiMethod.hasModifierProperty(PsiModifier.PROTECTED);
        isDefault = psiMethod.hasModifierProperty(PsiModifier.DEFAULT) || psiMethod.hasModifierProperty(PsiModifier.PACKAGE_LOCAL);
        isPublic = psiMethod.hasModifierProperty(PsiModifier.PUBLIC);
        isAbstract = psiMethod.hasModifierProperty(PsiModifier.ABSTRACT);
        isNative = psiMethod.hasModifierProperty(PsiModifier.NATIVE);
        isStatic = psiMethod.hasModifierProperty(PsiModifier.STATIC);
        name = psiMethod.getName();
        ownerClassCanonicalType = psiMethod.getContainingClass() == null ? null : psiMethod.getContainingClass().getQualifiedName();
        constructor = psiMethod.isConstructor();
        primaryConstructor = constructor && psiMethod.getClass().getSimpleName().contains("PrimaryConstructor");
        isSetter = PropertyUtils.isPropertySetter(psiMethod);
        isGetter = PropertyUtils.isPropertyGetter(psiMethod);
//        final PsiField psiField = PropertyUtil.findPropertyFieldByMember(psiMethod);
//        propertyName = psiField == null ? null : psiField.getName();
        propertyName = ClassNameUtils.extractTargetPropertyName(name,isSetter,isGetter);
        if (srcClass != null) {
            overridden = MethodBuilder.isOverriddenInChild(psiMethod, srcClass);
            inherited = MethodBuilder.isInherited(psiMethod, srcClass);
        } else {
            overridden = false;
            inherited = false;
        }
        isInInterface = MethodBuilder.isInterface(psiMethod);
        methodId = PsiMethodUtils.formatMethodId(psiMethod);
        accessible = typeDictionary.isAccessible(psiMethod);
        isSynthetic = MethodBuilder.isSyntheticMethod(psiMethod);
        Optional<PsiSubstitutor> methodSubstitutor = findMethodSubstitutor(psiMethod, srcClass, ownerClassPsiType);
        returnType = MethodBuilder.resolveReturnType(psiMethod, maxRecursionDepth, typeDictionary, methodSubstitutor);
        methodParams = MethodBuilder.extractMethodParams(psiMethod, primaryConstructor, maxRecursionDepth, typeDictionary, methodSubstitutor);
    }

    /**
     *
     * true - if method has a return type
     */
    public boolean hasReturn(){
        return returnType != null && !"void".equals(returnType.getName());
    }

    boolean isTestable(){
        return !MethodBuilder.isLanguageInherited(ownerClassCanonicalType) && !isSetter() && !isGetter() && !isConstructor() &&((isDefault()|| isProtected() ) && !isInherited() || isPublic()) && !isOverridden() && !isInInterface() && !isAbstract() && !isSynthetic();
    }

    void resolveInternalReferences(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        if (!MethodBuilder.isLanguageInherited(getOwnerClassCanonicalType())) {
            this.directMethodCalls.addAll(MethodBuilder.resolveCalledMethods(psiMethod, typeDictionary));
            this.methodCalls = this.directMethodCalls;
            this.internalReferences.addAll(MethodBuilder.resolveReferences(psiMethod,typeDictionary));
            this.methodReferences.addAll(MethodBuilder.resolveMethodReferences(psiMethod,typeDictionary));
        }
    }

    public  Optional<PsiSubstitutor> findMethodSubstitutor(PsiMethod psiMethod, PsiClass srcClass, @Nullable  PsiType ownerClassPsiType) {
//        if (/*isInherited() && */(isTestable() || isConstructor()) && srcClass != null && hasGenericType(psiMethod)) {
        if (inherited && isTestable() && srcClass != null && MethodBuilder.hasGenericType(psiMethod)) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Method)) {
            return false;
        }
        return methodId.equals(((Method) o).methodId);
    }

    @Override
    public int hashCode() {
        return methodId.hashCode();
    }

    @Override
    public String toString() {
        return "Method{" + "methodId='" + methodId + '\'' + '}';
    }

}
