package com.weirddev.testme.intellij.template;

import com.intellij.psi.PsiType;
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
}
