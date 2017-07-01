package com.weirddev.testme.intellij.generator;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.weirddev.testme.intellij.template.FileTemplateContext;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.template.context.*;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date: 20/11/2016
 *
 * @author Yaron Yamin
 */
public class TestTemplateContextBuilder {
    private static final Logger logger = Logger.getInstance(TestTemplateContextBuilder.class.getName());

    public Map<String, Object> build(FileTemplateContext context, Properties defaultProperties) {
        final long start = new Date().getTime();
        HashMap<String, Object> ctxtParams = initTemplateContext(defaultProperties);
        populateDateFields(ctxtParams, Calendar.getInstance());
        ctxtParams.put(TestMeTemplateParams.CLASS_NAME, context.getTargetClass());
        ctxtParams.put(TestMeTemplateParams.PACKAGE_NAME, context.getTargetPackage().getQualifiedName());
        int maxRecursionDepth = context.getMaxRecursionDepth();
        ctxtParams.put(TestMeTemplateParams.MAX_RECURSION_DEPTH, maxRecursionDepth);
        ctxtParams.put(TestMeTemplateParams.TEST_BUILDER, new TestBuilderImpl(context.getLanguage(),maxRecursionDepth, context.isIgnoreUnusedProperties(), context.getMinPercentOfExcessiveSettersToPreferDefaultCtor()));
        ctxtParams.put(TestMeTemplateParams.STRING_UTILS, new StringUtils());
        ctxtParams.put(TestMeTemplateParams.MOCKITO_UTILS, new MockitoUtils());
        ctxtParams.put(TestMeTemplateParams.TEST_SUBJECT_UTILS,new TestSubjectUtils());
        final PsiClass targetClass = context.getSrcClass();
        if (targetClass != null && targetClass.isValid()) {
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS_LANGUAGE, targetClass.getLanguage().getID());
            final TypeDictionary typeDictionary = new TypeDictionary(context.getSrcClass(), context.getTargetPackage());
            final Type type = typeDictionary.getType(Type.resolveType(targetClass), maxRecursionDepth, true);
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS, type);
            if (type != null) {
                resolveMethodCalls(maxRecursionDepth, type.getMethods());
            }
        }
        logger.debug("Done building Test Template context in "+(new Date().getTime()-start)+" millis");
        return ctxtParams;
    }

    void populateDateFields(Map<String, Object> ctxtParams, Calendar calendar) {
        ctxtParams.put(TestMeTemplateParams.MONTH_NAME_EN, new SimpleDateFormat("MMMM", Locale.ENGLISH).format(calendar.getTime()));
        ctxtParams.put(TestMeTemplateParams.DAY_NUMERIC, calendar.get(Calendar.DAY_OF_MONTH));
        ctxtParams.put(TestMeTemplateParams.HOUR_NUMERIC, calendar.get(Calendar.HOUR_OF_DAY));
        ctxtParams.put(TestMeTemplateParams.MINUTE_NUMERIC, calendar.get(Calendar.MINUTE));
        ctxtParams.put(TestMeTemplateParams.SECOND_NUMERIC, calendar.get(Calendar.SECOND));
    }

    @NotNull
    private HashMap<String, Object> initTemplateContext(Properties defaultProperties) {
        HashMap<String, Object> templateCtxtParams = new HashMap<String, Object>();
        for (Map.Entry<Object, Object> entry : defaultProperties.entrySet()) {
            templateCtxtParams.put((String) entry.getKey(), entry.getValue());
        }
        return templateCtxtParams;
    }

    private void resolveMethodCalls(int maxMethodCallsDepth, List<Method> methods) {
//              todo support groovy property read (implicitly) & direct field access + traverse methods recursively to relate all getter calls to specific method
//              todo test generic methods and type params. use actual type params passed
        for (int i = 0; i < maxMethodCallsDepth; i++) {
            for (Method method : methods) {
                resolveMethodCalls(methods, method);
                resolveConstructorCalls(method.getReturnType());
            }
        }
        for (Method method : methods) {
            resolveFieldsAffectedByCtor(method.getReturnType());
        }
    }

    private void resolveFieldsAffectedByCtor(Type type) {
        if (isValidObject(type)) {
            for (Method ctor : type.getConstructors()) {
                Set<Field> affectedFields = new HashSet<Field>();
                for (MethodCall methodCall : ctor.getCalledFamilyMembers()) {
//                    if (methodCall.getMethod().isConstructor()) {
                        for (Param param : methodCall.getMethod().getMethodParams()) {
                            affectedFields.addAll(param.getAssignedToFields());
                        }
//                    }
                }
                ctor.getAffectedFields().addAll(affectedFields);
            }
        }
    }

    private boolean isValidObject(Type type) {
        return type != null && !type.isPrimitive() && !type.isArray() && !type.isInterface() && !type.isAbstract() && !type.isVarargs();
    }

    private void resolveConstructorCalls(Type type) {
        if (isValidObject(type)) {
            for (Method ctor : type.getConstructors()) {
                final List<Method> methodsAndCtors = new ArrayList<Method>();
                methodsAndCtors.addAll(type.getMethods());
                methodsAndCtors.addAll(type.getConstructors());
                resolveMethodCalls(methodsAndCtors, ctor);
            }
        }
    }

    private void resolveMethodCalls(List<Method> methods, Method method) {
        final Set<MethodCall> calledMethodsByMethodCalls = new HashSet<MethodCall>();
        final Set<MethodCall> methodsInMyFamilyTree= new HashSet<MethodCall>();
        for (MethodCall methodCall : method.getMethodCalls()) {
            if (methods.contains(methodCall.getMethod())) {
                final Method calledMethodFound = find(methods, methodCall.getMethod().getMethodId());//find originally resolved method since methods in resolved method call are resolved in a shallow manner
                if (calledMethodFound != null) {
                    methodsInMyFamilyTree.add(new MethodCall(calledMethodFound,methodCall.getMethodCallArguments()));
                    calledMethodsByMethodCalls.addAll(calledMethodFound.getMethodCalls());
                }
            }
        }
        method.getMethodCalls().addAll(calledMethodsByMethodCalls);
        method.getCalledFamilyMembers().addAll(methodsInMyFamilyTree);
    }

    private Method find(List<Method> methods, String methodId) {
        for (Method method : methods) {
            if (method.getMethodId().equals(methodId)) {
                return method;
            }
        }
        return null;
    }

}
