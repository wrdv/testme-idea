package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.weirddev.testme.intellij.common.utils.LanguageUtils;
import com.weirddev.testme.intellij.scala.resolvers.ScalaPsiTreeUtils;
import com.weirddev.testme.intellij.scala.resolvers.ScalaTypeUtils;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import com.weirddev.testme.intellij.utils.JavaPsiTreeUtils;
import com.weirddev.testme.intellij.utils.JavaTypeUtils;
import com.weirddev.testme.intellij.utils.PropertyUtils;
import lombok.Getter;
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
public class Type {
    /**
     * Full canonical name including package
     */
    @Getter
    private final String canonicalName;
    /**
     * Type's name
     */
    @Getter private final String name;
    /**
     * true - if this type is a primitive. i.e. int, boolean
     */
    @Getter private final boolean isPrimitive;
    /**
     * Type's package
     */
    @Getter private final String packageName;
    /**
     * Used types of generic params if relevant
     */
    @Getter private final List<Type> composedTypes;
    /**
     * true when type is an array
     */
    @Getter private final boolean array;
    /**
     * true when this type is a varar
     */
    @Getter private final boolean varargs;
    /**
     * enum name values. relevant for enum types
     */
    @Getter private final List<String> enumValues;
    /**
     * true if this type is an enum
     */
    @Getter private final boolean isEnum;
    /**
     * true if this type is an interface
     */
    @Getter private final boolean isInterface;
    /**
     * true if this type is an abstract class
     */
    @Getter private final boolean isAbstract;
    /**
     * true if this is a static type/class or a Scala Object
     */
    @Getter private final boolean isStatic;
    /**
     * true if type id defined as final
     */
    @Getter private final boolean isFinal;
    /**
     * types methods if relevant
     */
    @Getter private final List<Method> methods;
    /**
     * in case this is an inner class - the outer class where this type is defined
     */
    @Getter private final Type parentContainerClass;
    /**
     * fields defined for this type
     */
    @Getter private final List<Field> fields;
    /**
     * true when all constructor dependencies, if exists, have been resolved by object graph introspection
     */
    @Getter private boolean dependenciesResolved =false;
    /**
     * used internally when building object graph
     */
    @Getter private boolean dependenciesResolvable =false;
    /**
     * true when type has a default constructor
     */
    @Getter private boolean hasDefaultConstructor=false;
    /**
     * interfaces implemented by this type if any
     */
    @Getter private List<Type> implementedInterfaces = new ArrayList<>();
    /**
     * true - if this is a scala case class
     */
    @Getter private final boolean caseClass;
    /**
     * true - if this is a scala sealed class
     */
    @Getter private final boolean sealed;
    /**
     * relevant of scala sealed classes
     */
    @Getter private final List<String> childObjectsQualifiedNames;

    public Type(String canonicalName, String name, String packageName, boolean isPrimitive, boolean isInterface, boolean isAbstract, boolean array, boolean varargs, List<Type> composedTypes) {
        this.canonicalName = canonicalName;
        this.name = name;
        this.isPrimitive = isPrimitive;
        this.packageName = packageName;
        this.isInterface = isInterface;
        this.isAbstract = isAbstract;
        this.array = array;
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

    Type(String canonicalName) {
        this(ClassNameUtils.extractContainerType(canonicalName), ClassNameUtils.extractClassName(canonicalName), ClassNameUtils.extractPackageName(canonicalName),false, false,false, ClassNameUtils.isArray(canonicalName),ClassNameUtils.isVarargs(canonicalName),null);
    }

    public Type(PsiType psiType, Object typePsiElement, @Nullable TypeDictionary typeDictionary, int maxRecursionDepth, boolean shouldResolveAllMethods) {
        String canonicalText = JavaTypeUtils.resolveCanonicalName(psiType,typePsiElement);
        array = ClassNameUtils.isArray(canonicalText);
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
        resolveDependencies(typeDictionary, maxRecursionDepth, PsiUtil.resolveClassInType(psiType), psiType.getCanonicalText(), shouldResolveAllMethods);
    }

    public void resolveDependencies(@Nullable TypeDictionary typeDictionary, int maxRecursionDepth, PsiClass psiClass, String canonicalText, boolean shouldResolveAllMethods) {
        if (psiClass != null && maxRecursionDepth>0 && !canonicalText.startsWith("java.") && !canonicalText.startsWith("scala.") /*todo consider replacing with just java.util.* || java.lang.*  */&& typeDictionary!=null) {
            if (psiClass.getConstructors().length == 0) {
                 hasDefaultConstructor=true; //todo check if parent ctors are also retrieved by getConstructors()
            }
            final PsiMethod[] methods = psiClass.getAllMethods();
                for (PsiMethod psiMethod : methods) {
                    if ( (shouldResolveAllMethods || ( PropertyUtils.isPropertySetter(psiMethod) || PropertyUtils.isPropertyGetter(psiMethod)) && !isGroovyLangProperty(psiMethod) || psiMethod.isConstructor()) && Method.isRelevant(psiClass, psiMethod)){
                        final Method method = new Method(psiMethod, psiClass, maxRecursionDepth - 1, typeDictionary);
                        method.resolveInternalReferences(psiMethod, typeDictionary);
                        this.methods.add(method);
                    }

                }
            resolveFields(psiClass,typeDictionary,maxRecursionDepth - 1);
            resolveImplementedInterfaces(psiClass,typeDictionary,shouldResolveAllMethods,maxRecursionDepth - 1);
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
//                        else{
                            types.add(typeDictionary.getType(psiTypeArg, maxRecursionDepth, false, composedElement));
                        } else {
                            types.add(typeDictionary.getType(psiClass, maxRecursionDepth, false));
                        }
                    }
//                } else {
//                    for (PsiClass psiClass : psiClasses) {
//                        types.add(typeDictionary.getType(psiClass, maxRecursionDepth, false));
//                    }
//                }
            }
        }
        return types;
    }

    private boolean hasModifier(PsiClass psiClass, String aStatic) {
        return psiClass != null && psiClass.getModifierList() != null && psiClass.getModifierList().hasExplicitModifier(aStatic);
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

    @Override
    public String toString() {
        return "Type{" + "canonicalName='" + canonicalName + '\'' + '}';
    }

}
