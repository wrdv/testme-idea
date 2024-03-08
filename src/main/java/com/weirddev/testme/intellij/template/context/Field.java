package com.weirddev.testme.intellij.template.context;

import com.intellij.psi.*;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import com.weirddev.testme.intellij.utils.JavaTypeUtils;
import lombok.Getter;

import java.util.List;

/**
 * Class field.
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Field {
    /**
     * type of field
     */
    @Getter private final Type type;

    /**
     * annotations of field
     */
    @Getter private final List<Type> annotations;

    /**
     * true - if field is inherited and overridden in this type
     */
    @Getter private final boolean overridden;
    /**
     * field has final modifier
     */
    @Getter private final boolean isFinal;
    /**
     * field is static
     */
    @Getter private final boolean isStatic;
    /**
     * canonical name of type owning this field
     */
    @Getter private final String ownerClassCanonicalName;
    /**
     * name given to field
     */
    @Getter private String name;

    public Field(PsiField psiField, PsiClass srcClass, TypeDictionary typeDictionary, int maxRecursionDepth) {
        this.name = psiField.getName();
        type= JavaTypeUtils.buildType(psiField.getType(), typeDictionary, maxRecursionDepth);
        annotations = JavaTypeUtils.buildAnnotations(psiField.getAnnotations(), typeDictionary, maxRecursionDepth);
        String canonicalText = srcClass.getQualifiedName();
        ownerClassCanonicalName = ClassNameUtils.stripArrayVarargsDesignator(canonicalText);
        overridden = isOverriddenInChild(psiField, srcClass);
        isFinal = psiField.getModifierList() != null && psiField.getModifierList().hasExplicitModifier(PsiModifier.FINAL);
        isStatic = psiField.getModifierList() != null && psiField.getModifierList().hasExplicitModifier(PsiModifier.STATIC);
    }

    /**
     *
     * @return true if the field annotations (Like @Resource, @Autowired) indicates it is di field
     */
    public boolean isDiField() {
        if (this.annotations.isEmpty()) {
            return false;
        }
        return annotations.stream().anyMatch(e -> {
            for (DiFieldAnnotationEnum annotationEnum : DiFieldAnnotationEnum.values()) {
                if (annotationEnum.getCanonicalName().equals(e.getCanonicalName())) {
                    return true;
                }
            }
            return false;
        });
    }

    private boolean isOverriddenInChild(PsiField psiField, PsiClass srcClass) {
        String srcQualifiedName = srcClass.getQualifiedName();
        String fieldClsQualifiedName = psiField.getContainingClass()==null?null:psiField.getContainingClass().getQualifiedName();
        return (srcQualifiedName!=null && fieldClsQualifiedName!=null &&  !srcQualifiedName.equals(fieldClsQualifiedName)) && srcClass.findFieldByName(psiField.getName(), false)!=null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;

        Field field = (Field) o;

        if (!type.equals(field.type)) return false;
        if (!ownerClassCanonicalName.equals(field.ownerClassCanonicalName)) return false;
        return name.equals(field.name);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + ownerClassCanonicalName.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Field{" + "type=" + type + ", overridden=" + overridden + ", isFinal=" + isFinal + ", isStatic=" + isStatic + ", ownerClassCanonicalName='" + ownerClassCanonicalName + '\'' + ", name='" + name + '\'' + '}';
    }
}
