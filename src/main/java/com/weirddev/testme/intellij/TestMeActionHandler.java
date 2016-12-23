package com.weirddev.testme.intellij;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.keymap.KeymapUtil;
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
import com.weirddev.testme.TestMeBundle;
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
    private String alternativeSourceName;

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
        alternativeSourceName = null;
        PsiElement element = TestSubjectResolverUtils.getElement(editor, file);
        if (element != null) {
            PsiClass containingClass = CreateTestMeAction.getContainingClass(element);
            if (containingClass != null) {
                final String name = ((PsiNamedElement)sourceElement).getName();
                if (containingClass.getName()!=null && !containingClass.getName().equals(name)) {
                    //todo remove refactor and temp WA and don't store this on the instance
                    alternativeSourceName = containingClass.getName();
                }
            }
        }
        List<TemplateDescriptor> templateDescriptors = templateRegistry.getTemplateDescriptors();
        for (final TemplateDescriptor templateDescriptor : templateDescriptors) {
            actions.add(new TestMeAdditionalAction(templateDescriptor, editor, file) );
        }
        return new GotoData(sourceElement, new PsiElement[]{}, actions);
    }

    @NotNull
    @Override
    protected String getChooserTitle(PsiElement sourceElement, String name, int length) {

        return TestMeBundle.message("testMe.create.title", alternativeSourceName !=null? alternativeSourceName :name);
    }

    @Override
    protected String getFeatureUsedKey() {
        return "testMe.generate.test"; //todo - is this useful? see lazyLoadFromPluginsFeaturesProviders()
    }

    @NotNull
    public static PsiElement getSelectedElement(Editor editor, PsiFile file) {
        return PsiUtilCore.getElementAtOffset(file, editor.getCaretModel().getOffset());
    }

    @Override
    protected boolean shouldSortTargets() {
        return false;
    }

    @NotNull
    @Override
    protected String getFindUsagesTitle(PsiElement sourceElement, String name, int length) {
        //todo remove after pin feature removed
        return CodeInsightBundle.message("goto.test.findUsages.test.title", name);
    }

    @NotNull
    @Override
    protected String getNotFoundMessage(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        return CodeInsightBundle.message("goto.test.notFound");
    }

    @Nullable
    @Override
    protected String getAdText(PsiElement source, int length) {//todo adapt after introducing generate and run functionality? currently will not be called since targets.length == 0
        if (length > 0 && !TestFinderHelper.isTest(source)) {
            final Keymap keymap = KeymapManager.getInstance().getActiveKeymap();
            final Shortcut[] shortcuts = keymap.getShortcuts(DefaultRunExecutor.getRunExecutorInstance().getContextActionId());
            if (shortcuts.length > 0) {
                return ("Press " + KeymapUtil.getShortcutText(shortcuts[0]) + " to run selected tests");
            }
        }
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
