package com.weirddev.testme.intellij.template;

import com.intellij.ide.fileTemplates.impl.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.lang.UrlClassLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    public class FTFolderManager {
        private static final Logger LOG = Logger.getInstance(FTFolderManager.class.getName());
        static final String TEMPLATE_EXTENSION_SUFFIX = ".ft";
        final String fileTemplatesInternalIncludesDir;
        final Map<String, FileTemplateBase> myTemplates = new HashMap<String, FileTemplateBase>();

        public FTFolderManager(String fileTemplatesDir) {
            this.fileTemplatesInternalIncludesDir = fileTemplatesDir;
            try {
                loadDefaultsFromRoot();
            } catch (IOException e) {
                LOG.error("Can't load TestMe macros",e);
            }
        }

        /**
     * @see FTManager#addDefaultTemplate(DefaultTemplate)
     */
    private void addDefaultTemplate(DefaultTemplate template) {
        createAndStoreBundledTemplate(template);
    }

    /**
     * @see FTManager#createAndStoreBundledTemplate(DefaultTemplate)
     */
    private BundledFileTemplate createAndStoreBundledTemplate(DefaultTemplate template) {
        final BundledFileTemplate bundled = new BundledFileTemplate(template, true);
        final String qName = bundled.getQualifiedName();
        final FileTemplateBase previous = getTemplates().put(qName, bundled);
//        LOG.assertTrue(previous == null, "Duplicate bundled template " + qName + " [" + template.getTemplateURL() + ", " + previous + ']');
        return bundled;
    }

    /**
     * @see FileTemplatesLoader#loadDefaultsFromRoot(URL)
     */
    private void loadDefaultsFromRoot() throws IOException {
        final URL root = getClass().getClassLoader().getResource(fileTemplatesInternalIncludesDir);
        if (root == null) {
            return;
        }
        final List<String> children = UrlUtil.getChildrenRelativePaths(root);
        if (children.isEmpty()) {
            return;
        }
        for (final String path : children) {
            if (path.endsWith(TEMPLATE_EXTENSION_SUFFIX)) {
                final String filename = path.substring(0, path.length() - TEMPLATE_EXTENSION_SUFFIX.length());
                final String extension = getExtension(filename);
                final String templateName = filename.substring(0, filename.length() - extension.length() - 1);
                final String rootDir = root.toExternalForm();
                final URL templateUrl = UrlClassLoader.internProtocol(new URL((rootDir.endsWith("/") ? rootDir : (rootDir + "/")) + path));
                assert templateUrl != null;
                addDefaultTemplate(new DefaultTemplate(templateName, extension, templateUrl, null));
            }
        }
    }

    @NotNull
    private String getExtension(@NotNull String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index < 0) return "";
        return fileName.substring(index + 1);
    }

    public Map<String, FileTemplateBase> getTemplates() {
        return myTemplates;
    }
}