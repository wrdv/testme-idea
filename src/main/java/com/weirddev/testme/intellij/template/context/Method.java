package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.search.GlobalSearchScope;
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
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import com.weirddev.testme.intellij.utils.JavaPsiTreeUtils;
import com.weirddev.testme.intellij.utils.PropertyUtils;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

/**
 * A class Method.
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Method {
    private static final Logger LOG = Logger.getInstance(Method.class.getName());
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
            overridden = isOverriddenInChild(psiMethod, srcClass);
            inherited = isInherited(psiMethod, srcClass);
        } else {
            overridden = false;
            inherited = false;
        }
        isInInterface = isInterface(psiMethod);
        methodId = PsiMethodUtils.formatMethodId(psiMethod);
        accessible = typeDictionary.isAccessible(psiMethod);
        isSynthetic = isSyntheticMethod(psiMethod);
        Optional<PsiSubstitutor> methodSubstitutor = findMethodSubstitutor(psiMethod, srcClass, ownerClassPsiType);
        returnType = resolveReturnType(psiMethod, maxRecursionDepth, typeDictionary, methodSubstitutor);
        methodParams = extractMethodParams(psiMethod, primaryConstructor, maxRecursionDepth, typeDictionary, methodSubstitutor);
    }

    static boolean isRelevant(PsiClass psiClass, PsiMethod psiMethod) {
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

    /**
     *
     * true - if method has a return type
     */
    public boolean hasReturn(){
        return returnType != null && !"void".equals(returnType.getName());
    }

    boolean isTestable(){
        return !isLanguageInherited(ownerClassCanonicalType) && !isSetter() && !isGetter() && !isConstructor() &&((isDefault()|| isProtected() ) && !isInherited() || isPublic()) && !isOverridden() && !isInInterface() && !isAbstract() && !isSynthetic();
    }

    void resolveInternalReferences(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        if (!isLanguageInherited(getOwnerClassCanonicalType())) {
            resolveCalledMethods(psiMethod, typeDictionary);
            resolveReferences(psiMethod,typeDictionary);
            resolveMethodReferences(psiMethod,typeDictionary);
        }
    }

    private static boolean isLanguageInherited(String ownerClassCanonicalType) {
        return "java.lang.Object".equals(ownerClassCanonicalType) || "java.lang.Class".equals(ownerClassCanonicalType) || "groovy.lang.GroovyObjectSupport".equals(ownerClassCanonicalType);
    }

    @Nullable
    private Type resolveReturnType(PsiMethod psiMethod, int maxRecursionDepth, TypeDictionary typeDictionary, Optional<PsiSubstitutor> methodSubstitutor) {
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

    private static PsiField resolveLeftHandExpressionAsField(PsiExpression expr) {
        PsiElement parent = PsiTreeUtil.skipParentsOfType(expr, PsiParenthesizedExpression.class);
        if (!(parent instanceof PsiAssignmentExpression)) {
            return null;
        }
        final PsiAssignmentExpression psiAssignmentExpression = (PsiAssignmentExpression) parent;
        final PsiReference reference = psiAssignmentExpression.getLExpression().getReference();
        final PsiElement element = reference != null ? reference.resolve() : null;
        return element == null || !(element instanceof PsiField) ? null : (PsiField)element ;
    }
    private boolean isInterface(PsiMethod psiMethod) {
//            //method inherited from an interface but implemented by this interface should not be considered as interface method
//            return psiMethod.hasModifierProperty("abstract") || psiMethod.getContainingClass() != null && psiMethod.getContainingClass().isInterface();
        return psiMethod.hasModifierProperty(PsiModifier.ABSTRACT);
    }

    private boolean isSyntheticMethod(PsiMethod psiMethod) {
        if (LanguageUtils.isScala(psiMethod.getLanguage())) {
            return ScalaPsiTreeUtils.isSyntheticMethod(psiMethod);
        } else {
            return false;
        }
    }
    private Optional<PsiSubstitutor> findMethodSubstitutor(PsiMethod psiMethod, PsiClass srcClass, @Nullable  PsiType ownerClassPsiType) {
//        if (/*isInherited() && */(isTestable() || isConstructor()) && srcClass != null && hasGenericType(psiMethod)) {
        if (isInherited() && isTestable() && srcClass != null && hasGenericType(psiMethod)) {
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

    private boolean hasGenericType(PsiMethod psiMethod) {
        return Stream.concat(Stream.of(psiMethod.getParameterList().getParameters()).map(PsiVariable::getType), Stream.of(psiMethod.getReturnType())).anyMatch(this::mayContainTypeParameter);
    }

    private boolean mayContainTypeParameter(PsiType psiType) {
        return psiType instanceof PsiClassReferenceType/* && ((PsiClassReferenceType) psiType).resolve() instanceof PsiTypeParameter */;
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
                this.methodReferences.add(new Method(resolvedMethodReference, resolvedMethodReference.getContainingClass(), 1, typeDictionary, null));
            }
        }
    }
    private void resolveCalledMethods(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        //todo try to pass/support src class in scala/groovy as well. if successful, consider re-implementing with a factory method call
        if (LanguageUtils.isGroovy(psiMethod.getLanguage())) {
            for (ResolvedMethodCall resolvedMethodCall : GroovyPsiTreeUtils.findMethodCalls(psiMethod)) {
                addDirectMethodCallIfRelevant(typeDictionary, resolvedMethodCall, null);
            }
        }
        else if (LanguageUtils.isScala(psiMethod.getLanguage())) {
            for (ResolvedMethodCall resolvedMethodCall : ScalaPsiTreeUtils.findMethodCalls(psiMethod)) {
                addDirectMethodCallIfRelevant(typeDictionary, resolvedMethodCall, null);
            }
        }
        else {
            for (ResolvedMethodCall methodCalled : JavaPsiTreeUtils.findMethodCalls(psiMethod)) {
                addDirectMethodCallIfRelevant(typeDictionary, methodCalled, methodCalled.getPsiMethod().getContainingClass());
            }
        }
        methodCalls = this.directMethodCalls;
    }

    private void addDirectMethodCallIfRelevant(TypeDictionary typeDictionary, ResolvedMethodCall methodCalled, PsiClass srcClass) {
        if (isRelevant(methodCalled.getPsiMethod().getContainingClass(), methodCalled.getPsiMethod())) {
            this.directMethodCalls.add(new MethodCall(new Method(methodCalled.getPsiMethod(), srcClass, 1, typeDictionary, null),convertArgs(methodCalled.getMethodCallArguments())));
        }
    }

    private List<MethodCallArgument> convertArgs(List<MethodCallArg> methodCallArguments) {
        final ArrayList<MethodCallArgument> methodCallArgs = new ArrayList<>();
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
        if (srcQualifiedName == null || methodClsQualifiedName == null || srcQualifiedName.equals(methodClsQualifiedName)) {
            return false;
        }
        else{
            final PsiMethod childMethod = MethodSignatureUtil.findMethodBySuperMethod(srcClass, method, false);
            return childMethod != null;
        }
    }

    private boolean isInherited(PsiMethod method, PsiClass srcClass) {
        String srcQualifiedName = srcClass.getQualifiedName();
        String methodClsQualifiedName = method.getContainingClass()==null?null:method.getContainingClass().getQualifiedName();
        return (srcQualifiedName!=null && methodClsQualifiedName!=null &&  !srcQualifiedName.equals(methodClsQualifiedName));
    }

    private List<Param> extractMethodParams(PsiMethod psiMethod, boolean shouldResolveAllMethods, int maxRecursionDepth, TypeDictionary typeDictionary, Optional<PsiSubstitutor> methodSubstitutor) {
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
                for (PsiReference reference : ReferencesSearch.search(psiParameter, new LocalSearchScope(new PsiMethod[]{psiMethod}))) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Method)) {
            return false;
        }

        Method method = (Method) o;

        return methodId.equals(method.methodId);
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
