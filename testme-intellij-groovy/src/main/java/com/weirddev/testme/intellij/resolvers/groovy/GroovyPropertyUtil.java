package com.weirddev.testme.intellij.resolvers.groovy;

import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.lang.psi.util.GroovyPropertyUtils;

/**
 * Created with IntelliJ IDEA.
 * User: yaron.yamin
 * Date: 11/1/2017
 * Time: 4:23 PM
 */
/**
A this wrapper to IJ GroovyPropertyUtils, used to isolate groovy plugin so there will be not attempts to load it at runtime when its not available
 */
public class GroovyPropertyUtil
{
    public static boolean isPropertySetter(@Nullable PsiMethod method) {
        return GroovyPropertyUtils.isSimplePropertySetter(method);
    }
    public static boolean isPropertyGetter(@Nullable PsiMethod method) {
        return GroovyPropertyUtils.isSimplePropertyGetter(method);
    }
}
