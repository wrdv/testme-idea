package com.weirddev.testme.intellij.template;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.impl.*;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.util.lang.UrlClassLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 2/12/2017
 *
 * @author Yaron
 */
@State(
        name = "TestMeFileTemplateManager",
        storages = {
                @Storage(file = StoragePathMacros.WORKSPACE_FILE)
        }
)
public class TestMeFileTemplateManager extends FileTemplateManagerImpl {
    private static final Logger LOG = Logger.getInstance("#" + TestMeFileTemplateManager.class.getName());
    private static final String TEMPLATE_EXTENSION_SUFFIX = ".ft";
    private static final String FILE_TEMPLATES_INTERNAL_INCLUDES_DIR = "fileTemplates/internalIncludes";
//    private final List<DefaultTemplate> myDefaultTemplates = new ArrayList<DefaultTemplate>();

    private final Map<String, FileTemplateBase> myTemplates = new HashMap<String, FileTemplateBase>();

    public TestMeFileTemplateManager(@NotNull FileTypeManagerEx typeManager, ProjectManager pm, Project project) {
        super(typeManager, pm, project);
        try {
            loadDefaultsFromRoot(getClass().getClassLoader().getResource(FILE_TEMPLATES_INTERNAL_INCLUDES_DIR));
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    /**
     * @see FTManager#addDefaultTemplate(com.intellij.ide.fileTemplates.impl.DefaultTemplate)
     */
    private void addDefaultTemplate(DefaultTemplate template) {
//        myDefaultTemplates.add(template);
        createAndStoreBundledTemplate(template);
    }

    /**
     * @see FTManager#createAndStoreBundledTemplate(com.intellij.ide.fileTemplates.impl.DefaultTemplate)
     */
    private BundledFileTemplate createAndStoreBundledTemplate(DefaultTemplate template) {
        final BundledFileTemplate bundled = new BundledFileTemplate(template, true);
        final String qName = bundled.getQualifiedName();
        final FileTemplateBase previous = getTemplates().put(qName, bundled);
//        LOG.assertTrue(previous == null, "Duplicate bundled template " + qName + " [" + template.getTemplateURL() + ", " + previous + ']');
        return bundled;
    }

    /**
     * @see FileTemplatesLoader#loadDefaultsFromRoot(java.net.URL)
     */
    private void loadDefaultsFromRoot(final URL root) throws IOException {
        final List<String> children = UrlUtil.getChildrenRelativePaths(root);
        if (children.isEmpty()) {
            return;
        }
        for (final String path : children) {
            if (path.endsWith(TEMPLATE_EXTENSION_SUFFIX)) {
                final String filename = path.substring(0, path.length() - TEMPLATE_EXTENSION_SUFFIX.length());
                final String extension = getExtension(filename);
                final String templateName = filename.substring(0, filename.length() - extension.length() - 1);
                final URL templateUrl = UrlClassLoader.internProtocol(new URL(root.toExternalForm() + "/" + path));
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

    @Override
    public FileTemplate getPattern(@NotNull String name) {
        final FileTemplate pattern = super.getPattern(name);
        if (pattern != null) {
            return pattern;
        } else {
            return getTemplates().get(name);
        }
    }

    private Map<String, FileTemplateBase> getTemplates() {
        return myTemplates;
    }
}
