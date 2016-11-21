package com.weirddev.testme.intellij.generator;

import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.SystemProperties;
import com.intellij.util.text.DateFormatUtil;
import com.weirddev.testme.intellij.FileTemplateContext;
import com.weirddev.testme.intellij.template.Method;
import com.weirddev.testme.intellij.template.TemplateUtils;
import com.weirddev.testme.intellij.template.TestMeTemplateParams;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

import com.weirddev.testme.intellij.template.Field;

import static com.intellij.ide.fileTemplates.FileTemplateManager.PROJECT_NAME_VARIABLE;
//import com.weirddev.testme.intellij.template.TemplateUtils;
//import com.weirddev.testme.intellij.template.Method;
//import com.weirddev.testme.intellij.template.TestMeTemplateParams;

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
//        Map<String, Object> ctxtParams = new HashMap<String, Object>();
//
//        Calendar calendar = Calendar.getInstance();
//        Date date = calendar.getTime();
//        SimpleDateFormat sdfMonthNameShort = new SimpleDateFormat("MMM");
//        SimpleDateFormat sdfMonthNameFull = new SimpleDateFormat("MMMM");
//        SimpleDateFormat sdfYearFull = new SimpleDateFormat("yyyy");
//
//        ctxtParams.put("DATE", DateFormatUtil.formatDate(date));
//        ctxtParams.put("TIME", DateFormatUtil.formatTime(date));
//        ctxtParams.put("YEAR", sdfYearFull.format(date));
//        ctxtParams.put("MONTH", getCalendarValue(calendar, Calendar.MONTH));
//        ctxtParams.put("MONTH_NAME_SHORT", sdfMonthNameShort.format(date));
//        ctxtParams.put("MONTH_NAME_FULL", sdfMonthNameFull.format(date));
//        ctxtParams.put("DAY", getCalendarValue(calendar, Calendar.DAY_OF_MONTH));
//        ctxtParams.put("HOUR", getCalendarValue(calendar, Calendar.HOUR_OF_DAY));
//        ctxtParams.put("MINUTE", getCalendarValue(calendar, Calendar.MINUTE));
//        ctxtParams.put("USER", SystemProperties.getUserName());
//        ctxtParams.put("PRODUCT_NAME", ApplicationNamesInfo.getInstance().getFullProductName());
//        ctxtParams.put("DS", "$"); // Dollar sign, strongly needed for PHP, JS, etc. See WI-8979
//        ctxtParams.put(PROJECT_NAME_VARIABLE, project.getName());


        ctxtParams.put(TestMeTemplateParams.MONTH_NAME_EN, new SimpleDateFormat("MMMM", Locale.ENGLISH).format(new Date()));
        ctxtParams.put(TestMeTemplateParams.CLASS_NAME, context.getTargetClass());
        ctxtParams.put(TestMeTemplateParams.PACKAGE_NAME, context.getTargetPackage().getQualifiedName());
        List<Field> fields = getFields(context);
        ctxtParams.put(TestMeTemplateParams.TESTED_CLASS_FIELDS, fields);
        List<Method> methods = getMethods(context.getSrcClass());
        ctxtParams.put(TestMeTemplateParams.TESTED_CLASS_METHODS, methods);
        ctxtParams.put(TestMeTemplateParams.TESTED_CLASS_TYPES_IN_DEFAULT_PACKAGE, classElementsLocator.filterTypesInDefaultPackage(methods, fields));
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
//    @NotNull
//    private static String getCalendarValue(final Calendar calendar, final int field) {
//        int val = calendar.get(field);
//        if (field == Calendar.MONTH) val++;
//        final String result = Integer.toString(val);
//        if (result.length() == 1) {
//            return "0" + result;
//        }
//        return result;
//    }

    @NotNull
    private List<Field> getFields(FileTemplateContext context) {
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

    private List<Method> getMethods(PsiClass srcClass) {
        ArrayList<Method> methods = new ArrayList<Method>();
        for (PsiMethod psiMethod : srcClass.getAllMethods()) {
            methods.add(new Method(psiMethod, srcClass));
        }
        return methods;
    }
}
