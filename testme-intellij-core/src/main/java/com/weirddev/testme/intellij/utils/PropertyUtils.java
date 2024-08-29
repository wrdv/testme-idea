package com.weirddev.testme.intellij.utils;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PropertyUtil;
import com.weirddev.testme.intellij.groovy.resolvers.GroovyPropertyUtil;
import com.weirddev.testme.intellij.common.utils.LanguageUtils;

/**
 * Created with IntelliJ IDEA.
 * User: yaron.yamin
 * Date: 11/1/2017
 * Time: 4:21 PM
 */
public class PropertyUtils
{
    public static boolean isPropertySetter(PsiMethod psiMethod) {
        if (LanguageUtils.isGroovy(psiMethod.getLanguage())) {
            return GroovyPropertyUtil.isPropertySetter(psiMethod);
        }
        else {
            return PropertyUtil.isSimplePropertySetter(psiMethod) && PropertyUtil.isSimpleSetter(psiMethod);
        }
    }

    public static boolean isPropertySetter(PsiMethod psiMethod, String propertyName) {
        if (LanguageUtils.isGroovy(psiMethod.getLanguage())) {
            return GroovyPropertyUtil.isPropertySetter(psiMethod, propertyName);
        } else {
            return PropertyUtil.isSimplePropertySetter(psiMethod) && PropertyUtil.isSimpleSetter(psiMethod)
                && propertyName.equals(PropertyUtil.getPropertyName(psiMethod));
        }
    }

    public static boolean isPropertyGetter(PsiMethod psiMethod) {
        if (LanguageUtils.isGroovy(psiMethod.getLanguage())) {
            return GroovyPropertyUtil.isPropertyGetter(psiMethod);
        }
        else {
            return PropertyUtil.isSimplePropertyGetter(psiMethod) && PropertyUtil.isSimpleGetter(psiMethod);
        }
    }
}
