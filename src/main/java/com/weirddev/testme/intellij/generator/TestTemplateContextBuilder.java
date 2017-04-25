package com.weirddev.testme.intellij.generator;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
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
        ctxtParams.put(TestMeTemplateParams.GROOVY_TEST_BUILDER, new GroovyTestBuilderImpl(maxRecursionDepth));
        ctxtParams.put(TestMeTemplateParams.JAVA_TEST_BUILDER, new JavaTestBuilderImpl(maxRecursionDepth));
        ctxtParams.put(TestMeTemplateParams.STRING_UTILS, StringUtils.class);
        ctxtParams.put(TestMeTemplateParams.MOCKITO_UTILS, MockitoUtils.class);
        ctxtParams.put(TestMeTemplateParams.TEST_SUBJECT_UTILS, TestSubjectUtils.class);
        final PsiClass targetClass = context.getSrcClass();
        if (targetClass != null && targetClass.isValid()) {
            final TypeDictionary typeDictionary = new TypeDictionary(context.getSrcClass(), context.getTargetPackage());
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS, typeDictionary.getType(Type.resolveType(targetClass), maxRecursionDepth));
            List<Field> fields = createFields(context);
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS_FIELDS, fields);//todo refactor to be part of TESTED_CLASS
            List<Method> methods = createMethods(context.getSrcClass(),maxRecursionDepth, typeDictionary);
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS_METHODS, methods);//todo refactor to be part of TESTED_CLASS
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

    @NotNull
    private List<Field> createFields(FileTemplateContext context) {
        ArrayList<Field> fields = new ArrayList<Field>();
        PsiClass srcClass = context.getSrcClass();
        for (PsiField psiField : srcClass.getAllFields()) {
            //TODO mark fields initialized inline/in default constructor
            if(!"groovy.lang.MetaClass".equals(psiField.getType().getCanonicalText())){
                fields.add(new Field(psiField, PsiUtil.resolveClassInType(psiField.getType()), srcClass));
            }
        }
        return fields;
    }

    private List<Method> createMethods(PsiClass srcClass, int maxRecursionDepth, TypeDictionary typeDictionary) {
        ArrayList<Method> methods = new ArrayList<Method>();
        for (PsiMethod psiMethod : srcClass.getAllMethods()) {
            methods.add(new Method(psiMethod, srcClass, maxRecursionDepth, typeDictionary));
        }
        return methods;
    }
}
