package com.weirddev.testme.intellij.template.context;

import com.intellij.psi.*;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.weirddev.testme.intellij.groovy.GroovyPsiTreeUtils;
import com.weirddev.testme.intellij.groovy.ResolvedReference;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import com.weirddev.testme.intellij.utils.JavaPsiTreeUtils;
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
    private final String propertyName;
    private final boolean accessible; // true - when accessible from class under test
    private Set<MethodCall> directMethodCalls = new HashSet<MethodCall>();
    private Set<MethodCall> methodCalls = new HashSet<MethodCall>();//methods called directly from this method or on the call stack from this method via other methods belonging to the same type hierarchy
    /**
     *  method calls of methods in this owner's class type or one of it's ancestor type. including indirectly called methods up to max method call search depth. MethodCalled objects of the class under test are deeply resolved
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
        this.returnType = typeDictionary.getType(psiMethod.getReturnType(),maxRecursionDepth,true);
        name = psiMethod.getName();
        ownerClassCanonicalType = psiMethod.getContainingClass() == null ? null : psiMethod.getContainingClass().getQualifiedName();
        methodParams = extractMethodParams(typeDictionary, maxRecursionDepth,psiMethod);
        isSetter = PropertyUtil.isSimplePropertySetter(psiMethod)||PropertyUtil.isSimpleSetter(psiMethod);
        isGetter = PropertyUtil.isSimplePropertyGetter(psiMethod)||PropertyUtil.isSimpleGetter(psiMethod);
//        final PsiField psiField = PropertyUtil.findPropertyFieldByMember(psiMethod);
//        propertyName = psiField == null ? null : psiField.getName();
        propertyName = ClassNameUtils.extractTargetPropertyName(name,isSetter,isGetter);
        constructor = psiMethod.isConstructor();
        if (srcClass != null) {
            overridden = isOverriddenInChild(psiMethod, srcClass);
            inherited = isInherited(psiMethod, srcClass);
        } else {
            overridden = false;
            inherited = false;
        }
        isInInterface = psiMethod.getContainingClass() != null && psiMethod.getContainingClass().isInterface();
        methodId = formatMethodId(psiMethod);
        accessible = typeDictionary.isAccessible(psiMethod);
    }

    static String formatMethodId(PsiMethod psiMethod) {
        String name = psiMethod.getName();
        String ownerClassCanonicalType = psiMethod.getContainingClass() == null ? null : psiMethod.getContainingClass().getQualifiedName();
        return ownerClassCanonicalType + "." + name + "(" + formatMethodParams(psiMethod.getParameterList().getParameters()) +")";

    }

    static String formatMethodParams(PsiParameter[] parameters) {
        final StringBuilder sb = new StringBuilder();
        if (parameters != null) {
            for (PsiParameter parameter : parameters) {
                sb.append(parameter.getType().getCanonicalText()).append(",");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static boolean isRelevant(PsiClass psiClass, PsiMethod psiMethod) {
        boolean isRelevant = true;
        if (psiClass!=null && isInheritedFromObject(psiClass.getQualifiedName())) {
            isRelevant = false;
        }
        final String methodId = formatMethodId(psiMethod);
        if (GroovyPsiTreeUtils.isGroovy(psiMethod.getLanguage())
                && ( psiMethod.getClass().getCanonicalName().contains ("GrGdkMethodImpl") || methodId.endsWith(".invokeMethod(java.lang.String,java.lang.Object)") || methodId.endsWith(".getProperty(java.lang.String)") || methodId.endsWith(".setProperty(java.lang.String,java.lang.Object)"))) {
            isRelevant = false;
        }
        return isRelevant;
    }

    public void resolveInternalReferences(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        if (!isInheritedFromObject(getOwnerClassCanonicalType())) {
            resolveCalledMethods(psiMethod, typeDictionary);
            resolveReferences(psiMethod,typeDictionary);
        }
    }

    private void resolveReferences(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        if (GroovyPsiTreeUtils.isGroovy(psiMethod.getLanguage())) {
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
    private void resolveCalledMethods(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        if (GroovyPsiTreeUtils.isGroovy(psiMethod.getLanguage())) {
            for (GroovyPsiTreeUtils.MethodCalled methodCalled : GroovyPsiTreeUtils.findMethodCalls(psiMethod)) {
                if (isRelevant(methodCalled.getPsiMethod().getContainingClass(), methodCalled.getPsiMethod())) {
                    this.directMethodCalls.add(new MethodCall(new Method(methodCalled.getPsiMethod(), null, 1, typeDictionary),convertArgs(methodCalled.getMethodCallArguments())));
                }
            }
        } else {
            for (JavaPsiTreeUtils.MethodCalled methodCalled : JavaPsiTreeUtils.findMethodCalls(psiMethod)) {
                if (isRelevant(methodCalled.getPsiMethod().getContainingClass(), methodCalled.getPsiMethod())) {
                    this.directMethodCalls.add(new MethodCall(new Method(methodCalled.getPsiMethod(), methodCalled.getPsiMethod().getContainingClass(), 1, typeDictionary),methodCalled.getMethodCallArguments()));
                }
            }

        }
        methodCalls = this.directMethodCalls;
    }

    private List<MethodCallArgument> convertArgs(List<String> methodCallArguments) {
        final ArrayList<MethodCallArgument> methodCallArgs = new ArrayList<MethodCallArgument>();
        if (methodCallArguments != null) {
            for (String methodCallArgument : methodCallArguments) {
                methodCallArgs.add(new MethodCallArgument(methodCallArgument));
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
        for (PsiParameter psiParameter : psiMethod.getParameterList().getParameters()) {
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
                if (GroovyPsiTreeUtils.isGroovy(element.getLanguage())) {
                    psiField = GroovyPsiTreeUtils.resolveGrLeftHandExpressionAsField(element);
                }
                else if (element instanceof PsiExpression && !PsiUtil.isOnAssignmentLeftHand((PsiExpression) element)) {
                    psiField = resolveLeftHandExpressionAsField((PsiExpression) element);
                }
                if (psiField != null) {
                    fields.add(new Field(psiField, psiField.getContainingClass()));
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
        return !isInheritedFromObject(ownerClassCanonicalType) && !isSetter() && !isGetter() && !isConstructor() &&((isDefault()|| isProtected() ) && !isInherited() || isPublic()) && !isOverridden() && !isInInterface() && !isAbstract();
    }

    public static boolean isInheritedFromObject(String ownerClassCanonicalType) {
        return "java.lang.Object".equals(ownerClassCanonicalType) || "groovy.lang.GroovyObjectSupport".equals(ownerClassCanonicalType);
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
        return "Method{" + "returnType=" + returnType + ", name='" + name + '\'' + ", ownerClassCanonicalType='" + ownerClassCanonicalType + '\'' + ", methodParams=" + methodParams + ", isPrivate=" + isPrivate + ", isProtected=" + isProtected + "," +
                " isDefault=" + isDefault + ", isPublic=" + isPublic + ", isAbstract=" + isAbstract + ", isNative=" + isNative + ", isStatic=" + isStatic + ", isSetter=" + isSetter + ", isGetter=" + isGetter + ", constructor=" + constructor + ", " +
                "overridden=" + overridden + ", inherited=" + inherited + ", isInInterface=" + isInInterface + ", propertyName='" + propertyName + '\'' + ", directMethodCalls=" + directMethodCalls +
                ", internalReferences=" + internalReferences + ", methodId='" + methodId + '\'' + '}';
    }
    public Set<Field> getIndirectlyAffectedFields() {
        return indirectlyAffectedFields;
    }

    public boolean isAccessible() {
        return accessible;
    }
}
