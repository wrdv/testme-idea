package com.weirddev.testme.intellij.action;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.psi.PsiClass;
import com.weirddev.testme.intellij.generator.TestMeGeneratorTestBase;
import com.weirddev.testme.intellij.icon.Icons;
import com.weirddev.testme.intellij.template.context.Language;
import org.junit.Assert;

/**
 * Date: 05/05/2017
 *
 * @author Yaron Yamin
 */
public class GotoTestOrCodeHandlerExtTest extends TestMeGeneratorTestBase {
    public GotoTestOrCodeHandlerExtTest() {
        super(null, "test", Language.Java);
    }

    public void testNestedClassParams() throws Exception {
        doTest();
    }

    @Override
    protected void doTest(final String packageName, final String testSubjectClassName, final String expectedTestClassName, final boolean reformatCode, final boolean optimizeImports, final boolean replaceFqn, final boolean ignoreUnusedProperties, final int minPercentOfExcessiveSettersToPreferDefaultCtor) {
        final PsiClass fooClass = setupSourceFiles(packageName, testSubjectClassName);
        CommandProcessor.getInstance().executeCommand(getProject(), new Runnable() {
            @Override
            public void run() {
                myFixture.openFileInEditor(fooClass.getContainingFile().getVirtualFile());
                final GotoTestOrCodeHandlerExt gotoTestOrCodeHandlerExt = new GotoTestOrCodeHandlerExt();
                final GotoTargetHandler.GotoData sourceAndTargetElements = gotoTestOrCodeHandlerExt.getSourceAndTargetElements(getEditor(), getFile());
                verifyGotoDataActions(sourceAndTargetElements, packageName, expectedTestClassName);
            }
        }, CodeInsightBundle.message("intention.create.test"), this);
    }

    protected void verifyGotoDataActions(GotoTargetHandler.GotoData sourceAndTargetElements, String packageName, String expectedTestClassName) {
        Assert.assertNotNull(sourceAndTargetElements);
        Assert.assertEquals(2,sourceAndTargetElements.additionalActions.size());
        final GotoTargetHandler.AdditionalAction firstAction = sourceAndTargetElements.additionalActions.get(0);
        Assert.assertEquals("TestMe...", firstAction.getText());
        Assert.assertEquals(Icons.TEST_ME, firstAction.getIcon());
        firstAction.execute();
    }
}
