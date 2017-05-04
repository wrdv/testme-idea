package com.weirddev.testme.intellij.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.psi.PsiClass;
import com.weirddev.testme.intellij.generator.TestMeGeneratorTestBase;
import org.junit.Assert;

import javax.swing.event.MenuKeyEvent;

/**
 * Date: 03/05/2017
 *
 * @author Yaron Yamin
 */
public class TestMeActionTest extends TestMeGeneratorTestBase {
    public TestMeActionTest() {
        super(null, "test");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        System.setProperty("testme.popoup.center", "true");//WA swing error when popup set relative to fake test editor
    }
    public void testSimpleClass() throws Exception {
        doTest();
    }

    @Override
    protected void doTest(final String packageName, final String testSubjectClassName, final String expectedTestClassName, final boolean reformatCode, final boolean optimizeImports, final boolean replaceFqn) {

        final PsiClass fooClass = setupSourceFiles(packageName, testSubjectClassName);
        CommandProcessor.getInstance().executeCommand(getProject(), new Runnable() {
            @Override
            public void run() {
                myFixture.openFileInEditor(fooClass.getContainingFile().getVirtualFile());
                final AnAction action = invokeAction();
                Assert.assertEquals(action.getTemplatePresentation().getText(),"TestMe...");
                Assert.assertTrue("opened instance of "+TestMeAction.class,action instanceof TestMeAction);
                final TestMeAction testMeAction = (TestMeAction) action;
                Assert.assertTrue(testMeAction.getTemplatePresentation().isEnabled());
                Assert.assertTrue(testMeAction.getTemplatePresentation().isVisible());
                final CodeInsightActionHandler actionHandler = testMeAction.getActionHandler();
                Assert.assertTrue("action handler is instance of "+TestMeActionHandler.class,actionHandler instanceof TestMeActionHandler);

            }
        }, CodeInsightBundle.message("intention.create.test"), this);
    }

    private AnAction invokeAction() {
        final String actionId = "testme.action.generators";
        final AnAction action = ActionManager.getInstance().getAction(actionId);
        assertNotNull("Can find registered action with id=" + actionId, action);
        action.actionPerformed(
                new AnActionEvent(
                        new MenuKeyEvent(getEditor().getComponent(), '\n',200,0,'\n','\n',null,null),
                        DataManager.getInstance().getDataContext(),
                        "",
                        action.getTemplatePresentation(),
                        ActionManager.getInstance(),
                        0
                )
        );
        return action;
    }
}
