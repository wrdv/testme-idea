package com.weirddev.testme.intellij.generator;

import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.utils.AccessLevelReflectionUtils;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.resource.ResourceManager;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

/**
 * relevant for IDEA 2024.1+ support
 * very hackish, but re-uses existing test templates support
 */
public class VelocityInitializer {
    private static final Logger LOG = Logger.getInstance(VelocityInitializer.class);
    public static void verifyRuntimeSetup() {
        try {
            RuntimeInstance runtimeInstance = getRuntimeInstance();
            if (runtimeInstance == null) {
                return;
            }
            ResourceManager resourceManager = (ResourceManager) AccessLevelReflectionUtils.getField(runtimeInstance.getClass().getDeclaredField("resourceManager"), runtimeInstance);
            List<ResourceLoader> resourceLoaders = (List<ResourceLoader>) AccessLevelReflectionUtils.getField(resourceManager.getClass().getDeclaredField("resourceLoaders"), resourceManager);//ResourceManagerImpl
            if (resourceLoaders.stream().noneMatch(loader -> loader instanceof TestMeResourceLoader)) {
                resourceLoaders.add(new TestMeResourceLoader());
            }

        } catch (Throwable e) {
            LOG.warn("unable to set resource loader: "+ e.getMessage());
        }
    }
    private static @Nullable RuntimeInstance getRuntimeInstance() {
        try {
            Class<?> vwClass = ClassLoader.getSystemClassLoader().loadClass("com.intellij.ide.fileTemplates.VelocityWrapper");
            Field ri = vwClass.getDeclaredField("ri");
            return (RuntimeInstance) AccessLevelReflectionUtils.getField(ri,null);
        } catch (Throwable e) {
            LOG.info("RI not found on velocity runtime:" + e.getMessage());
            return null;
        }
    }
}
