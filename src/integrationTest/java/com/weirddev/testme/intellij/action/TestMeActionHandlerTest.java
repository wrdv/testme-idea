package com.weirddev.testme.intellij.action;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.psi.PsiClass;
import com.weirddev.testme.intellij.generator.TestMeGeneratorTestBase;
import com.weirddev.testme.intellij.template.TemplateDescriptor;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.ui.TestMePopUpHandler;
import org.junit.Assert;

import java.util.List;

/**
 * Date: 03/05/2017
 *
 * @author Yaron Yamin
 */
public class TestMeActionHandlerTest extends TestMeGeneratorTestBase {

    public static final int TOTAL_TEMPLATES = 4;

    public TestMeActionHandlerTest() {
        super(TemplateRegistry.JUNIT4_MOCKITO_JAVA_TEMPLATE, "test");
    }

    public void testNestedClassParams() throws Exception {
        doTest();
    }

    @Override
    protected void doTest(final String packageName, final String testSubjectClassName, final String expectedTestClassName, final boolean reformatCode, final boolean optimizeImports, final boolean replaceFqn) {
        final PsiClass fooClass = setupSourceFiles(packageName, testSubjectClassName);
        CommandProcessor.getInstance().executeCommand(getProject(), new Runnable() {
            @Override
            public void run() {
                myFixture.openFileInEditor(fooClass.getContainingFile().getVirtualFile());
                final TestMeActionHandler actionHandler = new TestMeActionHandler();
                final String chooserTitle = actionHandler.getChooserTitle(getEditor(), getFile(), fooClass);
                Assert.assertEquals("<html><body>Test Class <b>Foo</b></body></html>",chooserTitle);
                final TestMePopUpHandler.GotoData sourceAndTargetElements = actionHandler.getSourceAndTargetElements(getEditor(), getFile());
                Assert.assertNotNull(sourceAndTargetElements);
                final List<TemplateDescriptor> templateDescriptors = new TemplateRegistry().getTemplateDescriptors();
                Assert.assertEquals(countEnabledTemplates(templateDescriptors),sourceAndTargetElements.additionalActions.size());
                final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
                propertiesComponent.setValue("create.test.in.the.same.root",String.valueOf(true));
                Assert.assertEquals("<html>with <i>JUnit4</i></html><JUnit4><html>& <i>Mockito</i></html><Mockito>",sourceAndTargetElements.additionalActions.get(0).getText());
                sourceAndTargetElements.additionalActions.get(0).execute();
                verifyGeneratedTest(packageName, expectedTestClassName);
            }
        }, CodeInsightBundle.message("intention.create.test"), this);
    }

    private int countEnabledTemplates(List<TemplateDescriptor> templateDescriptors) {
        int nEnabledTemplates=0;
        for (TemplateDescriptor templateDescriptor : templateDescriptors) {
            if(templateDescriptor.isEnabled()) nEnabledTemplates++;
        }
        return nEnabledTemplates;
    }

}
