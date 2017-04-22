package com.weirddev.testme.intellij.template.context;

import com.intellij.psi.*;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
    private Set<Method> directlyCalledMethods = new HashSet<Method>();
    private Set<Method> calledMethods = new HashSet<Method>();
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
        methodParams = extractMethodParams(psiMethod.getParameterList(), typeDictionary, maxRecursionDepth);
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

    public void resolveCalledMethods(PsiMethod psiMethod, TypeDictionary typeDictionary) {
        if (!isInheritedFromObject(ownerClassCanonicalType)) {
            final Collection<PsiMethodCallExpression> psiMethodCallExpressions = PsiTreeUtil.findChildrenOfType(psiMethod, PsiMethodCallExpression.class);
            for (PsiMethodCallExpression psiMethodCallExpression : psiMethodCallExpressions) {
                final PsiMethod psiMethodResolved = psiMethodCallExpression.resolveMethod();
                if (psiMethodResolved != null) {
                    directlyCalledMethods.add(new Method(psiMethodResolved,null, 1,typeDictionary));
                }
            }
            calledMethods = directlyCalledMethods;
        }
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

    private List<Param> extractMethodParams(PsiParameterList parameterList, TypeDictionary typeDictionary, int maxRecursionDepth) {
        ArrayList<Param> params = new ArrayList<Param>();
        for (PsiParameter psiParameter : parameterList.getParameters()) {
            params.add(new Param(psiParameter,typeDictionary,maxRecursionDepth));
        }
        return params;
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
        return !isInheritedFromObject(ownerClassCanonicalType) && !isSetter && !isGetter && !constructor && ((isDefault || isProtected) && !inherited || isPublic) && !overridden && !isInInterface && !isAbstract;
    }

    public static boolean isInheritedFromObject(String ownerClassCanonicalType) {
        return "java.lang.Object".equals(ownerClassCanonicalType) || "groovy.lang.GroovyObjectSupport".equals(ownerClassCanonicalType);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Set<Method> getCalledMethods() {
        return calledMethods;
    }

    public Set<Method> getDirectlyCalledMethods() {
        return directlyCalledMethods;
    }

    public String getMethodId() {
        return methodId;
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
}
