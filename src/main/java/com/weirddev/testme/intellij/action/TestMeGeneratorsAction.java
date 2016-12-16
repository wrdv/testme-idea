package com.weirddev.testme.intellij.action;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiClass;
import com.intellij.testIntegration.TestFinderHelper;
import com.weirddev.testme.intellij.GotoTestOrCodeHandlerFactory;

/**
 * Date: 16/12/2016
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorsAction extends BaseGenerateAction {
    public TestMeGeneratorsAction() {
        super(GotoTestOrCodeHandlerFactory.create(true));
    }

    @Override
    protected boolean isValidForClass(PsiClass targetClass) {
        return super.isValidForClass(targetClass) && !(targetClass instanceof PsiAnonymousClass)&& !TestFinderHelper.isTest(targetClass);
        //todo defect: should work if invoked outside class
        //todo add more conditions and match conditions on goto test validation
    }
}
