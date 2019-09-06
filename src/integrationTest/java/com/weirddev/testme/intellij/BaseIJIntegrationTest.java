package com.weirddev.testme.intellij;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Date: 19/12/2016
 *
 * @author Yaron Yamin
 */
abstract public class BaseIJIntegrationTest extends LightJavaCodeInsightFixtureTestCase {
    private static final String FILE_HEADER_TEMPLATE = "File Header.java";
    private static final String HEADER_TEMPLATE_REPLACEMENT_TEXT = "/** created by TestMe integration test on MMXVI */\n";
    private static boolean isHeaderTemplateReplaced=false;
    private String testDataRoot;

    public BaseIJIntegrationTest(String testDataRoot) {
        this.testDataRoot = testDataRoot;
        System.setProperty("IN_TEST_MODE", "Y");
    }

    @Override
    public void setUp() throws Exception {
        System.out.println("TestDataPath:"+getTestDataPath());
        assertTrue(new File(getTestDataPath()).exists());
        super.setUp();
        System.out.println("temp dir path:"+myFixture.getTempDirPath());
        replacePatternTemplateText(FILE_HEADER_TEMPLATE, HEADER_TEMPLATE_REPLACEMENT_TEXT);
    }

    private void replacePatternTemplateText(String templateName, String templateText) {
        if(isHeaderTemplateReplaced){
            return;
        }
        FileTemplateManager fileTemplateManager = FileTemplateManager.getInstance(getProject());
        FileTemplate headerTemplate = fileTemplateManager.getPattern(templateName);
        System.out.println("headerTemplate:"+headerTemplate);
        System.out.println("Existing header Template text:\n"+headerTemplate.getText());
        System.out.println("Replacing header Template text with:\n"+ templateText);
        headerTemplate.setText(templateText);
        isHeaderTemplateReplaced = true;
    }

    @Override
    protected String getTestDataPath() {
        return testDataRoot +getTestName(true).replace('$', '/');
    }

    @NotNull
    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return new DefaultLightProjectDescriptor() {
            @Override
            public Sdk getSdk() {
                return JavaSdk.getInstance().createJdk("java 1.8", new File(System.getProperty("java.home")).getParent(), false);
            }

            @Override
            public void configureModule(Module module, ModifiableRootModel model, ContentEntry contentEntry) {
                super.configureModule(module, model, contentEntry);
//                VirtualFile dummyRoot = VirtualFileManager.getInstance().findFileByUrl("temp:///");
//                assert dummyRoot != null;
//                dummyRoot.refresh(false, false);
//                try {
//                    dummyRoot.createChildDirectory(this, "resources");
//                    dummyRoot.createChildDirectory(this, "test");
//                    dummyRoot.refresh(false, true);
//                }
//                catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                VirtualFile resRoot = VirtualFileManager.getInstance().refreshAndFindFileByUrl("temp:///resources");
//                VirtualFile testRoot = VirtualFileManager.getInstance().refreshAndFindFileByUrl("temp:///test");
//                model.addContentEntry(resRoot).addSourceFolder("temp:///resources", JavaResourceRootType.RESOURCE);
//                model.addContentEntry(testRoot).addSourceFolder("temp:///test", JavaSourceRootType.TEST_SOURCE);
            }
        };
    }
}
