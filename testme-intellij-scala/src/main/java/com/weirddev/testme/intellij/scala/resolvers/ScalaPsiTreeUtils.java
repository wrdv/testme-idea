package com.weirddev.testme.intellij.scala.resolvers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.weirddev.testme.intellij.scala.utils.GenericsExpressionParser;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.scala.lang.psi.api.base.ScPrimaryConstructor;
import org.jetbrains.plugins.scala.lang.psi.api.base.types.ScParameterizedTypeElement;
import org.jetbrains.plugins.scala.lang.psi.api.base.types.ScTypeElement;
import org.jetbrains.plugins.scala.lang.psi.api.statements.params.ScClassParameter;
import org.jetbrains.plugins.scala.lang.psi.light.ScPrimaryConstructorWrapper;
import org.jetbrains.plugins.scala.lang.psi.types.ScType;
import org.jetbrains.plugins.scala.lang.psi.types.result.TypeResult;
import scala.Option;
import scala.collection.Seq;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Date: 08/12/2017
 *
 * @author Yaron Yamin
 */
public class ScalaPsiTreeUtils {
    private static final Logger LOG = Logger.getInstance(ScalaPsiTreeUtils.class.getName());
    public static PsiParameter[] resolveParameters(PsiMethod psiMethod) {
        PsiParameter[] psiParameters = null;
        if (psiMethod instanceof ScPrimaryConstructorWrapper) {
            final ScPrimaryConstructorWrapper scPrimaryConstructorWrapper = (ScPrimaryConstructorWrapper) psiMethod;
//            final ScPrimaryConstructor scPrimaryConstructor = scPrimaryConstructorWrapper.constr();
            ScPrimaryConstructor scPrimaryConstructor = resolvePrimaryConstructor(scPrimaryConstructorWrapper);
            if (scPrimaryConstructor != null) {
                final Seq<ScClassParameter> scClassParameterSeq = scPrimaryConstructor.effectiveFirstParameterSection();
                int len = scClassParameterSeq.length();
                psiParameters = new PsiParameter[len];
                for (int i = 0; i < len; i++) {
                    psiParameters[i] = scClassParameterSeq.apply(i);
                }
            }
        }
        if(psiParameters==null){
            psiParameters = psiMethod.getParameterList().getParameters();
        }
        return psiParameters;
    }

    /**
     * get ScPrimaryConstructor from ScPrimaryConstructorWrapper by reflection since method constr() has been renamed in succeeding versions to delegate()
     */
    @Nullable
    private static ScPrimaryConstructor resolvePrimaryConstructor(ScPrimaryConstructorWrapper scPrimaryConstructorWrapper) {

        ScPrimaryConstructor scPrimaryConstructor = null;
        try {
            Method delegateMethod = null;
            //          suggestModuleForTests = CreateTestAction.class.getDeclaredMethod("suggestModuleForTests", Project.class,Module.class);
            // this didn't do the trick. actual compiled method name differs from original source code. so locating by signature..
            for (Method method : ScPrimaryConstructorWrapper.class.getDeclaredMethods()) {
                final Class<?>[] parameters = method.getParameterTypes();
                if (method.getReturnType().isAssignableFrom(ScPrimaryConstructor.class) && parameters == null || parameters.length == 0) {
                    delegateMethod = method;
                }
            }
            if (delegateMethod != null) {
                delegateMethod.setAccessible(true);
                try {
                    final Object obj = delegateMethod.invoke(scPrimaryConstructorWrapper);
                    if (obj != null && obj instanceof ScPrimaryConstructor) {

                        scPrimaryConstructor = (ScPrimaryConstructor) obj;
                    }
                } catch (Exception e) {
                    LOG.debug("error extracting ScPrimaryConstructor through reflection", e);
                }
            }

        } catch (Exception e) {
            LOG.debug("delegate() or constr() Method mot found.", e);
        }
        return scPrimaryConstructor;
    }

    public static PsiElement resolveRelatedTypeElement(PsiParameter psiParameter) {
        if (psiParameter instanceof ScClassParameter) {
            final PsiType type = psiParameter.getType();
            final Option<ScTypeElement> scTypeElementOption = ((ScClassParameter) psiParameter).typeElement();
            if (!scTypeElementOption.isEmpty()) {
                final ScTypeElement scTypeElement = scTypeElementOption.get();
//                final PsiElement context = scTypeElement.context();
//                final ScType scType = scTypeElement.calcType();
//                ScType$.MODULE$.extractClass(scType,)
//                final Option<PsiClass> psiClassOption = ScType.ExtractClass.unapply(scType);
                return scTypeElement;
            }
        }
        return null;
    }

    private static PsiClass findClass(PsiElement psiElement, String qualifiedName) {
        final Module moduleForPsiElement = ModuleUtilCore.findModuleForPsiElement(psiElement);
        PsiClass aClass;
        if (moduleForPsiElement != null) {
            aClass = JavaPsiFacade.getInstance(psiElement.getProject()).findClass(qualifiedName, GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(moduleForPsiElement));
        } else {
            aClass = JavaPsiFacade.getInstance(psiElement.getProject()).findClass(qualifiedName, GlobalSearchScope.projectScope(psiElement.getProject()));
        }
        return aClass;
    }

        public static ArrayList<PsiClass> resolveComposedTypes(PsiType psiType, PsiElement typePsiElement) {
            final ArrayList<PsiClass> psiClasses = new ArrayList<PsiClass>();
            if (typePsiElement instanceof ScParameterizedTypeElement) {
                String canonicalName = resolveParameterizedCanonicalName(typePsiElement);
                final ArrayList<String> genericTypes = GenericsExpressionParser.extractGenericTypes(canonicalName);
//                Map<String, String> typeNameToReplacedTypeName = new LinkedHashMap<String, String>();
                for (String genericType : genericTypes) {
                    if (genericType.length() > 0) {
                        PsiClass aClass = findClass(typePsiElement, genericType);
                        if (aClass == null && !genericType.contains(".")) {
                            final String scalaLangTypeName = "scala." + genericType;
                            aClass = findClass(typePsiElement, scalaLangTypeName);
                            if (aClass != null) {
//                                typeNameToReplacedTypeName.put(genericType, scalaLangTypeName);
                                psiClasses.add(aClass);
                            }
                        } else {
                            psiClasses.add(aClass);
                        }
                    }
                }
//                for (Map.Entry<String, String> typeNameToReplacedTypeNameEntry : typeNameToReplacedTypeName.entrySet()) {
//                    canonicalName=canonicalName.replaceFirst(typeNameToReplacedTypeNameEntry.getKey(), typeNameToReplacedTypeNameEntry.getValue());
//                }

            }
            return psiClasses;
        }

    public static String resolveParameterizedCanonicalName(PsiElement typePsiElement) {
        if (typePsiElement instanceof ScParameterizedTypeElement) {
            final ScParameterizedTypeElement parameterizedTypeElement = (ScParameterizedTypeElement) typePsiElement;
            final TypeResult<ScType> typeResult = parameterizedTypeElement.getType(parameterizedTypeElement.getType$default$1());
            final ScType scType = typeResult.get();
            if (scType == null) {
                return null;
            } else {
                final String canonicalText = scType.canonicalText();
                final String sanitizedRoot = canonicalText.replace("_root_.", "");
                return sanitizedRoot.replaceAll("\\[", "<").replaceAll("]", ">");
            }
        } else {
            return null;
        }
    }
}
