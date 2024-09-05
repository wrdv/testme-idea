package com.weirddev.testme.intellij.utils;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.stream.Stream;

/**
 *
 * see https://stackoverflow.com/a/28057735
 */
public class ResourceLoader {
    public static Stream<Path> getDirResources(String path) throws URISyntaxException, IOException {
        Path myPath = getPath(path);
        return Files.walk(myPath, 1);
    }

    @NotNull
    public static Path getPath(String path) throws URISyntaxException, IOException {
        URI uri = ResourceLoader.class.getResource(path).toURI();
        Path myPath;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem;
            try {
                fileSystem = FileSystems.getFileSystem(uri);
            } catch (FileSystemNotFoundException ignore) {
                fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
            }
            myPath = fileSystem.getPath(path);
        } else {
            myPath = Paths.get(uri);
        }
        return myPath;
    }
}