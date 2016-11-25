package com.weirddev.testme.intellij.generator;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.weirddev.testme.intellij.FileTemplateContext;
import com.weirddev.testme.intellij.template.*;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date: 20/11/2016
 *
 * @author Yaron Yamin
 */
public class TestTemplateContextBuilder {
    ClassElementsLocator classElementsLocator;

    public TestTemplateContextBuilder() {
        classElementsLocator = new ClassElementsLocator();
    }

    public Map<String, Object> build(FileTemplateContext context, Properties defaultProperties) {
        HashMap<String, Object> ctxtParams = initTemplateContext(defaultProperties);
        ctxtParams.put(TestMeTemplateParams.MONTH_NAME_EN, new SimpleDateFormat("MMMM", Locale.ENGLISH).format(new Date()));
        ctxtParams.put(TestMeTemplateParams.CLASS_NAME, context.getTargetClass());
        ctxtParams.put(TestMeTemplateParams.PACKAGE_NAME, context.getTargetPackage().getQualifiedName());
        final PsiClass targetClass = context.getSrcClass();
        if (targetClass != null && targetClass.isValid()) {
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS_NAME, targetClass.getName());
            List<Field> fields = createFields(context);
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS_FIELDS, fields);
            int maxRecursionDepth = context.getMaxRecursionDepth();
            ctxtParams.put(TestMeTemplateParams.MAX_RECURSION_DEPTH, maxRecursionDepth);
            List<Method> methods = createMethods(context.getSrcClass(),maxRecursionDepth);
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS_METHODS, methods);
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS_TYPES_IN_DEFAULT_PACKAGE, classElementsLocator.filterTypesInDefaultPackage(methods, fields));

        }
        ctxtParams.put(TestMeTemplateParams.UTILS, new TemplateUtils());
        return ctxtParams;
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
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(context.getProject());
        PsiClass srcClass = context.getSrcClass();
        for (PsiField psiField : srcClass.getAllFields()) {
            //TODO research how different types should be handled - i.e. PsiClassType ?
            //TODO handle fields initialized inline/in default constructor
            fields.add(new Field(psiField, javaPsiFacade.findClass(psiField.getType().getCanonicalText(), GlobalSearchScope.allScope(context.getProject())), srcClass));
        }
        return fields;
    }

    private List<Method> createMethods(PsiClass srcClass, int maxRecursionDepth) {
        Map<String, Type> resolvedTypes = new HashMap<String, Type>();
        ArrayList<Method> methods = new ArrayList<Method>();
        for (PsiMethod psiMethod : srcClass.getAllMethods()) {
            methods.add(new Method(psiMethod, srcClass,resolvedTypes,maxRecursionDepth));
        }
        return methods;
    }
}
