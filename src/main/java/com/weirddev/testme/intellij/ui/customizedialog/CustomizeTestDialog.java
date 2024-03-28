package com.weirddev.testme.intellij.ui.customizedialog;

import java.util.*;
import java.util.List;

import javax.swing.*;

import com.intellij.psi.*;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiUtil;
import com.weirddev.testme.intellij.builder.MethodFactory;
import com.weirddev.testme.intellij.common.utils.PsiMethodUtils;
import com.weirddev.testme.intellij.generator.MockBuilderFactory;
import com.weirddev.testme.intellij.template.FileTemplateContext;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.template.context.*;
import com.weirddev.testme.intellij.utils.ClassNameUtils;
import com.weirddev.testme.intellij.utils.JavaPsiTreeUtils;
import com.weirddev.testme.intellij.utils.JavaTypeUtils;
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
public class CustomizeTestDialog extends DialogWrapper {

    private final PsiClass myTargetClass;
    private final MemberSelectionTable myMethodsTable = new MemberSelectionTable(Collections.emptyList(), null);
    private final MemberSelectionTable myFieldsTable = new MemberSelectionTable(Collections.emptyList(), null);
    private final FileTemplateContext fileTemplateContext;

    public CustomizeTestDialog(@NotNull Project project, @NotNull @NlsContexts.DialogTitle String title,
        PsiClass targetClass, FileTemplateContext fileTemplateContext) {
        super(project, true);
        myTargetClass = targetClass;
        this.fileTemplateContext = fileTemplateContext;
        initMethodsTable(myTargetClass);
        initFieldsTable(myTargetClass, fileTemplateContext.getFileTemplateDescriptor().getDisplayName());
        setTitle(title);
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (myFieldsTable.getRowCount() > 0) {
            JLabel fieldLabel = new JLabel("Mock fields:");
            panel.add(fieldLabel);
            panel.add(ScrollPaneFactory.createScrollPane(myFieldsTable));
        }

        JLabel methodLabel = new JLabel("Test Methods:");
        panel.add(methodLabel);
        panel.add(ScrollPaneFactory.createScrollPane(myMethodsTable));

        return panel;
    }

    /**
     * update field is mockable or not after user checked
     */
    private void updateClassMockableFields() {
        Collection<MemberInfo> selectedMemberInfos = myFieldsTable.getSelectedMemberInfos();
        List<String> userCheckedMockableFieldsList =
            selectedMemberInfos.stream().map(e -> e.getMember().getName()).toList();
        fileTemplateContext.getFileTemplateCustomization().getSelectedFieldNameList()
            .addAll(userCheckedMockableFieldsList);
    }

    /**
     * update method is testable or not after user checked
     */
    private void updateClassTestableMethods() {
        Collection<MemberInfo> selectedMemberInfos = myMethodsTable.getSelectedMemberInfos();
        List<String> testableMethodList =
            selectedMemberInfos.stream().map(e -> PsiMethodUtils.formatMethodId((PsiMethod)e.getMember())).toList();
        fileTemplateContext.getFileTemplateCustomization().getSelectedMethodIdList().addAll(testableMethodList);
    }

    /**
     *
     * @param templateFileName template name
     * @return true - if final can be mocked
     */
    private boolean canMockFinal(String templateFileName) {
        return TemplateRegistry.JUNIT4_POWERMOCK_JAVA_TEMPLATE.equals(templateFileName);
    }

    private List<MemberInfo> initMethodsTable(PsiClass myTargetClass) {
        Set<PsiClass> classes = new HashSet<>();
        InheritanceUtil.getSuperClasses(myTargetClass, classes, false);
        classes.add(myTargetClass);

        Set<PsiMember> selectedMethods = new HashSet<>();
        List<MemberInfo> result = new ArrayList<>();
        // init method table and field table
        for (PsiClass aClass : classes) {
            MemberInfo.extractClassMembers(aClass, result, member -> {
                if (!(member instanceof PsiMethod method))
                    return false;
                if (shouldBeTested(method, myTargetClass)) {
                    selectedMethods.add(member);
                    return true;
                }
                return false;
            }, false);
        }

        for (MemberInfo each : result) {
            each.setChecked(selectedMethods.contains(each.getMember()));
        }

        myMethodsTable.setMemberInfos(result);
        return result;
    }

    private List<MemberInfo> initFieldsTable(PsiClass myTargetClass, String templateFileName) {
        Set<PsiMember> selectedFields = new HashSet<>();
        List<MemberInfo> result = new ArrayList<>();
        MemberInfo.extractClassMembers(myTargetClass, result, member -> {
            if (!(member instanceof PsiField field))
                return false;
            if (isMockable(field, myTargetClass, templateFileName)) {
                selectedFields.add(member);
                return true;
            }
            return false;
        }, false);

        for (MemberInfo each : result) {
            each.setChecked(selectedFields.contains(each.getMember()));
        }

        myFieldsTable.setMemberInfos(result);
        return result;
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

    private boolean isMockable(PsiField psiField, PsiClass testedClass, String templateFileName) {
        boolean overridden = Field.isOverriddenInChild(psiField, testedClass);
        boolean isFinal =
            psiField.getModifierList() != null && psiField.getModifierList().hasExplicitModifier(PsiModifier.FINAL);
        boolean isPrimitive = psiField.getType() instanceof PsiPrimitiveType;
        PsiClass psiClass = PsiUtil.resolveClassInType(psiField.getType());
        String canonicalText = JavaTypeUtils.resolveCanonicalName(psiClass, null);
        boolean isArray = ClassNameUtils.isArray(canonicalText);
        boolean isEnum = JavaPsiTreeUtils.resolveIfEnum(psiClass);
        boolean isMockitoMockMakerInlineOn = MockBuilderFactory.isMockInline(fileTemplateContext);
        boolean isWrapperType = MockitoMockBuilder.WRAPPER_TYPES.contains(canonicalText);
        return !isPrimitive && !isWrapperType
            && (canMockFinal(templateFileName) || !isFinal || isMockitoMockMakerInlineOn) && !overridden && !isArray
            && !isEnum;
    }

    /**
     * true - method should test
     */
    private boolean shouldBeTested(PsiMethod method, PsiClass psiClass) {
        return MethodFactory.isTestable(method, psiClass);
    }

}
