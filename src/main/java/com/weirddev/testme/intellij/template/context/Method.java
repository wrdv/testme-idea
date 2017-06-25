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
    private Set<MethodCall> directMethodCalls = new HashSet<MethodCall>();
    private Set<MethodCall> methodCalls = new HashSet<MethodCall>();//methods called directly from this method or on the call stack from this method via other methods belonging to the same type hierarchy
    private final Set<MethodCall> calledFamilyMembers=new HashSet<MethodCall>();//called other methods of this method owner's class type or one of it's ancestor type. MethodCalled objects of the class under test have more data resolved such as internalReferences
    private Set<Reference> internalReferences = new HashSet<Reference>();
    private final String methodId;

    public Method(PsiMethod psiMethod, PsiClass srcClass, int maxRecursionDepth,TypeDictionary typeDictionary) {
        isPrivate = psiMethod.hasModifierProperty(PsiModifier.PRIVATE);
        isProtected = psiMethod.hasModifierProperty(PsiModifier.PROTECTED);
        isDefault = psiMethod.hasModifierProperty(PsiModifier.DEFAULT) || psiMethod.hasModifierProperty(PsiModifier.PACKAGE_LOCAL);
        isPublic = psiMethod.hasModifierProperty(PsiModifier.PUBLIC);
        isAbstract = psiMethod.hasModifierProperty(PsiModifier.ABSTRACT);
        isNative = psiMethod.hasModifierProperty(PsiModifier.NATIVE);
        isStatic = psiMethod.hasModifierProperty(PsiModifier.STATIC);
        this.returnType = typeDictionary.getType(psiMethod.getReturnType(),maxRecursionDepth);
        name = psiMethod.getName();
        ownerClassCanonicalType = psiMethod.getContainingClass() == null ? null : psiMethod.getContainingClass().getQualifiedName();
        methodParams = extractMethodParams(psiMethod.getParameterList(), typeDictionary, maxRecursionDepth,psiMethod);
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
        methodId = formatMethodId();
    }

    private String formatMethodId() {
        return ownerClassCanonicalType + "." + name + "(" +formatMathodParams(methodParams) +")";

    }

    private String formatMathodParams(List<Param> methodParams) {
        final StringBuilder sb = new StringBuilder();
        for (Param methodParam : methodParams) {
            sb.append(methodParam.getType().getCanonicalName()).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
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
            for (PsiMethod method : GroovyPsiTreeUtils.findMethodCalls(psiMethod)) {
                this.directMethodCalls.add(new MethodCall(new Method(method, null, 1, typeDictionary),null));//todo handle for groovy as well after *.context migrated to a new module
            }
        } else {
            List<JavaPsiTreeUtils.MethodCalled> psiMethods = JavaPsiTreeUtils.findMethodCalls(psiMethod);
            for (JavaPsiTreeUtils.MethodCalled methodCall : psiMethods) {
                this.directMethodCalls.add(new MethodCall(new Method(methodCall.getPsiMethod(), null, 1, typeDictionary),methodCall.getMethodCallArguments()));
            }

        }
        methodCalls = this.directMethodCalls;
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

    private List<Param> extractMethodParams(PsiParameterList parameterList, TypeDictionary typeDictionary, int maxRecursionDepth, PsiMethod psiMethod) {
        ArrayList<Param> params = new ArrayList<Param>();
        for (PsiParameter psiParameter : parameterList.getParameters()) {
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
                if (element instanceof PsiExpression && !PsiUtil.isOnAssignmentLeftHand((PsiExpression)element)) {
                    final PsiField psiField = resolveLeftHandExpressionAsField((PsiExpression) element);
                    if (psiField != null) {
                        fields.add(new Field(psiField, psiField.getContainingClass()));
                    }
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

    public Set<MethodCall> getCalledFamilyMembers() {
        return calledFamilyMembers;
    }

    @Override
    public String toString() {
        return "Method{" + "returnType=" + returnType + ", name='" + name + '\'' + ", ownerClassCanonicalType='" + ownerClassCanonicalType + '\'' + ", methodParams=" + methodParams + ", isPrivate=" + isPrivate + ", isProtected=" + isProtected + "," +
                " isDefault=" + isDefault + ", isPublic=" + isPublic + ", isAbstract=" + isAbstract + ", isNative=" + isNative + ", isStatic=" + isStatic + ", isSetter=" + isSetter + ", isGetter=" + isGetter + ", constructor=" + constructor + ", " +
                "overridden=" + overridden + ", inherited=" + inherited + ", isInInterface=" + isInInterface + ", propertyName='" + propertyName + '\'' + ", directMethodCalls=" + directMethodCalls +
                ", internalReferences=" + internalReferences + ", methodId='" + methodId + '\'' + '}';
    }
}
