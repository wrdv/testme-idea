package com.weirddev.testme.intellij.scala.resolvers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.weirddev.testme.intellij.common.reflection.MethodReflectionUtils;
import com.weirddev.testme.intellij.resolvers.to.MethodCallArg;
import com.weirddev.testme.intellij.resolvers.to.ResolvedMethodCall;
import com.weirddev.testme.intellij.scala.utils.GenericsExpressionParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.scala.lang.psi.api.base.ScPrimaryConstructor;
import org.jetbrains.plugins.scala.lang.psi.api.base.types.ScParameterizedTypeElement;
import org.jetbrains.plugins.scala.lang.psi.api.base.types.ScTypeElement;
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScArgumentExprList;
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScExpression;
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScMethodCall;
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScReferenceExpression;
import org.jetbrains.plugins.scala.lang.psi.api.statements.ScFunction;
import org.jetbrains.plugins.scala.lang.psi.api.statements.params.ScClassParameter;
import org.jetbrains.plugins.scala.lang.psi.api.statements.params.ScParameter;
import org.jetbrains.plugins.scala.lang.psi.api.toplevel.typedef.ScClass;
import org.jetbrains.plugins.scala.lang.psi.impl.expr.ScReferenceExpressionImpl;
import org.jetbrains.plugins.scala.lang.psi.light.PsiTypedDefinitionWrapper;
import org.jetbrains.plugins.scala.lang.psi.light.ScFunctionWrapper;
import org.jetbrains.plugins.scala.lang.psi.light.ScPrimaryConstructorWrapper;
import org.jetbrains.plugins.scala.lang.psi.types.ScParameterizedType;
import org.jetbrains.plugins.scala.lang.psi.types.ScType;
import org.jetbrains.plugins.scala.lang.psi.types.api.StdType;
import org.jetbrains.plugins.scala.lang.psi.types.result.Typeable;
import scala.Option;
import scala.collection.Seq;
import scala.util.Either;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Date: 08/12/2017
 *
 * @author Yaron Yamin
 */
public class ScalaPsiTreeUtils {
    private static final Logger LOG = Logger.getInstance(ScalaPsiTreeUtils.class.getName());
    public static PsiParameter[] resolveParameters(PsiMethod psiMethod) {
        PsiParameter[] psiParameters = null;
        if (psiMethod instanceof ScPrimaryConstructorWrapper) { //todo revise implementation
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
        else if (psiMethod instanceof ScFunctionWrapper) {
//            final ScFunction function = ((ScFunctionWrapper) psiMethod).function();
            final ScFunction function = resolveFunction(((ScFunctionWrapper) psiMethod));
            if (function != null) {
                final int length = function.parameters().length();
                psiParameters = new PsiParameter[length];
                for (int i = 0; i < length; i++) {
                    psiParameters[i] = function.parameters().apply(i);
                }
            }
        }
        if(psiParameters==null){
            psiParameters = psiMethod.getParameterList().getParameters();
        }
        return psiParameters;
    }

    public static Object resolveRelatedTypeElement(PsiParameter psiParameter) {
        if (psiParameter instanceof ScClassParameter) {
            final Option<ScTypeElement> scTypeElementOption = ((ScClassParameter) psiParameter).typeElement();
            if (!scTypeElementOption.isEmpty()) {
                return scTypeElementOption.get();
            }
        } else if (psiParameter instanceof ScParameter) {
            final ScParameter scParameter = (ScParameter) psiParameter;
//            final TypeResult<ScType> typeResult = scParameter.getRealParameterType(scParameter.getRealParameterType$default$1());
            return extractScType(scParameter);
        }
        return null;
    }


    @Nullable
    public static PsiClass resolvePsiClass(String qualifiedName, Project project, Module module) {
        PsiClass aClass = findClass(qualifiedName, module, project);
//        if (aClass == null && !qualifiedName.contains(".")) {//todo - consider removing . fallback may not be necessary anymore
//            final String scalaLangTypeName = "scala." + qualifiedName;
//            aClass = findClass(scalaLangTypeName, module, project);
//        }
        return aClass;
    }

    public static String resolveParameterizedCanonicalName(PsiElement typePsiElement) {
        String canonicalText = null;
        if (typePsiElement instanceof ScParameterizedTypeElement) {
            final ScParameterizedTypeElement parameterizedTypeElement = (ScParameterizedTypeElement) typePsiElement; //todo find alternative solution - no supported in future scala plugin versions
            final ScType scType = extractScType(parameterizedTypeElement);
            if (scType == null) {
                return null;
            } else {

                canonicalText = scType.canonicalText();
                final String sanitizedRoot = stripRootPrefixFromScalaCanonicalName(canonicalText);
                return normalizeGenericsRepresentation(sanitizedRoot);
            }
        } else if (typePsiElement instanceof ScClass) {
            final ScClass scClass = (ScClass) typePsiElement;
            final ScType scType = extractScType(scClass);
            if (scType!=null) {
                canonicalText = scType.canonicalText();
            }
        }
        if (null != canonicalText) {
            final String sanitizedRoot = stripRootPrefixFromScalaCanonicalName(canonicalText);
            return normalizeGenericsRepresentation(sanitizedRoot);
        } else {
            return null;
        }
    }

    public static String resolveCanonicalNameOfObject(Object typeElement) {
        String canonicalText = null;
        if (typeElement instanceof ScParameterizedType) {
            final ScParameterizedType scParameterizedType = (ScParameterizedType) typeElement;
            canonicalText = scParameterizedType.canonicalText();
            final String designatorCanonicalText = scParameterizedType.designator().canonicalText();
            if (canonicalText.startsWith("(") && canonicalText.endsWith(")")) {
                canonicalText = designatorCanonicalText + ("<"+canonicalText.substring(1, canonicalText.length() - 1)+">");
            }
        } else if(typeElement instanceof ScType){
            final ScType scType = (ScType) typeElement;
            if (scType instanceof StdType) {
                canonicalText = ((StdType) scType).fullName();
            } else {
                canonicalText = scType.canonicalText();
            }
        }
        canonicalText = stripRootPrefixFromScalaCanonicalName(canonicalText);
        return normalizeGenericsRepresentation(canonicalText);
    }

    public static List<Object> resolveComposedTypeElementsForObject(PsiType psiType, Object typeElement) {
        ArrayList<Object> typeElements= new ArrayList<>();
        Seq<ScType> scTypeSeq = null;
        if (typeElement instanceof ScParameterizedType) {
            final ScParameterizedType scParameterizedType = (ScParameterizedType) typeElement;
            scTypeSeq = scParameterizedType.typeArguments();
        }
        else if(typeElement instanceof ScParameterizedTypeElement){
            final ScParameterizedTypeElement scParameterizedTypeElement = (ScParameterizedTypeElement) typeElement;
            ScType scType = extractScType(scParameterizedTypeElement);
            if (scType instanceof ScParameterizedType) {
                scTypeSeq = ((ScParameterizedType) scType).typeArguments();
            }
        }
        if (scTypeSeq != null) {
            for (int i = 0; i < scTypeSeq.length(); i++) {
                final ScType scType = scTypeSeq.apply(i);
                typeElements.add(scType);
            }
        }
        else if (typeElement instanceof PsiElement) {//todo - consider removing . fallback may not be necessary anymore
            final List<PsiClass> psiClasses = resolveComposedTypes(psiType, ((PsiElement) typeElement));
            final ArrayList<Object> objects = new ArrayList<>();
            objects.addAll(psiClasses);
            typeElements = objects;
        }
        return typeElements;
    }

    public static  Object resolveReturnType(PsiMethod psiMethod) {
        Object scType = null;

        if (psiMethod instanceof ScFunctionWrapper) {
            final ScFunction function = resolveFunction(((ScFunctionWrapper) psiMethod)); //            final ScFunction function = ((ScFunctionWrapper) psiMethod).function();
            if (function != null) {
                Object resultObj = MethodReflectionUtils.invokeMethodReflectivelyWithFallback(function, Object.class /*TypeResult.class*/, "returnTypeInner", "returnType");
                if (resultObj != null) {
                    scType = MethodReflectionUtils.invokeMethodReflectivelyWithFallback(resultObj, Object.class, "get", null);
                }
                //Non reflective version #1
//                final org.jetbrains.plugins.scala.lang.psi.types.result.TypeResult<ScType> scTypeTypeResult = function.returnType();
//                if (scTypeTypeResult != null && !scTypeTypeResult.isEmpty()) {
//                    scType = scTypeTypeResult.get();
//                }
            }
        }
        return scType;
    }

    public static boolean isSyntheticMethod(PsiMethod psiMethod) {
        return psiMethod instanceof PsiTypedDefinitionWrapper;
    }

    @NotNull
    public static List<ResolvedMethodCall> findMethodCalls(PsiMethod psiMethod) {
        List<ResolvedMethodCall > methodCalled= new ArrayList<>();
        if (psiMethod instanceof ScFunctionWrapper) {
            final ScFunction function = resolveFunction(((ScFunctionWrapper) psiMethod));
            if (function != null) {
                final Collection<ScMethodCall> scMethodCall = PsiTreeUtil.findChildrenOfType(function, ScMethodCall.class);
                for (ScMethodCall methodCall : scMethodCall) {
                    final ScExpression scExpression = methodCall.deepestInvokedExpr();
                    if (scExpression instanceof ScReferenceExpressionImpl) {
                        final PsiElement resolvedElement = ((ScReferenceExpressionImpl) scExpression).resolve();
                        if (resolvedElement instanceof PsiMethod) {
                            final PsiMethod psiMethodResolved = (PsiMethod) resolvedElement;
                            final ScArgumentExprList args = methodCall.args();
                            final ArrayList<MethodCallArg> methodCallArguments = new ArrayList<>();
                            if(args!=null && args.exprs()!=null){
                                final Seq<ScExpression> argSeq = args.exprs().seq();
                                for (int i = 0; i <argSeq.length(); i++) {
                                    methodCallArguments.add(new MethodCallArg(argSeq.apply(i).getText().trim()));
                                }
                            }
                            methodCalled.add(new ResolvedMethodCall(psiMethodResolved,methodCallArguments));
                        }
                    }
                }
                //find used method refs
                final Collection<ScReferenceExpression> scReferenceExpressions = PsiTreeUtil.findChildrenOfType(function, ScReferenceExpression.class);
                for (ScReferenceExpression scReferenceExpression : scReferenceExpressions) {
                    final PsiElement resolvedPsiElement = scReferenceExpression.resolve();
                    LOG.debug("for method "+psiMethod.getText()+" found expression: "+scReferenceExpression.getText()+". which resolves to "+resolvedPsiElement+". that is "+(resolvedPsiElement==null?"null":resolvedPsiElement.getText()));
                    if (resolvedPsiElement instanceof PsiMethod) {
                        final PsiMethod resolvedMethod = (PsiMethod) resolvedPsiElement;
                        methodCalled.add(new ResolvedMethodCall(resolvedMethod, null));
                    }
                }
            }
        }
        return methodCalled;
    }

    private static String normalizeGenericsRepresentation(String sanitizedRoot) {
        return sanitizedRoot.replaceAll("\\[", "<").replaceAll("]", ">");
    }
    private static String stripRootPrefixFromScalaCanonicalName(String canonicalText) {
        return canonicalText.replaceAll("_root_.", "");
    }
    /**
     * get ScPrimaryConstructor from ScPrimaryConstructorWrapper by reflection since method constr() has been renamed in succeeding versions to delegate()
     */
    @Nullable
    private static ScPrimaryConstructor resolvePrimaryConstructor(ScPrimaryConstructorWrapper object) {
        return MethodReflectionUtils.getReturnTypeReflectively(object, ScPrimaryConstructorWrapper.class, ScPrimaryConstructor.class,null);
    }
    @Nullable
    private static ScFunction resolveFunction(ScFunctionWrapper object) {
        return MethodReflectionUtils.getReturnTypeReflectively(object, ScFunctionWrapper.class, ScFunction.class,null);
    }
    private static PsiClass findClass(String qualifiedName, Module module, Project project) {
        PsiClass aClass;
        if (module != null) {
            aClass = JavaPsiFacade.getInstance(project).findClass(qualifiedName, GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module));
        } else {
            aClass = JavaPsiFacade.getInstance(project).findClass(qualifiedName, GlobalSearchScope.everythingScope(project));
        }
        return aClass;
    }

    private static List<PsiClass> resolveComposedTypes(PsiType psiType, PsiElement typePsiElement) {
        final List<PsiClass> psiClasses = new ArrayList<PsiClass>();
        if (typePsiElement instanceof ScParameterizedTypeElement) {
            String canonicalName = resolveParameterizedCanonicalName(typePsiElement);
            final ArrayList<String> genericTypes = GenericsExpressionParser.extractGenericTypes(canonicalName);
            for (String genericType : genericTypes) {
                if (genericType.length() > 0) {
                    PsiClass aClass = resolvePsiClass(typePsiElement, genericType);
                    if (aClass != null) {
                        psiClasses.add(aClass);
                    }
                }
            }
        }
        return psiClasses;
    }

    private static PsiClass resolvePsiClass(PsiElement typePsiElement, String genericType) {
        return resolvePsiClass(genericType, typePsiElement.getProject(), ModuleUtilCore.findModuleForPsiElement(typePsiElement));
    }

    @Nullable
    private static ScType extractScType(Typeable typeable) {
        ScType scType;
        scType = getEitherReturnValueReflectively(typeable, Typeable.class, ScType.class, null);
        if (scType == null) {
            //Deprecated api since 2017.x:
//        final org.jetbrains.plugins.scala.lang.psi.types.result.TypeResult<ScType> typeResult = typeable.getType(typeable.getType$default$1());
//        if (!typeResult.isEmpty()) {
//            scType = typeResult.get();
//        }
            scType = getTypingContextReturnValueReflectively(typeable);
        }
        return scType;
    }

    @Nullable
    private static ScType getTypingContextReturnValueReflectively(Typeable typeable) {
        try {
            final Class<?> typingContextClass = Class.forName("org.jetbrains.plugins.scala.lang.psi.types.result.TypingContext");
            final Object typingContext = MethodReflectionUtils.getReturnTypeReflectively(typeable, Typeable.class, typingContextClass,null);
            if (typingContext != null) {
                final Method getTypeMethod = typeable.getClass().getMethod("getType", typingContextClass);
                final Object result = getTypeMethod.invoke(typeable, typingContext);
                if (result != null && result.getClass().getCanonicalName().equals("org.jetbrains.plugins.scala.lang.psi.types.result.Success")) {
                    final Method getMethod = result.getClass().getMethod("get");
                    if (getMethod != null) {
                        final Object type = getMethod.invoke(result);
                        if (type instanceof ScType) {
                            return (ScType) type;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.info("could not find Typeable.getType(TypingContext) method. this seams to be an up to date version of idea scala plugin", e);
        }
        return null;
    }

    @Nullable
    private static <U,T> T getEitherReturnValueReflectively(U owner, Class<U> ownerClass, Class<T> returnClass, String methodName) {
        try {
            final Either returnTypeReflective = MethodReflectionUtils.getReturnTypeReflectively(owner, ownerClass, Either.class, methodName);
            if (returnTypeReflective != null) {
                if (returnTypeReflective.isRight()) {
                    final Object obj = returnTypeReflective.right().get();
                    if (obj != null && returnClass.isInstance(obj)) {
                        return (T) obj;
                    }
                }
            }
        } catch (Exception e) {
            LOG.info("could not find method "+ (methodName==null?"":methodName) +" that returns type of "+returnClass.getCanonicalName()+ " wrapped in Either. this seams to be an outdated idea scala plugin", e);
        }
        return null;
    }

}
