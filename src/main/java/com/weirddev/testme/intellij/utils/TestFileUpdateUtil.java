package com.weirddev.testme.intellij.utils;

import java.util.*;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilCore;
import com.weirddev.testme.intellij.template.FileTemplateContext;
import com.weirddev.testme.intellij.template.context.TestFileUpdateInfo;
import com.weirddev.testme.intellij.template.context.StringUtils;

public class TestFileUpdateUtil {

    private static final String TYPE_IMPORT = "import";

    private static final String TYPE_FIELD = "field";

    private static final String TYPE_METHOD = "method";

    /**
     * find test file according to test class name
     *
     * @param context       context
     * @param testClassName test class name
     * @return test file
     */
    public static PsiFile getPsiTestFile(FileTemplateContext context, String testClassName) {
        VirtualFile file = context.getTargetDirectory().getVirtualFile();
        String defaultExtension = TestFileTemplateUtil.getLanguageFileType(context.getLanguage()).getDefaultExtension();
        VirtualFile child = file.findChild(testClassName + "." + defaultExtension);
        PsiManager psiManager = PsiManager.getInstance(context.getProject());
        return PsiUtilCore.toPsiFiles(psiManager, List.of(child)).get(0);
    }

    /**
     * generate a new test file if not exists or add a new test method to exist test file
     *
     * @param context              context
     * @param currentTestClassFile generated new test file
     * @return test file
     */
    public static PsiFile generateOrUpdateTestFile(FileTemplateContext context, PsiFile currentTestClassFile) {
        boolean hasTestFile = context.isHasTestFile();
        PsiMethod selectedMethod = context.getSelectedMethod();
        Project project = context.getProject();
        if (!hasTestFile || null == selectedMethod) {
            return currentTestClassFile;
        }
        PsiFile oldTestClassFile = getPsiTestFile(context, context.getTargetClass());
        // build imports element update list
        List<TestFileUpdateInfo> testFileUpdateInfoList =
            resolveUpdatesOfTestImports(oldTestClassFile, currentTestClassFile);

        // get psi class from psi file
        PsiClass currentTestPsiClass = getContainingClass(currentTestClassFile);
        PsiClass oldTestPsiClass = getContainingClass(oldTestClassFile);
        // build class element <method & fields> update list
        List<TestFileUpdateInfo> testClassUpdateInfoList =
            resolveUpdatesOfTestClass(oldTestPsiClass, currentTestPsiClass, selectedMethod);
        testFileUpdateInfoList.addAll(testClassUpdateInfoList);
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);

        PsiElement importStartElementToAdd = getImportStartElementToAdd(oldTestClassFile);
        PsiElement fieldStartElementToAdd = getFieldStartElementToAdd(oldTestPsiClass);
        PsiElement methodStartElementToAdd = getMethodStartElementToAdd(oldTestPsiClass);

        Document document = psiDocumentManager.getDocument(oldTestClassFile);
        // test file update
        for (TestFileUpdateInfo info : testFileUpdateInfoList) {
            if (info.getContentType().equals(TYPE_IMPORT)) {
                info.getParentElement().addAfter(info.getAddElement(), importStartElementToAdd);
            }
            if (info.getContentType().equals(TYPE_METHOD)) {
                info.getParentElement().addBefore(info.getAddElement(), methodStartElementToAdd);
            }
            if (info.getContentType().equals(TYPE_FIELD)) {
                info.getParentElement().addAfter(info.getAddElement(), fieldStartElementToAdd);
            }
        }
        psiDocumentManager.doPostponedOperationsAndUnblockDocument(document);
        psiDocumentManager.commitDocument(document);
        FileDocumentManager.getInstance().saveDocument(document);

        // get and return updated test file
        return getPsiTestFile(context, currentTestClassFile.getName());
    }

    private static List<TestFileUpdateInfo> resolveUpdatesOfTestImports(PsiFile oldTestClassFile,
        PsiFile currentTestClassFile) {
        List<TestFileUpdateInfo> testFileUpdateInfoList = new ArrayList<>();
        if (oldTestClassFile instanceof PsiJavaFile oldJavaTestFile
            && currentTestClassFile instanceof PsiJavaFile currentJavaTestFile) {
            PsiImportList currentImport = currentJavaTestFile.getImportList();
            if (null == currentImport) {
                return testFileUpdateInfoList;
            }
            PsiImportStatementBase[] currentImportList = currentImport.getAllImportStatements();

            PsiImportList oldImport = oldJavaTestFile.getImportList();
            List<String> oldImportStrList;
            if (null == oldImport) {
                oldImportStrList = new ArrayList<>();
            } else {
                PsiImportStatementBase[] oldImportList = oldImport.getAllImportStatements();
                oldImportStrList = Arrays.stream(oldImportList).map(PsiElement::getText).toList();
            }

            for (PsiImportStatementBase importStatementBase : currentImportList) {
                String importText = importStatementBase.getText();
                if (oldImportStrList.contains(importText)) {
                    continue;
                }
                TestFileUpdateInfo updateInfo = new TestFileUpdateInfo(oldImport, importStatementBase, TYPE_IMPORT);
                testFileUpdateInfoList.add(updateInfo);
            }
        }
        return testFileUpdateInfoList;
    }

    /**
     * @param oldTestClass     old exist test class
     * @param currentTestClass new generated test class
     * @param selectedMethod   selected method
     * @return new fields and method info need to add to old exist test class
     */
    public static List<TestFileUpdateInfo> resolveUpdatesOfTestClass(PsiClass oldTestClass, PsiClass currentTestClass,
        PsiMethod selectedMethod) {
        List<TestFileUpdateInfo> testFileUpdateInfoList = new ArrayList<>();

        String testMethodName = "test" + StringUtils.capitalizeFirstLetter(selectedMethod.getName());

        PsiMethod newTestMethod =
            Arrays.stream(currentTestClass.getMethods()).filter(m -> m.getName().equals(testMethodName)).findFirst()
                .orElse(null);
        if (null == newTestMethod) {
            return testFileUpdateInfoList;
        }

        // get info of new fields to add
        List<PsiField> addFieldList = getFieldsToAdd(oldTestClass, currentTestClass);
        for (PsiField field : addFieldList) {
            TestFileUpdateInfo addInfo = new TestFileUpdateInfo(oldTestClass, field, TYPE_FIELD);
            testFileUpdateInfoList.add(addInfo);
        }

        // get info of new test to add
        TestFileUpdateInfo addInfo = new TestFileUpdateInfo(oldTestClass, newTestMethod, TYPE_METHOD);
        testFileUpdateInfoList.add(addInfo);
        return testFileUpdateInfoList;
    }

    /**
     * get psi class of a file
     *
     * @param containingFile file
     * @return class of file
     */
    public static PsiClass getContainingClass(PsiFile containingFile) {
        if (containingFile instanceof PsiClassOwner) {
            final PsiClass[] classes = ((PsiClassOwner)containingFile).getClasses();
            if (classes.length == 1) {
                return classes[0];
            }
        }
        return null;
    }

    /**
     * compare with old exist test class and get the new testFields to add
     *
     * @param oldTestClass     old exist test class
     * @param currentTestClass generated new test class
     * @return new fields to add
     */
    private static List<PsiField> getFieldsToAdd(PsiClass oldTestClass, PsiClass currentTestClass) {
        List<String> oldFieldNameList = Arrays.stream(oldTestClass.getFields()).map(PsiField::getName).toList();
        return Arrays.stream(currentTestClass.getFields()).filter(e -> !oldFieldNameList.contains(e.getName()))
            .toList();
    }

    /**
     * @param testFile old exist test file
     * @return the location where to add new import element
     */
    public static PsiElement getImportStartElementToAdd(PsiFile testFile) {
        if (testFile instanceof PsiJavaFile javaTestFile) {
            if (null == javaTestFile.getImportList()) {
                return javaTestFile.getPackageStatement();
            }
            PsiImportStatementBase[] oldImportList = javaTestFile.getImportList().getAllImportStatements();
            return oldImportList[oldImportList.length - 1];
        } else {
            return testFile.getFirstChild();
        }

    }

    /**
     * @param oldTestClass old exist test class
     * @return the location where to add new method element
     */
    public static PsiElement getMethodStartElementToAdd(PsiClass oldTestClass) {
        // get the element of '}' of class
        return oldTestClass.getRBrace();
    }

    /**
     * @param oldTestClass old exist test class
     * @return the location where to add new field element
     */
    public static PsiElement getFieldStartElementToAdd(PsiClass oldTestClass) {
        // get the element of '{' of class
        return oldTestClass.getLBrace();
    }
}
