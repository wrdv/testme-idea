package com.weirddev.testme.intellij.generator;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ResourceFileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.PsiManagerEx;
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
        int maxRecursionDepth = context.getFileTemplateConfig().getMaxRecursionDepth();
        ctxtParams.put(TestMeTemplateParams.MAX_RECURSION_DEPTH, maxRecursionDepth);
        ctxtParams.put(TestMeTemplateParams.STRING_UTILS, new StringUtils());
        final TypeDictionary typeDictionary = new TypeDictionary(context.getSrcClass(), context.getTargetPackage());
        ctxtParams.put(TestMeTemplateParams.TEST_BUILDER, new TestBuilderImpl(context.getLanguage(), context.getSrcModule(), typeDictionary, context.getFileTemplateConfig()));
        final PsiClass targetClass = context.getSrcClass();
        if (targetClass != null && targetClass.isValid()) {
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS_LANGUAGE, targetClass.getLanguage().getID());
            final Type type = typeDictionary.getType(Type.resolveType(targetClass), maxRecursionDepth, true);
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS, type);
            if (type != null) {
                resolveInternalReferences(maxRecursionDepth, type.getMethods());
            }
        }
        final TestSubjectInspector testSubjectInspector = new TestSubjectInspector(context.getFileTemplateConfig().isGenerateTestsForInheritedMethods());
        ctxtParams.put(TestMeTemplateParams.TEST_SUBJECT_UTILS, testSubjectInspector);
        ctxtParams.put(TestMeTemplateParams.MOCKITO_MOCK_BUILDER, createMockitoMockBuilder(context, testSubjectInspector));
        logger.debug("Done building Test Template context in "+(new Date().getTime()-start)+" millis");
        return ctxtParams;
    }

    @NotNull
    private MockitoMockBuilder createMockitoMockBuilder(FileTemplateContext context, TestSubjectInspector testSubjectInspector) {
        boolean found = false;
//        final VirtualFile mockMakerVFile = ResourceFileUtil.findResourceFileInScope("mockito-extensions/org.mockito.plugins.MockMaker", context.getProject(), context.getTestModule().getModuleWithDependenciesAndLibrariesScope(true));
        final VirtualFile mockMakerVFile = ResourceFileUtil.findResourceFileInDependents(context.getTestModule(), "mockito-extensions/org.mockito.plugins.MockMaker");
        logger.debug("found mockito MockMaker in test module classpath:"+mockMakerVFile);
        if (mockMakerVFile != null) {
            final PsiFile mockMakerPsiFile = ((PsiManagerEx) PsiManager.getInstance(context.getProject())).getFileManager().getCachedPsiFile(mockMakerVFile);
            if (mockMakerPsiFile != null) {
                final String mockFileText = mockMakerPsiFile.getText();
                found = StringUtils.hasLine(mockFileText,"mock-maker-inline");
                logger.debug("mockito MockMaker content:"+ mockFileText);
                logger.debug("is mock-maker-inline turned on:"+ found);
            }
        }
        return new MockitoMockBuilder(found,context.getFileTemplateConfig().isStubMockMethodCallsReturnValues(), testSubjectInspector);
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

    private void resolveInternalReferences(int maxMethodCallsDepth, List<Method> methods) {
//              todo test generic methods and type params. use actual type params passed
        for (int i = 0; i < maxMethodCallsDepth; i++) {
            for (Method method : methods) {
                resolveMethodCalls(methods, method);
            }
        }
        for (Method method : methods) {
            resolveFieldsAffectedByCtor(method.getReturnType(),maxMethodCallsDepth);
        }
        logger.debug("Resolved internal references in test template context");
    }

    private void resolveFieldsAffectedByCtor(Type type, int maxMethodCallsDepth) {//todo consider moving to test builder
        if (maxMethodCallsDepth < 1) {
            return;
        }
        if (isValidObject(type)) {
            for (Method ctor : type.findConstructors()) {
                Set<Field> affectedFields = new HashSet<Field>();
                for (MethodCall methodCall : ctor.getMethodCalls()) {
                    for (Param param : methodCall.getMethod().getMethodParams()) {
                        for (Field assignedToField : param.getAssignedToFields()) {
                            if (assignedToField.getOwnerClassCanonicalName().equals(ctor.getOwnerClassCanonicalType())) {
                                affectedFields.add(assignedToField);
                            }
                        }
                        resolveFieldsAffectedByCtor(param.getType(), maxMethodCallsDepth--);
                    }
                }
                ctor.getIndirectlyAffectedFields().addAll(affectedFields);
            }
        }
    }

    private boolean isValidObject(Type type) {
        return type != null && !type.isPrimitive() && !type.isArray() && !type.isInterface() && !type.isAbstract() && !type.isVarargs();
    }

    private void resolveMethodCalls(List<Method> methods, Method method) {
        final Set<MethodCall> calledMethodsByMethodCalls = new HashSet<MethodCall>();
        final Set<MethodCall> methodsInMyFamilyTree= new HashSet<MethodCall>();
        for (MethodCall methodCall : method.getMethodCalls()) {
            final Method calledMethodFound = find(methods, methodCall.getMethod().getMethodId());//find originally resolved method since methods in resolved method call are resolved in a shallow manner
            if (calledMethodFound != null) {
                MethodCall methodCallFound;
                if (methodCall.getMethod() == calledMethodFound) {
                    methodCallFound = methodCall;
                } else {
                    methodCallFound = new MethodCall(calledMethodFound, methodCall.getMethodCallArguments());
                }
                methodsInMyFamilyTree.add(methodCallFound);
                calledMethodsByMethodCalls.add(methodCallFound);
                if (method.getOwnerClassCanonicalType()!=null && method.getOwnerClassCanonicalType().equals(methodCallFound.getMethod().getOwnerClassCanonicalType())) {
                    calledMethodsByMethodCalls.addAll(calledMethodFound.getMethodCalls());
                }
            }
        }
        method.getMethodCalls().removeAll(calledMethodsByMethodCalls);
        method.getMethodCalls().addAll(calledMethodsByMethodCalls);
        method.getCalledFamilyMembers().addAll(methodsInMyFamilyTree);
    }

    private Method find(List<Method> methods, String methodId) {
        for (Method method : methods) {
            if (method.getMethodId().equals(methodId)) {
                return method;
            }
            if (method.getReturnType() != null) {
                for (Method returnTypeMethod : method.getReturnType().getMethods()) {
                    if (returnTypeMethod.getMethodId().equals(methodId)) {
                        return returnTypeMethod;
                    }
                }

            }
        }
        return null;
    }

}
