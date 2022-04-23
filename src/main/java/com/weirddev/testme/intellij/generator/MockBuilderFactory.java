package com.weirddev.testme.intellij.generator;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ResourceFileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.PsiManagerEx;
import com.weirddev.testme.intellij.template.FileTemplateContext;
import com.weirddev.testme.intellij.template.context.MockitoMockBuilder;
import com.weirddev.testme.intellij.template.context.StringUtils;
import com.weirddev.testme.intellij.template.context.TestSubjectInspector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MockBuilderFactory {
    private static final Logger logger = Logger.getInstance(MockBuilderFactory.class.getName());
    private static final String MOCKITO_CORE_JAR_NAME_PREFIX = "mockito-core-";
    private static final Pattern MOCKITO_CORE_VERSION_REGEX = Pattern.compile(MOCKITO_CORE_JAR_NAME_PREFIX + "(.*)\\.jar");

    public MockBuilderFactory() {
    }

    @NotNull
    public MockitoMockBuilder createMockitoMockBuilder(FileTemplateContext context, TestSubjectInspector testSubjectInspector, List<String> classpathJars) {
        boolean found = false;
//        final VirtualFile mockMakerVFile = ResourceFileUtil.findResourceFileInScope("mockito-extensions/org.mockito.plugins.MockMaker", context.getProject(), context.getTestModule().getModuleWithDependenciesAndLibrariesScope(true));
        final VirtualFile mockMakerVFile = ResourceFileUtil.findResourceFileInDependents(context.getTestModule(), "mockito-extensions/org.mockito.plugins.MockMaker");
        logger.debug("found mockito MockMaker in test module classpath:" + mockMakerVFile);
        if (mockMakerVFile != null) {
            final PsiFile mockMakerPsiFile = ((PsiManagerEx) PsiManager.getInstance(context.getProject())).getFileManager().getCachedPsiFile(mockMakerVFile);
            if (mockMakerPsiFile != null) {
                final String mockFileText = mockMakerPsiFile.getText();
                found = StringUtils.hasLine(mockFileText, "mock-maker-inline");
                logger.debug("mockito MockMaker content:" + mockFileText);
                logger.debug("is mock-maker-inline turned on:" + found);
            }
        }
       return new MockitoMockBuilder(found, context.getFileTemplateConfig().isStubMockMethodCallsReturnValues(), testSubjectInspector, resolveMockitoVersion(classpathJars));
    }

    @Nullable
    String resolveMockitoVersion(List<String> classpathJars) {
        return classpathJars == null ? null : classpathJars.stream()
                .map(f -> {
                    Matcher matcher = MOCKITO_CORE_VERSION_REGEX.matcher(f);
                    if (matcher.find()) {
                        return matcher.group(1);
                    } else {
                        return "";
                    }
                })
                .filter(f -> !f.isEmpty())
                .findFirst()
                .orElse(null);
    }
}