package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.weirddev.testme.intellij.builder.MethodFactory;
import com.weirddev.testme.intellij.common.utils.LanguageUtils;
import com.weirddev.testme.intellij.scala.resolvers.ScalaPsiTreeUtils;
import com.weirddev.testme.intellij.scala.resolvers.ScalaTypeUtils;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import com.weirddev.testme.intellij.utils.JavaPsiTreeUtils;
import com.weirddev.testme.intellij.utils.JavaTypeUtils;
import com.weirddev.testme.intellij.utils.PropertyUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A used type - object or primitive.
 *
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
@Getter @ToString(onlyExplicitlyIncluded = true) @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Type {
    /**
     * Full canonical name including package
     */
    @EqualsAndHashCode.Include @ToString.Include
    private final String canonicalName;
    /**
     * Type's name
     */
    private final String name;
    /**
     * true - if this type is a primitive. i.e. int, boolean
     */
    private final boolean isPrimitive;
    /**
     * Type's package
     */
    private final String packageName;
    /**
     * Used types of generic params if relevant
     */
    private final List<Type> composedTypes;
    /**
     * true when type is an array
     */
    private final boolean array;
    /**
     * no. of array dimensions. relevant only in case this type is an array
     */
    private final int arrayDimensions;
    /**
     * true when this type is a varar
     */
    private final boolean varargs;
    /**
     * true if this type is an enum
     */
    private final boolean isEnum;
    /**
     * enum name values. relevant for enum types
     */
    private final List<String> enumValues;
    /**
     * true if this type is an interface
     */
    private final boolean isInterface;
    /**
     * true if this type is an abstract class
     */
    private final boolean isAbstract;
    /**
     * true if this is a static type/class or a Scala Object
     */
    private final boolean isStatic;
    /**
     * true if type id defined as final
     */
    private final boolean isFinal;
    /**
     * in case this is an inner class - the outer class where this type is defined
     */
    private final Type parentContainerClass;
    /**
     * true when all constructor dependencies, if exists, have been resolved by object graph introspection
     */
    private boolean dependenciesResolved =false;
    /**
     * used internally when building object graph
     */
    private boolean dependenciesResolvable =false;
    /**
     * true when type has a default constructor
     */
    private boolean hasDefaultConstructor=false;
    /**
     * true - if this is a scala case class
     */
    private final boolean caseClass;
    /**
     * true - if this is a scala sealed class
     */
    private final boolean sealed;
    /**
     * type's methods if relevant
     */
    private final List<Method> methods;
    /**
     * fields defined for this type
     */
    private final List<Field> fields;
    /**
     * interfaces implemented by this type if any
     */
    private final List<Type> implementedInterfaces = new ArrayList<>();
    /**
     * relevant of scala sealed classes
     */
    private final List<String> childObjectsQualifiedNames;

    @Deprecated
    public Type(String canonicalName, String name, String packageName, boolean isPrimitive, boolean isInterface, boolean isAbstract, boolean array, int arrayDimensions, boolean varargs, List<Type> composedTypes) {
        this.canonicalName = canonicalName;
        this.name = name;
        this.isPrimitive = isPrimitive;
        this.packageName = packageName;
        this.isInterface = isInterface;
        this.isAbstract = isAbstract;
        this.array = array;
        this.arrayDimensions = arrayDimensions;
        this.varargs = varargs;
        this.composedTypes = composedTypes;
        enumValues = new ArrayList<>();
        isEnum = false;
        methods= new ArrayList<>();
        fields= new ArrayList<>();
        parentContainerClass = null;
        isStatic = false;
        isFinal = false;
        caseClass = false;
        sealed = false;
        childObjectsQualifiedNames = new ArrayList<>();
    }


    public Type(PsiType psiType, @Nullable Object typePsiElement, @Nullable TypeDictionary typeDictionary, int maxRecursionDepth, boolean shouldResolveAllMethods) {
        String canonicalText = JavaTypeUtils.resolveCanonicalName(psiType,typePsiElement);
        array = ClassNameUtils.isArray(canonicalText);
        arrayDimensions = ClassNameUtils.arrayDimensions(canonicalText);
        varargs = ClassNameUtils.isVarargs(canonicalText);
        canonicalName = ClassNameUtils.stripArrayVarargsDesignator(canonicalText);
        name = ClassNameUtils.extractClassName(ClassNameUtils.stripArrayVarargsDesignator(psiType.getPresentableText()));
        packageName = ClassNameUtils.extractPackageName(canonicalName);
        isPrimitive = psiType instanceof PsiPrimitiveType;
        composedTypes = resolveTypes(psiType, typePsiElement,typeDictionary, maxRecursionDepth);
        PsiClass psiClass = PsiUtil.resolveClassInType(psiType);
        isEnum = JavaPsiTreeUtils.resolveIfEnum(psiClass);
        isInterface = psiClass != null && psiClass.isInterface();
        isAbstract = psiClass != null && psiClass.getModifierList() != null && psiClass.getModifierList().hasModifierProperty(PsiModifier.ABSTRACT);
        isStatic = hasModifier(psiClass, PsiModifier.STATIC) || psiClass!=null && "org.jetbrains.plugins.scala.lang.psi.impl.toplevel.typedef.ScObjectImpl".equals(psiClass.getClass().getCanonicalName());
        parentContainerClass = psiClass != null && psiClass.getParent() != null && psiClass.getParent() instanceof PsiClass && typeDictionary != null ? typeDictionary.getType(resolveType((PsiClass) psiClass.getParent()), maxRecursionDepth,
                false) : null;
        fields = new ArrayList<>();
        enumValues = JavaPsiTreeUtils.resolveEnumValues(psiClass,typePsiElement);
        dependenciesResolvable = shouldResolveAllMethods && maxRecursionDepth > 1;
        methods = new ArrayList<>();
        isFinal = isFinalType(psiClass);
        caseClass = psiClass != null && LanguageUtils.isScala(psiClass.getLanguage()) && ScalaTypeUtils.isCaseClass(psiClass);
        sealed = psiClass != null && LanguageUtils.isScala(psiClass.getLanguage()) && ScalaTypeUtils.isSealed(psiClass);
        childObjectsQualifiedNames = sealed ? ScalaPsiTreeUtils.findChildObjectsQualifiedNameInFile(psiClass):new ArrayList<>();
    }

    public Type(PsiClass psiClass, TypeDictionary typeDictionary, int maxRecursionDepth, boolean shouldResolveAllMethods) {
        String canonicalText = JavaTypeUtils.resolveCanonicalName(psiClass, null);
        array = ClassNameUtils.isArray(canonicalText);
        arrayDimensions = ClassNameUtils.arrayDimensions(canonicalText);
        varargs = ClassNameUtils.isVarargs(canonicalText);
        canonicalName = ClassNameUtils.stripArrayVarargsDesignator(canonicalText);
        name = psiClass.getQualifiedName() == null ? null : ClassNameUtils.extractClassName(ClassNameUtils.stripArrayVarargsDesignator(psiClass.getQualifiedName()));
        packageName = ClassNameUtils.extractPackageName(canonicalName);
        isPrimitive = false;
        composedTypes = new ArrayList<>();
        isEnum = psiClass.isEnum();
        isInterface = psiClass.isInterface();
        isAbstract = psiClass.getModifierList() != null && psiClass.getModifierList().hasModifierProperty(PsiModifier.ABSTRACT);
        isStatic = psiClass.getModifierList() != null && psiClass.getModifierList().hasExplicitModifier(PsiModifier.STATIC);
        parentContainerClass = psiClass.getParent() != null && psiClass.getParent() instanceof PsiClass && typeDictionary != null ? typeDictionary.getType(resolveType((PsiClass) psiClass.getParent()), maxRecursionDepth,
                false) : null;
        fields = new ArrayList<>();
        enumValues = JavaPsiTreeUtils.resolveEnumValues(psiClass, null);
        dependenciesResolvable = shouldResolveAllMethods && maxRecursionDepth > 1;
        methods = new ArrayList<>();
        isFinal = isFinalType(psiClass);
        caseClass = LanguageUtils.isScala(psiClass.getLanguage()) && ScalaTypeUtils.isCaseClass(psiClass);
        sealed = LanguageUtils.isScala(psiClass.getLanguage()) && ScalaTypeUtils.isSealed(psiClass);
        childObjectsQualifiedNames = sealed ? ScalaPsiTreeUtils.findChildObjectsQualifiedNameInFile(psiClass):new ArrayList<>();
    }

    @NotNull
    public static PsiClassType resolveType(PsiClass psiClass) {
        return JavaPsiFacade.getInstance(psiClass.getProject()).getElementFactory().createType(psiClass);
    }

    public void resolveDependencies(@Nullable TypeDictionary typeDictionary, int maxRecursionDepth, PsiType psiType, boolean shouldResolveAllMethods) {
        PsiClass psiClass = PsiUtil.resolveClassInType(psiType);
        //Need to resolve methods of dependant libs for mocking. consider performance hit...
//        String canonicalText = psiType.getCanonicalText();
//        if (psiClass != null && maxRecursionDepth >0 && !canonicalText.startsWith("java.") && !canonicalText.startsWith("scala.") /*todo consider replacing with just java.util.* || java.lang.*  */&& typeDictionary !=null) {
        if (psiClass != null && maxRecursionDepth >0 && typeDictionary !=null) {
            if (psiClass.getConstructors().length == 0) {
                 hasDefaultConstructor=true; //todo check if parent ctors are also retrieved by getConstructors()
            }
            for (PsiMethod psiMethod : psiClass.getAllMethods()) {
                    if ( (shouldResolveAllMethods || ( PropertyUtils.isPropertySetter(psiMethod) || PropertyUtils.isPropertyGetter(psiMethod)) && !isGroovyLangProperty(psiMethod) || psiMethod.isConstructor()) && MethodFactory.isRelevant(psiMethod, psiClass)){
                        final Method method = MethodFactory.createMethod(psiMethod, psiClass, maxRecursionDepth - 1, typeDictionary, psiType);
                        MethodFactory.resolveInternalReferences(typeDictionary, psiMethod, method);
                        this.methods.add(method);
                    }

                }
            resolveFields(psiClass, typeDictionary, maxRecursionDepth - 1);
            resolveImplementedInterfaces(psiClass, typeDictionary, shouldResolveAllMethods, maxRecursionDepth - 1);
            dependenciesResolved=true;
        }
    }
    private void resolveFields(@NotNull PsiClass psiClass, TypeDictionary typeDictionary, int maxRecursionDepth) {
        for (PsiField psiField : psiClass.getAllFields()) {
            if(!"groovy.lang.MetaClass".equals(psiField.getType().getCanonicalText())){
                fields.add(new Field(psiField, psiClass,typeDictionary,maxRecursionDepth));
            }
        }
    }
    private void resolveImplementedInterfaces(@NotNull PsiClass psiClass, TypeDictionary typeDictionary, boolean shouldResolveAllMethods, int maxRecursionDepth) {
        for (PsiClassType psiClassType : psiClass.getImplementsListTypes()) {
            implementedInterfaces.add(new Type(psiClassType, null,typeDictionary, maxRecursionDepth, shouldResolveAllMethods));
        }
    }

    private boolean isFinalType(PsiClass aClass) {
        return hasModifier(aClass, PsiModifier.FINAL);
    }

    private boolean isGroovyLangProperty(PsiMethod method) {
        final PsiParameter[] parameters = method.getParameterList().getParameters();
        if (parameters.length == 0) {
            return false;
        }
        final PsiParameter psiParameter = parameters[0];
        return "groovy.lang.MetaClass".equals(psiParameter.getType().getCanonicalText()) && "metaClass".equals(psiParameter.getName());
    }

    private List<Type> resolveTypes(PsiType psiType, Object typeElement, TypeDictionary typeDictionary, int maxRecursionDepth) {
        List<Type> types = new ArrayList<>();
        if (typeDictionary!=null && psiType instanceof PsiClassType) {
            PsiClassType psiClassType = (PsiClassType) psiType;
            PsiType[] parameters = psiClassType.getParameters();
            if (parameters.length > 0) {
//                List<PsiClass> psiClasses = null;
//                if (typeElement!=null &&  typeElement instanceof PsiElement &&  LanguageUtils.isScala(((PsiElement)typeElement).getLanguage())) {
//                    final PsiElement typePsiElement = (PsiElement) typeElement;
//                    psiClasses = ScalaPsiTreeUtils.resolveComposedTypes(psiType, typePsiElement);
//                    if (psiClasses != null && psiClasses.size() != parameters.length) {
//                        psiClasses = null;
//                    }
//                }
//                else
                List<Object> composedTypeElements = null;
                if(typeElement!=null && LanguageUtils.isScalaPluginObject(typeElement)){
                    composedTypeElements = ScalaPsiTreeUtils.resolveComposedTypeElementsForObject(psiType, typeElement);
                    if (composedTypeElements != null && composedTypeElements.size() != parameters.length) {
                        composedTypeElements = null;
                    }
                }
//                if (psiClasses == null || psiClasses.isEmpty()) {
                    for (int i = 0; i < parameters.length; i++) {
                        PsiClass psiClass = null;
                        final PsiType psiTypeArg = parameters[i];
                        final Object composedElement = composedTypeElements == null ? null : composedTypeElements.get(i);
                        if (composedTypeElements !=null && "java.lang.Object".equals(psiTypeArg.getCanonicalText())/* && !"Object".equals(psiClasses.get(i).getQualifiedName())*/) {
//                            types.add(typeDictionary.getType(psiClasses.get(i), maxRecursionDepth, false));
/*
                            if (composedElement instanceof ScClass) {
                                final ScClass scClass = (ScClass) composedElement;
                                final TypeResult<ScType> typeWithProjections = scClass.getTypeWithProjections(((ScClass) composedElement).getType$default$1(), true);
                                if (!typeWithProjections.isEmpty()) {
                                    typeWithProjections.get().canonicalText();
                                }
//                                ((ScClassImpl) composedElement).getTypeWithProjections(((ScClassImpl) composedElement).getType$default$1(),true).get()
                            }
*/
                            String canonicalText = JavaTypeUtils.resolveCanonicalName(psiTypeArg, composedElement);
                            final Project project = psiType.getResolveScope()==null?null:psiType.getResolveScope().getProject();
                            psiClass = ScalaPsiTreeUtils.resolvePsiClass(canonicalText,project,null);
                        }
                        if (psiClass == null) {
                            types.add(typeDictionary.getType(psiTypeArg, maxRecursionDepth, false, composedElement));
                        } else {
                            types.add(typeDictionary.getType(psiClass, maxRecursionDepth, false));
                        }
                    }
            }
        }
        return types;
    }

    private boolean hasModifier(PsiClass psiClass, String aStatic) {
        return psiClass != null && psiClass.getModifierList() != null && psiClass.getModifierList().hasExplicitModifier(aStatic);
    }

    /**
     * Find methods that are constructors
     * @return Type's constructors sorted in revers order by no. of constructor params
     */
    public List<Method> findConstructors() {
        List<Method> constructors = new ArrayList<>();
        for (Method method : methods) {
            if (method.isConstructor() && !"java.lang.Object".equals(method.getOwnerClassCanonicalType())) {
                constructors.add(method);
            }
        }
        constructors.sort((o1, o2) -> { //sort in reverse order by #no of c'tor params
            return o2.getMethodParams().size() - o1.getMethodParams().size();
        });
        return constructors;
    }

}
