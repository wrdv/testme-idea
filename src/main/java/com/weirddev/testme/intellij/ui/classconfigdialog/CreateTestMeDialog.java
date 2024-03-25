package com.weirddev.testme.intellij.ui.classconfigdialog;

import java.util.*;
import java.util.List;

import javax.swing.*;

import com.intellij.psi.*;
import com.intellij.psi.util.InheritanceUtil;
import com.weirddev.testme.intellij.common.utils.PsiMethodUtils;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.template.context.*;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.refactoring.ui.MemberSelectionTable;
import com.intellij.refactoring.util.classMembers.MemberInfo;
import com.intellij.ui.ScrollPaneFactory;

/**
 *
 * dialog for user to check fields mockable and methods testable
 *
 * @author huangliang
 */
public class CreateTestMeDialog extends DialogWrapper {

    private final Map<String, Object> templateCtxtParams;
    private final PsiClass myTargetClass;
    private final String templateFileName;
    private final MemberSelectionTable myMethodsTable = new MemberSelectionTable(Collections.emptyList(), null);
    private final MemberSelectionTable myFieldsTable = new MemberSelectionTable(Collections.emptyList(), null);
    private final List<String> checkedFieldNameList = new ArrayList<>();
    private final List<String> checkedMethodIdList = new ArrayList<>();


    public CreateTestMeDialog(@NotNull Project project, @NotNull @NlsContexts.DialogTitle String title,
        PsiClass targetClass, String templateFileName, Map<String, Object> templateCtxtParams) {
        super(project, true);
        myTargetClass = targetClass;
        this.templateCtxtParams = templateCtxtParams;
        this.templateFileName = templateFileName;
        setTitle(title);
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel fieldLabel = new JLabel("Select Need Mocked Fields");
        panel.add(fieldLabel);
        panel.add(ScrollPaneFactory.createScrollPane(myFieldsTable));

        JLabel methodLabel = new JLabel("Select Need Test Methods");
        panel.add(methodLabel);
        panel.add(ScrollPaneFactory.createScrollPane(myMethodsTable));

        initExtractingClassMembers();
        return panel;
    }

    /**
     * update field is mockable or not after user checked
     */
    private void updateClassMockableFields() {
        Collection<MemberInfo> selectedMemberInfos = myFieldsTable.getSelectedMemberInfos();
        List<String> userCheckedMockableFieldsList =
            selectedMemberInfos.stream().map(e -> e.getMember().getName()).toList();
        Type classTypeObj = (Type)templateCtxtParams.get(TestMeTemplateParams.TESTED_CLASS);
        for (Field field : classTypeObj.getFields()) {
            if (userCheckedMockableFieldsList.contains(field.getName())) {
                checkedFieldNameList.add(field.getName());
            }
        }
        templateCtxtParams.put(TestMeTemplateParams.USER_CHECKED_MOCK_FIELDS, checkedFieldNameList);
    }

    /**
     * update method is testable or not after user checked
     */
    private void updateClassTestableMethods() {
        Collection<MemberInfo> selectedMemberInfos = myMethodsTable.getSelectedMemberInfos();
        List<String> testableMethodList =
            selectedMemberInfos.stream().map(e -> PsiMethodUtils.formatMethodId((PsiMethod)e.getMember())).toList();
        Type classTypeObj = (Type)templateCtxtParams.get(TestMeTemplateParams.TESTED_CLASS);
        for (Method method : classTypeObj.getMethods()) {
            if (testableMethodList.contains(method.getMethodId())) {
                checkedMethodIdList.add(method.getMethodId());
            }
        }
        templateCtxtParams.put(TestMeTemplateParams.USER_CHECKED_TEST_METHODS, checkedMethodIdList);
    }

    /**
     * init and extract class fields and methods for user to check
     */
    public void initExtractingClassMembers() {
        Set<PsiClass> classes= InheritanceUtil.getSuperClasses(myTargetClass);
        classes.add(myTargetClass);

        Type classTypeObj = (Type)templateCtxtParams.get(TestMeTemplateParams.TESTED_CLASS);
        TestSubjectInspector testSubjectUtil =
            (TestSubjectInspector)templateCtxtParams.get(TestMeTemplateParams.TestSubjectUtils);
        MockBuilder templateMockBuilder = getTemplateMockBuilder(templateFileName);

        // build field mockable map, key = field name, value = true - if field mockable
        Map<String, Boolean> fieldMockableMap = new HashMap<>();
        for (Field field : classTypeObj.getFields()) {
            Boolean fieldMockable = templateMockBuilder.isMockable(field, classTypeObj);
            fieldMockableMap.put(field.getName(), fieldMockable);
        }
        // build method testable map, key = method id, value = true - if method testable
        Map<String, Boolean> methodTestableMap = new HashMap<>();
        for (Method method : classTypeObj.getMethods()) {
            Boolean testable = testSubjectUtil.shouldBeTested(method);
            methodTestableMap.put(method.getMethodId(), testable);
        }

        // init method table and field table
        List<MemberInfo> methodResult = new ArrayList<>();
        List<MemberInfo> fieldResult = new ArrayList<>();
        for (PsiClass aClass : classes) {
            if (CommonClassNames.JAVA_LANG_OBJECT.equals(aClass.getQualifiedName()))
                continue;
            initMethodsTable(aClass, methodResult, methodTestableMap);
            initFieldsTable(aClass, fieldResult, fieldMockableMap);
        }
    }

    /**
     * get mock builder for template
     * @param templateFileName template name
     * @return MockBuilder
     */
    private MockBuilder getTemplateMockBuilder(String templateFileName) {
        if (TemplateRegistry.JUNIT4_POWERMOCK_JAVA_TEMPLATE.equals(templateFileName)) {
            return (PowerMockBuilder)templateCtxtParams.get(TestMeTemplateParams.PowerMockBuilder);
        } else {
            return (MockitoMockBuilder)templateCtxtParams.get(TestMeTemplateParams.MockitoMockBuilder);
        }
    }

    private void initMethodsTable(PsiClass myTargetClass, List<MemberInfo> result, Map<String, Boolean> methodTestableMap) {
        Set<PsiMember> selectedMethods = new HashSet<>();
        MemberInfo.extractClassMembers(myTargetClass, result, member -> {
            if (!(member instanceof PsiMethod method))
                return false;
            String methodId = PsiMethodUtils.formatMethodId(method);
            if (methodTestableMap.containsKey(methodId) && methodTestableMap.get(methodId)) {
                selectedMethods.add(member);
                return true;
            }
            return false;
        }, false);

        for (MemberInfo each : result) {
            each.setChecked(selectedMethods.contains(each.getMember()));
        }

        myMethodsTable.setMemberInfos(result);
    }

    private void initFieldsTable(PsiClass myTargetClass, List<MemberInfo> result, Map<String, Boolean> fieldMockableMap) {
        Set<PsiMember> selectedFields = new HashSet<>();
        MemberInfo.extractClassMembers(myTargetClass, result, member -> {
            if (!(member instanceof PsiField field))
                return false;
            if (fieldMockableMap.containsKey(field.getName()) && fieldMockableMap.get(field.getName())) {
                selectedFields.add(member);
                return true;
            }
            return false;
        }, false);

        for (MemberInfo each : result) {
            each.setChecked(selectedFields.contains(each.getMember()));
        }

        myFieldsTable.setMemberInfos(result);
    }

    @Override
    protected String getDimensionServiceKey() {
        return getClass().getName();
    }

    @Override
    protected void doOKAction() {
        updateClassMockableFields();
        updateClassTestableMethods();
        super.doOKAction();
    }

}
