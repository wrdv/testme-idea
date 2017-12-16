package com.weirddev.testme.intellij.scala.resolvers;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.weirddev.testme.intellij.scala.utils.GenericsExpressionParser;
import org.jetbrains.plugins.scala.lang.psi.api.base.types.ScParameterizedTypeElement;
import org.jetbrains.plugins.scala.lang.psi.api.base.types.ScTypeElement;
import org.jetbrains.plugins.scala.lang.psi.api.statements.params.ScClassParameter;
import org.jetbrains.plugins.scala.lang.psi.light.ScPrimaryConstructorWrapper;
import scala.Option;
import scala.collection.Seq;

import java.util.ArrayList;

/**
 * Date: 08/12/2017
 *
 * @author Yaron Yamin
 */
public class ScalaPsiTreeUtils {

    public static PsiParameter[] resolveParameters(PsiMethod psiMethod) {
        PsiParameter[] parameters;
        if (psiMethod instanceof ScPrimaryConstructorWrapper) {
            final Seq<ScClassParameter> scClassParameterSeq = ((ScPrimaryConstructorWrapper) psiMethod).constr().effectiveFirstParameterSection();
            int len = scClassParameterSeq.length();
            parameters = new PsiParameter[len];
            for (int i = 0; i < len; i++) {
                parameters[i] = scClassParameterSeq.apply(i);
            }
        } else {
            parameters = psiMethod.getParameterList().getParameters();
        }
        return parameters;
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
            final String canonicalText = parameterizedTypeElement.calcType().canonicalText();
            final String sanitizedRoot = canonicalText.replace("_root_.", "");
            return sanitizedRoot.replaceAll("\\[", "<").replaceAll("]",">");
        } else {
            return null;
        }
    }
}
