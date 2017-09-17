package com.weirddev.testme.intellij.template.context;

import com.intellij.psi.*;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.psi.util.PsiUtil;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Type {
    private final String canonicalName;
    private final String name;
    private final boolean isPrimitive;
    private final String packageName;
    private final List<Type> composedTypes;
    private final boolean array;
    private final boolean varargs;
    private final List<String> enumValues;
    private final boolean isEnum;
    private final boolean isInterface;
    private final boolean isAbstract;
    private final boolean isStatic;
    private final boolean isFinal;
    private final List<Method> methods;//resolve Setters/Getters only for now
    /**
     * in case this is an inner class - the outer class where this type is defined
     */
    private final Type parentContainerClass;
    private final List<Field> fields;
    private boolean dependenciesResolved =false;
    private boolean dependenciesResolvable =false;
    private boolean hasDefaultConstructor=false;

    Type(String canonicalName, String name, String packageName, boolean isPrimitive, boolean isInterface, boolean isAbstract, boolean array, boolean varargs, List<Type> composedTypes) {
        this.canonicalName = canonicalName;
        this.name = name;
        this.isPrimitive = isPrimitive;
        this.packageName = packageName;
        this.isInterface = isInterface;
        this.isAbstract = isAbstract;
        this.array = array;
        this.varargs = varargs;
        this.composedTypes = composedTypes;
        enumValues = new ArrayList<String>();
        isEnum = false;
        methods=new ArrayList<Method>();
        fields=new ArrayList<Field>();
        parentContainerClass = null;
        isStatic = false;
        isFinal = false;
    }

    Type(String canonicalName) {
        this(ClassNameUtils.extractContainerType(canonicalName), ClassNameUtils.extractClassName(canonicalName), ClassNameUtils.extractPackageName(canonicalName),false, false,false, ClassNameUtils.isArray(canonicalName),ClassNameUtils.isVarargs(canonicalName),null);
    }

    public Type(PsiType psiType, @Nullable TypeDictionary typeDictionary, int maxRecursionDepth) {
        String canonicalText = psiType.getCanonicalText();
        array = ClassNameUtils.isArray(canonicalText);
        varargs = ClassNameUtils.isVarargs(canonicalText);
        canonicalName = ClassNameUtils.stripArrayVarargsDesignator(canonicalText);
        name = ClassNameUtils.extractClassName(ClassNameUtils.stripArrayVarargsDesignator(psiType.getPresentableText()));
        packageName = ClassNameUtils.extractPackageName(canonicalName);
        isPrimitive = psiType instanceof PsiPrimitiveType;
        composedTypes = resolveTypes(psiType,typeDictionary,maxRecursionDepth);
        PsiClass psiClass = PsiUtil.resolveClassInType(psiType);
        isEnum = psiClass != null && psiClass.isEnum();
        isInterface = psiClass != null && psiClass.isInterface();
        isAbstract = psiClass != null && psiClass.getModifierList()!=null &&  psiClass.getModifierList().hasModifierProperty(PsiModifier.ABSTRACT);
        isStatic = psiClass != null && psiClass.getModifierList() != null && psiClass.getModifierList().hasExplicitModifier(PsiModifier.STATIC);
        parentContainerClass = psiClass != null && psiClass.getParent()!=null && psiClass.getParent() instanceof PsiClass && typeDictionary!=null ? typeDictionary.getType(resolveType((PsiClass) psiClass.getParent()), maxRecursionDepth,
                false):null;
        fields = new ArrayList<Field>();
        enumValues = resolveEnumValues(psiType);
        dependenciesResolvable = maxRecursionDepth > 0;
        methods=new ArrayList<Method>();
        isFinal = isFinalType(PsiUtil.resolveClassInType(psiType));
    }

    private boolean isFinalType(PsiClass aClass) {
        return aClass != null &&  aClass.getModifierList()!=null && aClass.getModifierList().hasExplicitModifier(PsiModifier.FINAL);
    }

    private void resolveFields(@NotNull PsiClass psiClass) {
        for (PsiField psiField : psiClass.getAllFields()) {
            if(!"groovy.lang.MetaClass".equals(psiField.getType().getCanonicalText())){
                fields.add(new Field(psiField, psiClass));
            }
        }
    }

    @NotNull
    public static PsiClassType resolveType(PsiClass psiClass) {
        return JavaPsiFacade.getInstance(psiClass.getProject()).getElementFactory().createType(psiClass);
    }

    public void resolveDependencies(@Nullable TypeDictionary typeDictionary, int maxRecursionDepth, PsiType psiType, boolean shouldResolveAllMethods) {
        String canonicalText = psiType.getCanonicalText();
        PsiClass psiClass = PsiUtil.resolveClassInType(psiType);
        if (psiClass != null && maxRecursionDepth>0 && !canonicalText.startsWith("java.") /*todo consider replacing with just java.util.* || java.lang.*  */&& typeDictionary!=null) {
            if (psiClass.getConstructors().length == 0) {
                 hasDefaultConstructor=true; //todo check if parent ctors are also retrieved by getConstructors()
            }
            final PsiMethod[] methods = psiClass.getAllMethods();
                for (PsiMethod psiMethod : methods) {
                    if (Method.isRelevant(psiClass, psiMethod) &&  (shouldResolveAllMethods || (PropertyUtil.isSimplePropertySetter(psiMethod) || PropertyUtil.isSimplePropertyGetter(psiMethod)
                            || PropertyUtil.isSimpleSetter(psiMethod)|| PropertyUtil.isSimpleGetter(psiMethod)) && !isGroovyLangProperty(psiMethod) || psiMethod.isConstructor())) {

                        final Method method = new Method(psiMethod, psiClass, maxRecursionDepth - 1, typeDictionary);
                        method.resolveInternalReferences(psiMethod, typeDictionary);
                        this.methods.add(method);
                    }

                }
            resolveFields(psiClass);
            dependenciesResolved=true;
        }
    }

    private boolean isGroovyLangProperty(PsiMethod method) {
        final PsiParameter[] parameters = method.getParameterList().getParameters();
        if (parameters.length == 0) {
            return false;
        }
        final PsiParameter psiParameter = parameters[0];
        return "groovy.lang.MetaClass".equals(psiParameter.getType().getCanonicalText()) && "metaClass".equals(psiParameter.getName());
    }

    private static List<String> resolveEnumValues(PsiType psiType) {
        PsiClass psiClass = PsiUtil.resolveClassInType(psiType);
        List<String> enumValues = new ArrayList<String>();
        if (psiClass != null && psiClass.isEnum()) {
            for (PsiField field : psiClass.getFields()) {
                if (field instanceof PsiEnumConstant) {
                    final PsiEnumConstant enumConstant = (PsiEnumConstant) field;
                    final PsiEnumConstantInitializer initializingClass = enumConstant.getInitializingClass();
                    if (initializingClass == null) {
                        enumValues.add(enumConstant.getName());
                    }
                }
            }
        }
        return enumValues;
    }

    private List<Type> resolveTypes(PsiType psiType, TypeDictionary typeDictionary, int maxRecursionDepth) {
        ArrayList<Type> types = new ArrayList<Type>();
        if (typeDictionary!=null && psiType instanceof PsiClassType) {
            PsiClassType psiClassType = (PsiClassType) psiType;
            PsiType[] parameters = psiClassType.getParameters();
            if (parameters.length > 0) {
                for (PsiType parameter : parameters) {
                    types.add(typeDictionary.getType(parameter,maxRecursionDepth, false));
                }
            }
        }
        return types;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public String getPackageName() {
        return packageName;
    }

    public List<Type> getComposedTypes() {
        return composedTypes;
    }

    public boolean isArray() {
        return array;
    }

    public List<String> getEnumValues() {
        return enumValues;
    }

    public boolean isEnum() {
        return isEnum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Type)) return false;
        Type type = (Type) o;
        return canonicalName.equals(type.canonicalName);
    }

    @Override
    public int hashCode() {
        return canonicalName.hashCode();
    }

    public boolean isVarargs() {
        return varargs;
    }

    public List<Method> findConstructors() {
        List<Method> constructors = new ArrayList<Method>();
        for (Method method : methods) {
            if (method.isConstructor() && !"java.lang.Object".equals(method.getOwnerClassCanonicalType())) {
                constructors.add(method);
            }
        }
        Collections.sort(constructors, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) { //sort in reverse order by #no of c'tor params
                return o2.getMethodParams().size()-o1.getMethodParams().size();
            }
        });
        return constructors;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public boolean isDependenciesResolved() {
        return dependenciesResolved;
    }

    public boolean isHasDefaultConstructor() {
        return hasDefaultConstructor;
    }

    public boolean isDependenciesResolvable() {
        return dependenciesResolvable;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public Type getParentContainerClass() {
        return parentContainerClass;
    }

    public List<Field> getFields() {
        return fields;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public String toString() {
        return "Type{" + "canonicalName='" + canonicalName + '\'' + '}';
    }
}
