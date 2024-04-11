package com.weirddev.testme.intellij.template;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.weirddev.testme.intellij.builder.MethodFactory;
import com.weirddev.testme.intellij.cache.Cache;
import com.weirddev.testme.intellij.cache.LruCache;
import com.weirddev.testme.intellij.common.utils.LanguageUtils;
import com.weirddev.testme.intellij.common.utils.PsiMethodUtils;
import com.weirddev.testme.intellij.resolvers.to.ResolvedMethodCall;
import com.weirddev.testme.intellij.template.context.Type;
import com.weirddev.testme.intellij.utils.JavaTypeUtils;
import com.weirddev.testme.intellij.utils.TypeUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date: 26/11/2016
 *
 * @author Yaron Yamin
 */
public class TypeDictionary {
    private static final Logger LOG = Logger.getInstance(TypeDictionary.class.getName());
    private static final int MAX_RELEVANT_METHOD_IDS_CACHE = 5000;
    private final Cache<String, Boolean> relevantMethodIdsCache;
    private final long startTimestamp;
    private final Set<String> testSubjectTypesNames;
    Map<String, Type> typeDictionary = new HashMap<>();//todo standardize as Cache
    private final PsiClass testSubjectClass;
    private final PsiPackage targetPackage;
    private final Set<ResolvedMethodCall> methodCallsFromTestSubject;
    private final List<String> testSubjectMethodParamsType;
    private AtomicInteger newTypeCounter = new AtomicInteger();
    private AtomicInteger existingTypeHitsCounter = new AtomicInteger();
    private boolean throwSpecificExceptionTypes;

    private TypeDictionary(PsiClass srcClass, PsiPackage targetPackage, Set<ResolvedMethodCall> methodCallsFromTestSubject, List<String> testSubjectMethodParamsType,boolean throwSpecificExceptionTypes) {
        this.testSubjectClass = srcClass;
        this.testSubjectTypesNames = resolveTypesNames(srcClass);
        this.targetPackage = targetPackage;
        this.methodCallsFromTestSubject = methodCallsFromTestSubject;
        this.testSubjectMethodParamsType = testSubjectMethodParamsType;
        this.relevantMethodIdsCache = new LruCache<>(MAX_RELEVANT_METHOD_IDS_CACHE);
        startTimestamp = System.currentTimeMillis();
        this.throwSpecificExceptionTypes = throwSpecificExceptionTypes;
    }

    private Set<String> resolveTypesNames(PsiClass srcClass) {
        HashSet<String> typesNames = new HashSet<>();
        for(PsiClass clazz = srcClass; clazz != null && !TypeUtils.isLanguageBaseClass(clazz.getQualifiedName()); clazz = clazz.getSuperClass()) {
            typesNames.add(clazz.getQualifiedName());
        }
        return typesNames;
    }

    public static TypeDictionary create(PsiClass srcClass, PsiPackage targetPackage,boolean throwSpecificExceptionTypes){
        Set<ResolvedMethodCall> methodCallsFromTestSubject = new HashSet<>();
        if (srcClass != null) {
            for (PsiMethod method : srcClass.getAllMethods()) {
                List<ResolvedMethodCall> methodCalls = MethodFactory.resolvedMethodCalls(method);
                LOG.debug("resolved method calls ", method.getName(), methodCalls);
                methodCallsFromTestSubject.addAll(methodCalls);
            }
        }
        List<String> testSubjectMethodParamsType = srcClass == null ? List.of() :  Arrays.stream(srcClass.getAllMethods()).flatMap(psiMethod1 -> Arrays.stream(psiMethod1.getParameterList().getParameters()).map(p -> p.getType().getCanonicalText()).filter(TypeUtils::isBasicType)).toList();
        return new TypeDictionary(srcClass, targetPackage, methodCallsFromTestSubject, testSubjectMethodParamsType,throwSpecificExceptionTypes);
    }

    /**
     * check if method might be relevant for constructs in unit test
     *
     * @param psiMethod                  method to check
     * @param psiClass                   owner class of method
     * @return true if method might be relevant for constructs in unit test
     */
    public boolean isRelevant(PsiMethod psiMethod, @Nullable PsiClass psiClass) {
        return relevantMethodIdsCache.getOrCompute(PsiMethodUtils.formatMethodId(psiMethod), () -> computeIsRelevant(psiMethod, psiClass) );
//        return computeIsRelevant(psiMethod, psiClass);
    }

    private boolean computeIsRelevant(PsiMethod psiMethod, @Nullable PsiClass psiClass) {
        final PsiClass ownerClass = psiMethod.getContainingClass() == null ? psiClass : psiMethod.getContainingClass();
        final String methodId = PsiMethodUtils.formatMethodId(psiMethod);
        if(!isTestSubject(ownerClass) && !calledFromTestSubject(methodId) && !isCtorOfUsedType(psiMethod)){
            return false;//todo ConvertedBean c'tor not called from test subject but is used in test subject - so it's c'tor is relevant
        }
        if (LanguageUtils.isGroovy(psiMethod.getLanguage())
                && (psiMethod.getClass().getCanonicalName().contains("GrGdkMethodImpl") ||
                methodId.endsWith(".invokeMethod(java.lang.String,java.lang.Object)") || methodId.endsWith(".getProperty(java.lang.String)") ||
                methodId.endsWith(".setProperty(java.lang.String,java.lang.Object)"))) {
            return false;
        }
        if (ownerClass != null && TypeUtils.isLanguageBaseClass(ownerClass.getQualifiedName())) {
            return false;
        }

// todo Need to resolve methods of mocked dependencies from imported libs. consider performance hit..

//         if(ownerClass!=null && ownerClass.getQualifiedName()!=null){
//            JavaPsiFacade facade = JavaPsiFacade.getInstance( ownerClass.getProject());
//            PsiClass[] possibleClasses = facade.findClasses(ownerClass.getQualifiedName(), GlobalSearchScope.projectScope(ownerClass.getProject()));// todo - test with GlobalSearchScope.allScope(ownerClass.getProject()). Alt. skip the check ?
//            if (possibleClasses.length == 0) {
//                return false;
//            }
//        }
        return true;
    }

    private boolean isCtorOfUsedType(PsiMethod psiMethod) {
        return psiMethod.isConstructor() && psiMethod.getContainingClass() != null && testSubjectMethodParamsType.contains(psiMethod.getContainingClass().getQualifiedName())  ;
    }

    private boolean calledFromTestSubject(String methodId) {
        return methodCallsFromTestSubject.stream().anyMatch(resolvedMethodCall -> resolvedMethodCall.getMethodId().equals(methodId));
    }

    public  boolean isTestSubject(PsiClass psiClass) {
        return psiClass != null && testSubjectTypesNames.contains(psiClass.getQualifiedName());
    }

    @Nullable
    public Type getType(PsiType psiType, int maxRecursionDepth, boolean shouldResolveAllMethods) {
        return getTypeInternal(psiType, maxRecursionDepth, shouldResolveAllMethods, null);
    }

    @Nullable
    public Type getType(PsiClass psiClass, int maxRecursionDepth, boolean shouldResolveAllMethods) {
        return getTypeInternal(psiClass, maxRecursionDepth, shouldResolveAllMethods, null);
    }

    public Type getType(PsiType type, int maxRecursionDepth, boolean shouldResolveAllMethods, Object element) {
        return getTypeInternal(type, maxRecursionDepth, shouldResolveAllMethods, element);
    }

    @Nullable
    private Type getTypeInternal(Object element, int maxRecursionDepth, boolean shouldResolveAllMethods, Object typeElement) {
        Type type = null;
        String canonicalText = JavaTypeUtils.resolveCanonicalName(element, typeElement);
        if  (canonicalText != null) {
            type = typeDictionary.get(canonicalText);
            if (type == null || !type.isDependenciesResolvable() && shouldResolveAllMethods && maxRecursionDepth > 1) {
                LOG.debug(newTypeCounter.incrementAndGet() + ". Creating new Type for:" + canonicalText + " maxRecursionDepth:" + maxRecursionDepth);
                if (element instanceof PsiType) {
                    final PsiType psiType = (PsiType) element;
                    type = new Type(psiType, typeElement, this, maxRecursionDepth, shouldResolveAllMethods);
                    typeDictionary.put(canonicalText, type);
                    type.resolveDependencies(this, maxRecursionDepth, psiType, shouldResolveAllMethods);
                } else if (element instanceof PsiClass) {
                    final PsiClass psiClass = (PsiClass) element;
                    type = new Type(psiClass, this, maxRecursionDepth, shouldResolveAllMethods);
                    typeDictionary.put(canonicalText, type);
                }
            } else {
                LOG.debug(existingTypeHitsCounter.incrementAndGet() + ". Found existing Type for:" + canonicalText + " maxRecursionDepth:" + maxRecursionDepth);
            }
        }
        return type;
    }

    public boolean isAccessible(PsiMethod psiMethod) {
        return PsiUtil.isAccessibleFromPackage(psiMethod, targetPackage) && (psiMethod.getContainingClass() == null || PsiUtil.isAccessibleFromPackage(psiMethod.getContainingClass(), targetPackage));
    }
    public void logStatistics() {
        LOG.info("**** Statistics: took %dms. type hits/req:%d/%d method relevancy cache %s".formatted(
                startTimestamp - System.currentTimeMillis(),newTypeCounter.get(), newTypeCounter.get() + existingTypeHitsCounter.get(), relevantMethodIdsCache.getUsageStats()));
    }

    public boolean isThrowSpecificExceptionTypes() {
        return throwSpecificExceptionTypes;
    }

    public void setThrowSpecificExceptionTypes(boolean throwSpecificExceptionTypes) {
        this.throwSpecificExceptionTypes = throwSpecificExceptionTypes;
    }
}
