package com.weirddev.testme.intellij.utils;

import com.intellij.ide.fileTemplates.CreateFromTemplateHandler;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ClassLoaderUtil;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.weirddev.testme.intellij.common.utils.LanguageUtils;
import com.weirddev.testme.intellij.groovy.resolvers.GroovyPropertyUtil;
import com.weirddev.testme.intellij.scala.resolvers.ScalaTypeUtils;
import com.weirddev.testme.intellij.template.FileTemplateContext;
import com.weirddev.testme.intellij.template.context.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * create test with template but not generate a real file to directory
 * see also FileTemplateUtil.createFromTemplate
 *
 * @author huangliang
 */
public class TestFileTemplateUtil {

    public static @NotNull PsiFile createFromTemplate(final @NotNull FileTemplate template, FileTemplateContext context,
        @Nullable Map<String, Object> propsMap, final @NotNull PsiDirectory directory,
        @Nullable ClassLoader classLoader) throws Exception {
        Project project = directory.getProject();
        String fileName = context.getTargetClass();
        Language language = context.getLanguage();
        FileTemplateManager.getInstance(project).addRecentName(template.getName());

        if (propsMap == null) {
            Properties p = FileTemplateManager.getInstance(project).getDefaultProperties();
            propsMap = new HashMap<>();
            FileTemplateUtil.putAll(propsMap, p);
        }

        Properties p = new Properties();
        FileTemplateUtil.fillDefaultProperties(p, directory);
        FileTemplateUtil.putAll(propsMap, p);

        final CreateFromTemplateHandler handler = FileTemplateUtil.findHandler(template);
        if (fileName != null && propsMap.get(FileTemplate.ATTRIBUTE_NAME) == null) {
            propsMap.put(FileTemplate.ATTRIBUTE_NAME, fileName);
        } else if (fileName == null && handler.isNameRequired()) {
            fileName = (String)propsMap.get(FileTemplate.ATTRIBUTE_NAME);
            if (fileName == null) {
                throw new Exception("File name must be specified");
            }
        }
        String fileNameWithExt =
            fileName + (StringUtil.isEmpty(template.getExtension()) ? "" : "." + template.getExtension());
        propsMap.put(FileTemplate.ATTRIBUTE_FILE_NAME, fileNameWithExt);
        propsMap.put(FileTemplate.ATTRIBUTE_FILE_PATH,
            FileUtil.join(directory.getVirtualFile().getPath(), fileNameWithExt));
        String dirPath = FileTemplateUtil.getDirPathRelativeToProjectBaseDir(directory);
        if (dirPath != null) {
            propsMap.put(FileTemplate.ATTRIBUTE_DIR_PATH, dirPath);
        }

        //Set escaped references to dummy values to remove leading "\" (if not already explicitly set)
        String[] dummyRefs =
            FileTemplateUtil.calculateAttributes(template.getText(), propsMap, true, directory.getProject());
        for (String dummyRef : dummyRefs) {
            propsMap.put(dummyRef, "");
        }

        handler.prepareProperties(propsMap, fileName, template, project);
        handler.prepareProperties(propsMap);

        Map<String, Object> props_ = propsMap;
        String mergedText = ClassLoaderUtil.computeWithClassLoader(
            classLoader != null ? classLoader : FileTemplateUtil.class.getClassLoader(),
            () -> template.getText(props_));
        String templateText = StringUtil.convertLineSeparators(mergedText);
        PsiFileFactory fileFactory = PsiFileFactory.getInstance(project);
        return fileFactory.createFileFromText(fileName, getLanguageFileType(context.getSrcClass().getLanguage()), templateText);
    }

    public static FileType getLanguageFileType(com.intellij.lang.Language language) {
        if (LanguageUtils.isScala(language)) {
            return ScalaTypeUtils.getScalaFileType();
        }

        if (LanguageUtils.isGroovy(language)) {
            return GroovyPropertyUtil.getGroovyFileType();
        }
        return JavaFileType.INSTANCE;
    }

}
