package com.weirddev.testme.intellij;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.testIntegration.GotoTestOrCodeHandler;
import com.weirddev.testme.TestMeBundle;
import com.weirddev.testme.intellij.utils.TestSubjectResolverUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Date: 10/15/2016
 * @author Yaron Yamin
 */
public class TestMeActionHandler extends GotoTestOrCodeHandler {
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
        GotoData sourceAndTargetElements = super.getSourceAndTargetElements(editor, file);
        if (sourceAndTargetElements == null) return null;
        if (TestSubjectResolverUtils.isValidForTesting(editor, file)) {
            List<TemplateDescriptor> templateDescriptors = templateRegistry.getTemplateDescriptors();
            alternativeSourceName = null;
            sourceAndTargetElements.targets = new PsiElement[]{};
            sourceAndTargetElements.additionalActions.clear();
            PsiElement element = TestSubjectResolverUtils.getElement(editor, file);
            if (element != null) {
                PsiClass containingClass = CreateTestMeAction.getContainingClass(element);
                if (containingClass != null) {
                    final String name = ((PsiNamedElement)sourceAndTargetElements.source).getName();
                    if (containingClass.getName()!=null && !containingClass.getName().equals(name)) {
                        //todo remove refactor and temp WA and don't store this on the instance
                        alternativeSourceName = containingClass.getName();
                    }
                }
            }
            int index=0;
            for (final TemplateDescriptor templateDescriptor : templateDescriptors) {
                sourceAndTargetElements.additionalActions.add(index++, new TestMeAdditionalAction(true, templateDescriptor, editor, file) );
            }
        }
        return sourceAndTargetElements;
    }
    //            todo  remove popup pin
    @NotNull
    @Override
    protected String getChooserTitle(PsiElement sourceElement, String name, int length) {

        return TestMeBundle.message("testMe.create.title", alternativeSourceName !=null? alternativeSourceName :name);
    }
}
