package com.weirddev.testme.intellij.template;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.util.PsiUtil;
import com.weirddev.testme.intellij.template.utils.ClassUtils;
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
    private final List<String> enumValues;
    private final boolean isEnum;
    private final List<Method> constructors=new ArrayList<Method>();

    Type(String canonicalName, String name, String packageName, boolean isPrimitive, boolean array, List<Type> composedTypes) {
        this.canonicalName = canonicalName;
        this.name = name;
        this.isPrimitive = isPrimitive;
        this.packageName = packageName;
        this.array = array;
        this.composedTypes = composedTypes;
        enumValues = new ArrayList<String>();
        isEnum = false;
        //todo consider introducing a type in this hierarchy that doesn't have constructors
    }

    Type(String canonicalName) {
        this(ClassUtils.extractContainerType(canonicalName), ClassUtils.extractClassName(canonicalName), ClassUtils.extractPackageName(canonicalName),false, ClassUtils.isArray(canonicalName),null);
    }

    public Type(PsiType psiType, @Nullable TypeDictionary typeDictionary, int maxRecursionDepth) {
        String canonicalText = psiType.getCanonicalText();
        array = ClassUtils.isArray(canonicalText);
        this.canonicalName = ClassUtils.stripArrayDesignator(canonicalText);
        this.name = ClassUtils.stripArrayDesignator(psiType.getPresentableText());
        packageName = ClassUtils.extractPackageName(canonicalName);
        this.isPrimitive = psiType instanceof PsiPrimitiveType;
        composedTypes = resolveTypes(psiType);
        PsiClass psiClass = PsiUtil.resolveClassInType(psiType);
        isEnum = psiClass != null && psiClass.isEnum();
        enumValues = resolveEnumValues(psiType);
        if (psiClass != null && maxRecursionDepth>0 && !canonicalText.startsWith("java.") /*todo consider replacing with just java.util.* || java.lang.*  */&& typeDictionary!=null) {
            for (PsiMethod psiMethod : psiClass.getConstructors()) {
                if (typeDictionary.isAccessible(psiMethod)) {
                    constructors.add(new Method(psiMethod,psiClass, maxRecursionDepth-1, typeDictionary));
                }
            }
            Collections.sort(constructors, new Comparator<Method>() {
                @Override
                public int compare(Method o1, Method o2) { //sort in reverse order by #no of c'tor params
                    return o2.getMethodParams().size()-o1.getMethodParams().size();
                }
            });
        }
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

    private List<Type> resolveTypes(PsiType psiType) {
        ArrayList<Type> types = new ArrayList<Type>();
        resolveTypes(psiType, types);
        return types;
    }

    private void resolveTypes(PsiType psiType, ArrayList<Type> types) {
        types.add(new Type(psiType.getCanonicalText()));
        if (psiType instanceof PsiClassReferenceType) {
            PsiClassReferenceType psiClassReferenceType = (PsiClassReferenceType) psiType;
            PsiType[] parameters = psiClassReferenceType.getParameters();
            if (parameters.length > 0) {
                for (PsiType parameter : parameters) {
                    resolveTypes(parameter, types);
                }
            }
        }
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
        if (o == null || getClass() != o.getClass()) return false;

        Type type = (Type) o;

        if (isPrimitive != type.isPrimitive) return false;
        if (array != type.array) return false;
        if (isEnum != type.isEnum) return false;
        if (canonicalName != null ? !canonicalName.equals(type.canonicalName) : type.canonicalName != null)
            return false;
        if (name != null ? !name.equals(type.name) : type.name != null) return false;
        if (packageName != null ? !packageName.equals(type.packageName) : type.packageName != null) return false;
        if (composedTypes != null ? !composedTypes.equals(type.composedTypes) : type.composedTypes != null)
            return false;
        return enumValues != null ? enumValues.equals(type.enumValues) : type.enumValues == null;

    }

    @Override
    public int hashCode() {
        int result = canonicalName != null ? canonicalName.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (isPrimitive ? 1 : 0);
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (composedTypes != null ? composedTypes.hashCode() : 0);
        result = 31 * result + (array ? 1 : 0);
        result = 31 * result + (enumValues != null ? enumValues.hashCode() : 0);
        result = 31 * result + (isEnum ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Type{" +
                "canonicalName='" + canonicalName + '\'' +
                ", name='" + name + '\'' +
                ", isPrimitive=" + isPrimitive +
                ", packageName='" + packageName + '\'' +
                ", composedTypes=" + composedTypes +
                ", array=" + array +
                '}';
    }

    public List<Method> getConstructors() {
        return constructors;
    }

}
