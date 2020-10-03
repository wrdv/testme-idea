package com.weirddev.testme.intellij.action;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.testIntegration.GotoTestOrCodeHandler;
import com.intellij.testIntegration.TestFinderHelper;
import com.intellij.util.SmartList;
import com.weirddev.testme.intellij.TestMeBundle;
import com.weirddev.testme.intellij.template.TemplateDescriptor;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.ui.popup.ConfigurationLinkAction;
import com.weirddev.testme.intellij.ui.popup.TestMePopUpHandler;
import com.weirddev.testme.intellij.ui.template.TestMeTemplateManager;
import com.weirddev.testme.intellij.utils.TestSubjectResolverUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Date: 10/15/2016
 * @author Yaron Yamin
 * @see GotoTestOrCodeHandler
 */
public class TestMeActionHandler extends TestMePopUpHandler {
    private TemplateRegistry templateRegistry;

    public TestMeActionHandler() {
        this(new TemplateRegistry());
    }

    TestMeActionHandler(TemplateRegistry templateRegistry) {
        this.templateRegistry = templateRegistry;
    }

    @Nullable
    @Override
    protected GotoData getSourceAndTargetElements(final Editor editor, final PsiFile file) {
        PsiElement sourceElement = TestFinderHelper.findSourceElement(getSelectedElement(editor, file));
        if (sourceElement == null) return null;
        List<AdditionalAction> actions = new SmartList<AdditionalAction>();
        findNestedClassName(editor, file, (PsiNamedElement) sourceElement);
        TestMeTemplateManager fileTemplateManager = TestMeTemplateManager.getInstance(file.getProject());
        List<TemplateDescriptor> templateDescriptors = fileTemplateManager.getTestTemplates();
        for (final TemplateDescriptor templateDescriptor : templateDescriptors) {
            actions.add(new TestMeAdditionalAction(templateDescriptor, editor, file) );
        }
        actions.add(new ConfigurationLinkAction());
        return new GotoData(sourceElement, actions);
    }

    @NotNull
    @Override
    protected String getChooserTitle(Editor editor, PsiFile file, PsiElement sourceElement) {
        PsiNamedElement namedElement = (PsiNamedElement) sourceElement;
        final String name = namedElement.getName();
        String nestedClassName = findNestedClassName(editor, file, namedElement);
        return TestMeBundle.message("testMe.create.title", nestedClassName !=null? nestedClassName :name);
    }

    private String findNestedClassName(Editor editor, PsiFile file, PsiNamedElement sourceElement) {
        String alternativeSourceName = null;
        PsiElement element = TestSubjectResolverUtils.getTestableElement(editor, file);
        if (element != null) {
            PsiClass containingClass = CreateTestMeAction.getContainingClass(element);
            if (containingClass != null) {
                final String name = sourceElement.getName();
                if (containingClass.getName()!=null && !containingClass.getName().equals(name)) {
                    alternativeSourceName = containingClass.getName();
                }
            }
        }
        return alternativeSourceName;
    }

    @Override
    protected String getFeatureUsedKey() {
        return "TestMe.generate.test"; //todo - map key. see lazyLoadFromPluginsFeaturesProviders()
    }

    @NotNull
    private static PsiElement getSelectedElement(Editor editor, PsiFile file) {
        return PsiUtilCore.getElementAtOffset(file, editor.getCaretModel().getOffset());
    }


    @NotNull
    @Override
    protected String getNotFoundMessage(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        return CodeInsightBundle.message("goto.test.notFound");
    }

    @Nullable
    @Override
    protected String getAdText(PsiElement source, int length) {//todo might be useful for generate and run functionality, currently un-used
//        if (length > 0 && !TestFinderHelper.isTest(source)) {
//            final Keymap keymap = KeymapManager.getInstance().getActiveKeymap();
//            final Shortcut[] shortcuts = keymap.getShortcuts(DefaultRunExecutor.getRunExecutorInstance().getContextActionId());
//            if (shortcuts.length > 0) {
//                return ("Press " + KeymapUtil.getShortcutText(shortcuts[0]) + " to run selected tests");
//            }
//        }
        return null;
    }

    @Override
    protected void navigateToElement(Navigatable element) {
        if (element instanceof PsiElement) {
            NavigationUtil.activateFileWithPsiElement((PsiElement)element, true);
        }
        else {
            element.navigate(true);
        }
    }
}
