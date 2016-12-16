package com.weirddev.testme.intellij;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testIntegration.GotoTestOrCodeHandler;
import com.intellij.testIntegration.TestFinderHelper;
import com.intellij.testIntegration.createTest.CreateTestAction;
import com.weirddev.testme.intellij.icon.Icons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Date: 10/15/2016
 * @author Yaron Yamin
 */
public class GotoTestOrCodeHandlerExt extends GotoTestOrCodeHandler {
    private final boolean supportIconTokens;
    private final TestMeCreator testMeCreator;
    private TemplateRegistry templateRegistry;
    private boolean testMeOnlyMode;

    public GotoTestOrCodeHandlerExt(boolean supportIconTokens, boolean testMeOnlyMode) {
        this(new TestMeCreator(), supportIconTokens,new TemplateRegistry());
        this.testMeOnlyMode = testMeOnlyMode;
    }

    GotoTestOrCodeHandlerExt(TestMeCreator testMeCreator, boolean supportIconTokens, TemplateRegistry templateRegistry) {
        this.supportIconTokens = supportIconTokens;
        this.testMeCreator = testMeCreator;
        this.templateRegistry = templateRegistry;
    }

    @Nullable
    @Override
    protected GotoData getSourceAndTargetElements(final Editor editor, final PsiFile file) {
        GotoData sourceAndTargetElements = super.getSourceAndTargetElements(editor, file);
        if (sourceAndTargetElements == null) return null;
        PsiElement selectedElement = getSelectedElement(editor, file);
        if (!TestFinderHelper.isTest(selectedElement) && CreateTestAction.isAvailableForElement(selectedElement)) {
            List<TemplateDescriptor> templateDescriptors = templateRegistry.getTemplateDescriptors();
            if (testMeOnlyMode) {
                sourceAndTargetElements.targets = new PsiElement[]{};
                sourceAndTargetElements.additionalActions.clear();
            }
            int index=0;
            for (final TemplateDescriptor templateDescriptor : templateDescriptors) {//todo pull anonymous class up
                sourceAndTargetElements.additionalActions.add(index++, new TestMeAdditionalAction(supportIconTokens? templateDescriptor.getTokenizedDisplayName():templateDescriptor.getDisplayName(), Icons.TEST_ME) {
                    @Override
                    public void execute() {
                        testMeCreator.createTest(editor, file, templateDescriptor.getFilename());
                    }
                });
            }
        }
        return sourceAndTargetElements;
    }
    @NotNull
    @Override
    protected String getChooserTitle(PsiElement sourceElement, String name, int length) {
        if (testMeOnlyMode) {
            return "TestMe";
        } else {
            return super.getChooserTitle(sourceElement, name, length);
        }
    }
}
