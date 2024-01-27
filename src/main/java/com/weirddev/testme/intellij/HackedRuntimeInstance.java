package com.weirddev.testme.intellij;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.ui.template.TestMeTemplateManager;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.util.ExtProperties;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Stream;

/**
 * Motivation: use a hidden folder where included velocity templates can be referenced. the default IJ includes folder can be overridden by the user. overriding the included macros will break plugin functionality on upgrades
 * Implementation: an ugly hack on velocity inner implementation.
 * Reasoning: Velocity is statically initialized, globally, in IJ IDEA. Extending IJ Classes that initialize Velocity is tricky since their API keeps changing between versions 14 - 2016
 * Date: 2/13/2017
 * @author Yaron Yamin
 */

public class HackedRuntimeInstance extends RuntimeInstance {
    private static final Logger LOG = Logger.getInstance(HackedRuntimeInstance.class.getName());
    private static final String TEST_ME_INCLUDES_DIR = "testMeIncludes";
    private static final String DEPRECATED_RESOURCE_LOADER_KEY = "resource.loader";
    private static final String RESOURCE_LOADERS_KEY = "resource.loaders";

    public HackedRuntimeInstance(RuntimeInstance existingRi) {
        @Nullable Object resourceLoaderProperty = null;
        if (existingRi != null) {
            setConfiguration(existingRi.getConfiguration());
            resourceLoaderProperty = updateResourceLoader(existingRi, RESOURCE_LOADERS_KEY);
            if (resourceLoaderProperty == null) {
                resourceLoaderProperty = updateResourceLoader(existingRi, DEPRECATED_RESOURCE_LOADER_KEY);
            }
        }
        if(resourceLoaderProperty == null) {
            super.setProperty(RESOURCE_LOADERS_KEY, TEST_ME_INCLUDES_DIR);
        }
        super.setProperty("resource.loader." + TEST_ME_INCLUDES_DIR + ".instance", new ResourceLoader() {
            @Override
            public void init(ExtProperties extProperties) {
            }

//            @Override
//            public InputStream getResourceStream(String resourceName) throws ResourceNotFoundException {
//                TestMeTemplateManager fileTemplateManager = TestMeTemplateManager.getDefaultInstance();
//                FileTemplate[] allPatterns = fileTemplateManager.getAllPatterns();
//                Optional<FileTemplate> optTemplate = Stream.of(allPatterns).filter(t -> resourceName.equals(t.getName() + "." + t.getExtension())).findAny();
//                final FileTemplate include = optTemplate.orElseThrow(() -> new ResourceNotFoundException("Template not found: " + resourceName));
//                final String text = include.getText();
//                try {
//                    return new ByteArrayInputStream(text.getBytes(FileTemplate.ourEncoding));
//                } catch (UnsupportedEncodingException e) {
//                    throw new RuntimeException(e);
//                }
//            }

            @Override
            public Reader getResourceReader(String source, String encoding) throws ResourceNotFoundException {
                TestMeTemplateManager fileTemplateManager = TestMeTemplateManager.getDefaultInstance();
                FileTemplate[] allPatterns = fileTemplateManager.getAllPatterns();
                Optional<FileTemplate> optTemplate = Stream.of(allPatterns).filter(t -> source.equals(t.getName() + "." + t.getExtension())).findAny();
                final FileTemplate include = optTemplate.orElseThrow(() -> new ResourceNotFoundException("Template not found: " + source));
                final String text = include.getText();
                return new StringReader(text);
            }

            @Override
            public boolean isSourceModified(Resource resource) {
                return true;
            }

            @Override
            public long getLastModified(Resource resource) {
                return 0L;
            }
        });
    }

    private Object updateResourceLoader(RuntimeInstance existingRi, String resourceLoaderKey) {
        @Nullable Object resourceLoaderProperty;
        resourceLoaderProperty = existingRi.getProperty(resourceLoaderKey);
        if (resourceLoaderProperty instanceof String && !((String) resourceLoaderProperty).contains(TEST_ME_INCLUDES_DIR)) {
            super.setProperty(resourceLoaderKey, resourceLoaderProperty + "," + TEST_ME_INCLUDES_DIR);
        }
        else if (resourceLoaderProperty instanceof Vector && !((Vector) resourceLoaderProperty).contains(TEST_ME_INCLUDES_DIR)) {
            ((Vector) resourceLoaderProperty).add(TEST_ME_INCLUDES_DIR);
        }
        return resourceLoaderProperty;
    }

    @Override
    public void setProperty(String key, Object value) {
        super.setProperty(key, resolveValue(key, value));
    }

    private Object resolveValue(String key, Object value) {
        if ((DEPRECATED_RESOURCE_LOADER_KEY.equals(key) || RESOURCE_LOADERS_KEY.equals(key)) && value instanceof String && !((String) value).contains(TEST_ME_INCLUDES_DIR)) {
            LOG.debug("adding TestMe includes dir to Velocity loader");
            return value + "," + TEST_ME_INCLUDES_DIR;
        }
        return value;
    }
}
