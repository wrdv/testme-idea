package com.weirddev.testme.intellij.template;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiUtil;
import com.weirddev.testme.intellij.template.context.Type;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 26/11/2016
 *
 * @author Yaron Yamin
 */
public class TypeDictionary {

    Map<String, Type> typeDictionary =new HashMap<String, Type>();
    private final PsiClass srcClass;
    private final PsiPackage targetPackage;

    public TypeDictionary(PsiClass srcClass, PsiPackage targetPackage) {
        this.srcClass = srcClass;
        this.targetPackage = targetPackage;
    }

    @Nullable
    public Type getType(PsiType psiType, int maxRecursionDepth) {
        Type type = null;
        if (psiType != null) {
            type = typeDictionary.get(psiType.getCanonicalText());
            if (type == null) {
                type = new Type(psiType, this, maxRecursionDepth);
                typeDictionary.put(psiType.getCanonicalText(), type);
            }
        }
        return type;
    }

    public boolean isAccessible(PsiMethod psiMethod) {
        return PsiUtil.isAccessibleFromPackage(psiMethod, targetPackage);
    }
}
