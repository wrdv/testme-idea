package com.weirddev.testme.intellij;

import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.generator.TestMeResourceLoader;
import org.apache.velocity.runtime.RuntimeInstance;
import org.jetbrains.annotations.Nullable;

import java.util.Vector;

/**
 * for loading testMe included macros and templates. possibly after Velocity runtime has been initialized already
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
        super.setProperty("resource.loader." + TEST_ME_INCLUDES_DIR + ".instance", new TestMeResourceLoader());
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
