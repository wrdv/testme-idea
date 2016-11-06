package com.weirddev.testme.intellij;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.codeInsight.FileModificationService;
import com.intellij.execution.junit.JUnit4Framework;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PostprocessReformattingAspect;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopesCore;
import com.intellij.testIntegration.TestFramework;
import com.intellij.testIntegration.createTest.JavaTestGenerator;
import com.intellij.util.IncorrectOperationException;
import com.weirddev.testme.intellij.template.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Date: 10/19/2016
 *
 * @author Yaron Yamin
 * @see JavaTestGenerator
 */
public class TestMeGenerator {
    public PsiElement generateTest(final FileTemplateContext context) {
        final Project project = context.getProject();
        return PostprocessReformattingAspect.getInstance(project).postponeFormattingInside(new Computable<PsiElement>() {
            public PsiElement compute() {
                return ApplicationManager.getApplication().runWriteAction(new Computable<PsiElement>() {
                    public PsiElement compute() {
                        try {
                            IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();

                            PsiClass targetClass = createTestClass(context);
                            if (targetClass == null) {
                                return null;
                            }
                            final TestFramework frameworkDescriptor = new JUnit4Framework();// context.getSelectedTestFrameworkDescriptor();
//                            final String defaultSuperClass = frameworkDescriptor.getDefaultSuperClass();
//                            final String superClassName = null;//context.getSuperClassName();
//                            if (!Comparing.strEqual(superClassName, defaultSuperClass)) {
//                                addSuperClass(targetClass, project, superClassName);
//                            }
                            if (targetClass != null) {
                                Editor editor = CodeInsightUtil.positionCursor(project, targetClass.getContainingFile(), targetClass.getLBrace());
                            }

//                            addTestMethods(editor,
//                                    targetClass,
//                                    frameworkDescriptor,
//                                    null,//context.getSelectedMethods(),
//                                    true /*context.shouldGeneratedBefore()*/,
//                                    true /*context.shouldGeneratedAfter()*/);
                            return targetClass;
                        }
                        catch (IncorrectOperationException e) {
                            showErrorLater(project, context.getTargetClass());
                            return null;
                        }
                    }
                });
            }
        });
    }

    @Nullable
    private PsiClass createTestClass(FileTemplateContext context) {
        final TestFramework testFrameworkDescriptor = new JUnit4Framework(); //TODO consider removing the dependency
//        final FileTemplateDescriptor fileTemplateDescriptor = TestIntegrationUtils.MethodKind.TEST_CLASS.getFileTemplateDescriptor(testFrameworkDescriptor);
        final PsiDirectory targetDirectory = context.getTargetDirectory();

        final PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage(targetDirectory);
        if (aPackage != null) {
            final GlobalSearchScope scope = GlobalSearchScopesCore.directoryScope(targetDirectory, false);
            final PsiClass[] classes = aPackage.findClassByShortName(context.getTargetClass(), scope);
            if (classes.length > 0) {
                if (!FileModificationService.getInstance().preparePsiElementForWrite(classes[0])) {
                    return null;
                }
                return classes[0];
            }
        }
//        if (fileTemplateDescriptor != null) {
            final PsiClass classFromTemplate = createTestClassFromCodeTemplate(context, targetDirectory);
            if (classFromTemplate != null) {
                return classFromTemplate;
            }
//        }

        return JavaDirectoryService.getInstance().createClass(targetDirectory, context.getTargetClass());
    }

    private PsiClass createTestClassFromCodeTemplate(final FileTemplateContext context, final PsiDirectory targetDirectory) {
        final String templateName = context.getFileTemplateDescriptor().getFileName();
        FileTemplateManager fileTemplateManager = FileTemplateManager.getInstance(targetDirectory.getProject());
        HashMap<String, Object> templateCtxtParams = initTemplateContext(fileTemplateManager.getDefaultProperties());
        templateCtxtParams.put(TestMeTemplateParams.CLASS_NAME, context.getTargetClass());
        templateCtxtParams.put(TestMeTemplateParams.PACKAGE_NAME, context.getTargetPackage().getQualifiedName());
        List<Field> fields = getFields(context);
        templateCtxtParams.put(TestMeTemplateParams.TESTED_CLASS_FIELDS, fields);
        List<Method> methods = getMethods(context);
        templateCtxtParams.put(TestMeTemplateParams.TESTED_CLASS_METHODS, methods);
        templateCtxtParams.put(TestMeTemplateParams.TESTED_CLASS_TYPES_IN_DEFAULT_PACKAGE, filterTypesInDefaultPackage(methods,fields));

        final PsiClass targetClass = context.getSrcClass();
        if (targetClass != null && targetClass.isValid()) {
            templateCtxtParams.put(TestMeTemplateParams.TESTED_CLASS_NAME, targetClass.getName());
        }
        try {
            FileTemplate codeTemplate = fileTemplateManager.getCodeTemplate(templateName);
            codeTemplate.setReformatCode(context.isReformatCode());
            final PsiElement psiElement = FileTemplateUtil.createFromTemplate(codeTemplate, templateName, templateCtxtParams, targetDirectory,null);
            if (psiElement instanceof PsiClass) {
                return (PsiClass)psiElement;
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }

    private Set<String> filterTypesInDefaultPackage(List<Method> methods, List<Field> fields) {
        HashSet<String> typesInDefaultPackage = new HashSet<String>();
        for (Field field : fields) {
            addTypesInDefaultPackage(typesInDefaultPackage, field.getType());
        }
        for (Method method : methods) {
            for (Param param : method.getMethodParams()) {
                addTypesInDefaultPackage(typesInDefaultPackage, param.getType());
            }
        }
        return typesInDefaultPackage;
    }

    private void addTypesInDefaultPackage(HashSet<String> typesInDefaultPackage, Type type) {
        if ((type.getPackageName()==null || type.getPackageName().isEmpty()) && !type.isPrimitive()) {
            typesInDefaultPackage.add(type.getName());
        }
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
    private List<Field> getFields(FileTemplateContext context) {
        ArrayList<Field> fields = new ArrayList<Field>();
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(context.getProject());
        PsiClass srcClass = context.getSrcClass();
        for (PsiField psiField : srcClass.getAllFields()) {
            //TODO research how different types should be handled - i.e. PsiClassType ?
            //TODO handle fields initialized inline/in default constructor
            fields.add(new Field(psiField, javaPsiFacade.findClass(psiField.getType().getCanonicalText(), GlobalSearchScope.allScope(context.getProject())),srcClass));
        }
        return fields;
    }

    private List<Method> getMethods(FileTemplateContext context) {
        ArrayList<Method> methods = new ArrayList<Method>();
        //TODO should use only getMethods() ?  or indicator if method inherited ?
        for (PsiMethod psiMethod : context.getSrcClass().getAllMethods()) {
            methods.add(new Method(psiMethod,context.getSrcClass()));
        }
        return methods;
    }

    private static void showErrorLater(final Project project, final String targetClassName) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                Messages.showErrorDialog(project,
                        CodeInsightBundle.message("intention.error.cannot.create.class.message", targetClassName),
                        CodeInsightBundle.message("intention.error.cannot.create.class.title"));
            }
        });
    }


    @Override
    public String toString() {
        return CodeInsightBundle.message("intention.create.test.dialog.java");
    }
}
