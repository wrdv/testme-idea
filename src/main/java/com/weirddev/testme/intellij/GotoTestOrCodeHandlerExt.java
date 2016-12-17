package com.weirddev.testme.intellij;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.testIntegration.GotoTestOrCodeHandler;
import com.intellij.testIntegration.TestFinderHelper;
import com.weirddev.testme.TestMeBundle;
import com.weirddev.testme.intellij.utils.TestSubjectResolverUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Date: 10/15/2016
 * @author Yaron Yamin
 */
public class GotoTestOrCodeHandlerExt extends GotoTestOrCodeHandler {
    private final boolean supportIconTokens;
    private TemplateRegistry templateRegistry;
    private boolean testMeOnlyMode;
    private String alternativeSourceName;

    public GotoTestOrCodeHandlerExt(boolean supportIconTokens, boolean testMeOnlyMode) {
        this(supportIconTokens,testMeOnlyMode,new TemplateRegistry());
    }

    GotoTestOrCodeHandlerExt(boolean supportIconTokens, boolean testMeOnlyMode, TemplateRegistry templateRegistry) {
        this.supportIconTokens = supportIconTokens;
        this.testMeOnlyMode = testMeOnlyMode;
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
            if (testMeOnlyMode) {
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
            }
            int index=0;
            for (final TemplateDescriptor templateDescriptor : templateDescriptors) {
                sourceAndTargetElements.additionalActions.add(index++, new TestMeAdditionalAction(supportIconTokens, templateDescriptor, editor, file) );
            }
        }
        return sourceAndTargetElements;
    }

    @NotNull
    @Override
    protected String getChooserTitle(PsiElement sourceElement, String name, int length) {
        if (testMeOnlyMode) {
//            todo  remove pin
            return TestMeBundle.message("testMe.create.title", alternativeSourceName !=null? alternativeSourceName :name);
        } else if (!TestFinderHelper.isTest(sourceElement) && length==0) {
            // todo  remove pin or place back default text
            return TestMeBundle.message("goto.test.create.title", name);
        } else {
            return super.getChooserTitle(sourceElement, name, length);
        }
    }
}
