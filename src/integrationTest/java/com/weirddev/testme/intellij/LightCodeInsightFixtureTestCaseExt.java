package com.weirddev.testme.intellij;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.LanguageLevelModuleExtension;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.*;
import com.intellij.testFramework.*;
import com.intellij.testFramework.fixtures.*;
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Date: 10/26/2016
 * @author Yaron Yamin
 * @see LightCodeInsightFixtureTestCase
 */
public class LightCodeInsightFixtureTestCaseExt extends UsefulTestCase {
    public static final LightProjectDescriptor JAVA_1_4 = new DefaultLightProjectDescriptor() {
        @Override
        public void configureModule(Module module, ModifiableRootModel model, ContentEntry contentEntry) {
            model.getModuleExtension(LanguageLevelModuleExtension.class).setLanguageLevel(LanguageLevel.JDK_1_4);
        }
    };
    public static final LightProjectDescriptor JAVA_1_5 = new DefaultLightProjectDescriptor() {
        @Override
        public void configureModule(Module module, ModifiableRootModel model, ContentEntry contentEntry) {
            model.getModuleExtension(LanguageLevelModuleExtension.class).setLanguageLevel(LanguageLevel.JDK_1_5);
        }
    };
    public static final LightProjectDescriptor JAVA_1_6 = new DefaultLightProjectDescriptor() {
        @Override
        public void configureModule(Module module, ModifiableRootModel model, ContentEntry contentEntry) {
            model.getModuleExtension(LanguageLevelModuleExtension.class).setLanguageLevel(LanguageLevel.JDK_1_6);
        }
    };
    public static final LightProjectDescriptor JAVA_1_7 = new DefaultLightProjectDescriptor() {
        @Override
        public void configureModule(Module module, ModifiableRootModel model, ContentEntry contentEntry) {
            model.getModuleExtension(LanguageLevelModuleExtension.class).setLanguageLevel(LanguageLevel.JDK_1_7);
        }
    };
    public static final LightProjectDescriptor JAVA_8 = new DefaultLightProjectDescriptor() {
        @Override
        public Sdk getSdk() {
            return IdeaTestUtil.getMockJdk18();
        }

        @Override
        public void configureModule(Module module, ModifiableRootModel model, ContentEntry contentEntry) {
            model.getModuleExtension(LanguageLevelModuleExtension.class).setLanguageLevel(LanguageLevel.JDK_1_8);
        }
    };
    public static final LightProjectDescriptor JAVA_LATEST = new DefaultLightProjectDescriptor();


    protected JavaCodeInsightTestFixture myFixture;
    protected Module myModule;

    @SuppressWarnings("JUnitTestCaseWithNonTrivialConstructors")
    protected LightCodeInsightFixtureTestCaseExt() {
        IdeaTestCase.initPlatformPrefix();
    }

    @SuppressWarnings("JUnitTestCaseWithNonTrivialConstructors")
    public LightCodeInsightFixtureTestCaseExt(String classToTest, String prefix) {
        PlatformTestCase.initPlatformPrefix(classToTest, prefix);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        IdeaTestFixtureFactory factory = IdeaTestFixtureFactory.getFixtureFactory();
        TestFixtureBuilder<IdeaProjectTestFixture> fixtureBuilder = factory.createLightFixtureBuilder(getProjectDescriptor());
        final IdeaProjectTestFixture fixture = fixtureBuilder.getFixture();
        myFixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(fixture, getTempDirFixture());

        myFixture.setUp();
        myFixture.setTestDataPath(getTestDataPath());

        myModule = myFixture.getModule();
    }

    /**
     *  override to control temp file creation strategy. by default temp project files are created in-memory.
     *
     * @return TempDirTestFixture to be used for this test fixure
     */
    @NotNull
    protected TempDirTestFixture getTempDirFixture() {
        return new LightTempDirTestFixtureImpl(true);
    }

    /**
     * Return relative path to the test data.
     *
     * @return relative path to the test data.
     */
    @NonNls
    protected String getBasePath() {
        return "";
    }

    @NotNull
    protected LightProjectDescriptor getProjectDescriptor() {
        return JAVA_LATEST;
    }


    /**
     * Return absolute path to the test data. Not intended to be overridden.
     *
     * @return absolute path to the test data.
     */
    @NonNls
    protected String getTestDataPath() {
        String communityPath = PlatformTestUtil.getCommunityPath().replace(File.separatorChar, '/');
        String path = communityPath + getBasePath();
        if (new File(path).exists()) return path;
        return communityPath + "/../" + getBasePath();
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            myFixture.tearDown();
        }
        finally {
            myFixture = null;
            myModule = null;

            super.tearDown();
        }
    }

    protected final void runTestBare() throws Throwable {
        LightCodeInsightFixtureTestCaseExt.super.runTest();
    }

    protected Project getProject() {
        return myFixture.getProject();
    }

    protected PsiFile getFile() { return myFixture.getFile(); }

    protected Editor getEditor() { return myFixture.getEditor(); }

    protected PsiManager getPsiManager() {
        return PsiManager.getInstance(getProject());
    }

    public PsiElementFactory getElementFactory() {
        return JavaPsiFacade.getInstance(getProject()).getElementFactory();
    }

    protected PsiFile createLightFile(final FileType fileType, final String text) {
        return PsiFileFactory.getInstance(getProject()).createFileFromText("a." + fileType.getDefaultExtension(), fileType, text);
    }

    public PsiFile createLightFile(final String fileName, final Language language, final String text) {
        return PsiFileFactory.getInstance(getProject()).createFileFromText(fileName, language, text, false, true);
    }

}
