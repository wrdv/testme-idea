package com.weirddev.testme.intellij.template;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.weirddev.testme.intellij.template.context.Type;
import com.weirddev.testme.intellij.utils.JavaTypeUtils;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date: 26/11/2016
 *
 * @author Yaron Yamin
 */
public class TypeDictionary {
    private static final Logger LOG = Logger.getInstance(TypeDictionary.class.getName());
    Map<String, Type> typeDictionary = new HashMap<String, Type>();
    private final PsiClass srcClass;
    private final PsiPackage targetPackage;
    private AtomicInteger newTypeCounter = new AtomicInteger();
    private AtomicInteger existingTypeHitsCounter = new AtomicInteger();

    public TypeDictionary(PsiClass srcClass, PsiPackage targetPackage) {
        this.srcClass = srcClass;
        this.targetPackage = targetPackage;
    }

    @Nullable
    public Type getType(PsiType psiType, int maxRecursionDepth, boolean shouldResolveAllMethods) {
        return getTypeInternal(psiType, maxRecursionDepth, shouldResolveAllMethods, null);
    }

    @Nullable
    public Type getType(PsiClass psiClass, int maxRecursionDepth, boolean shouldResolveAllMethods) {
        return getTypeInternal(psiClass, maxRecursionDepth, shouldResolveAllMethods, null);
    }

    public Type getType(PsiType type, int maxRecursionDepth, boolean shouldResolveAllMethods, PsiElement psiElement) {
        return getTypeInternal(type, maxRecursionDepth, shouldResolveAllMethods, psiElement);
    }

    @Nullable
    private Type getTypeInternal(Object psiElement, int maxRecursionDepth, boolean shouldResolveAllMethods, PsiElement typePsiElement) {
        Type type = null;
        String canonicalText = JavaTypeUtils.resolveCanonicalName(psiElement, typePsiElement);
        if (canonicalText != null) {
            type = typeDictionary.get(canonicalText);
            if (type == null || !type.isDependenciesResolvable() && shouldResolveAllMethods && maxRecursionDepth > 1) {
                LOG.debug(newTypeCounter.incrementAndGet() + ". Creating new type object for:" + canonicalText + " maxRecursionDepth:" + maxRecursionDepth);
                if (psiElement instanceof PsiType) {
                    final PsiType psiType = (PsiType) psiElement;
                    type = new Type(psiType, typePsiElement, this, maxRecursionDepth, shouldResolveAllMethods);
                    typeDictionary.put(canonicalText, type);
                    type.resolveDependencies(this, maxRecursionDepth, psiType, shouldResolveAllMethods);
                } else if (psiElement instanceof PsiClass) {
                    final PsiClass psiClass = (PsiClass) psiElement;
                    type = new Type(psiClass, this, maxRecursionDepth, shouldResolveAllMethods);
                    typeDictionary.put(canonicalText, type);
                }
            } else {
                LOG.debug(existingTypeHitsCounter.incrementAndGet() + ". Found existing type object for:" + canonicalText + " maxRecursionDepth:" + maxRecursionDepth);
            }
        }
        return type;
    }

    public boolean isAccessible(PsiMethod psiMethod) {
        return PsiUtil.isAccessibleFromPackage(psiMethod, targetPackage) && (psiMethod.getContainingClass() == null || PsiUtil.isAccessibleFromPackage(psiMethod.getContainingClass(), targetPackage));
    }
}
