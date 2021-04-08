package com.weirddev.testme.intellij.utils;

import com.intellij.openapi.diagnostic.LoggerRt;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author : Yaron Yamin
 * @since : 4/9/21
 **/
public class UrlClassLoaderUtils {
    /**
     * Interns a value of the {@link URL#protocol} ("file" or "jar") and {@link URL#host} (empty string) fields.
     * @see com.intellij.util.lang.UrlClassLoader#internProtocol(java.net.URL)
     */
    public static URL internProtocol(@NotNull URL url) {
        String protocol = url.getProtocol();
        boolean interned = false;
        if ("file".equals(protocol) || "jar".equals(protocol)) {
            protocol = protocol.intern();
            interned = true;
        }
        String host = url.getHost();
        if (host != null && host.isEmpty()) {
            host = "";
            interned = true;
        }
        try {
            if (interned) {
                url = new URL(protocol, host, url.getPort(), url.getFile());
            }
            return url;
        }
        catch (MalformedURLException e) {
            LoggerRt.getInstance(com.intellij.util.lang.UrlClassLoader.class).error(e);
            return null;
        }
    }
}
