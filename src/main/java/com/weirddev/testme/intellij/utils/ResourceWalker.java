package com.weirddev.testme.intellij.utils;


import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 *
 * @link https://stackoverflow.com/a/28057735
 */
public class ResourceWalker {
    public static Stream<Path> getDirResources(String path) throws URISyntaxException, IOException {
        URI uri = ResourceWalker.class.getResource(path).toURI();
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
        return Files.walk(myPath, 1);
    }
}