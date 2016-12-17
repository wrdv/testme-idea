package com.weirddev.testme;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;

/**
 * Date: 17/12/2016
 *
 * @author Yaron Yamin
 */
public class TestMeBundle {
    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    private static Reference<ResourceBundle> bundle;
    @NonNls
    private static final String BUNDLE = "messages.TestMeBundle";

    private TestMeBundle() {
    }

    private static ResourceBundle getBundle() {
        ResourceBundle bundle = com.intellij.reference.SoftReference.dereference(TestMeBundle.bundle);
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BUNDLE);
            TestMeBundle.bundle = new SoftReference<ResourceBundle>(bundle);
        }
        return bundle;
    }
}
