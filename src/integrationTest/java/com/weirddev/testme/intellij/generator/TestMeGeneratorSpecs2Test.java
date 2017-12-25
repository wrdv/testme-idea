package com.weirddev.testme.intellij.generator;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.IdeaTestUtil;
import com.intellij.testFramework.LightProjectDescriptor;
import com.weirddev.testme.intellij.template.TemplateRegistry;
import com.weirddev.testme.intellij.template.context.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 24/12/2017
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorSpecs2Test extends TestMeGeneratorTestBase   {
    public static final String SCALA_LIB_SUB_PATH = "org.scala-lang/scala-library/2.10.6/";
    public static final String SCALA_LIB_NAME = "scala-library-2.10.6";

    public TestMeGeneratorSpecs2Test() {
        super(TemplateRegistry.SPECS2_MOCKITO_SCALA_TEMPLATE, "testSpecs2", Language.Scala);
        expectedTestClassExtension = "scala";
        skipTestIfScalaPluginDisabled();
    }

    public void testBasicScala() throws Exception{
        doTest(true,true,true);  //todo add method that returns a tuple for assertion
    }


    /**
     * @link https://github.com/JetBrains/intellij-scala/blob/9198f8fb5b93d2ff142a65e320b812fc92cc0830/scala/scala-impl/test/org/jetbrains/plugins/scala/LightScalaTestCase.scala
     */
    @NotNull
    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return new LightProjectDescriptor(){
            @NotNull
            @Override
            public ModuleType getModuleType() {
                return StdModuleTypes.JAVA;
            }
            @Nullable
            @Override
            public Sdk getSdk() {
                return IdeaTestUtil.getMockJdk14();
            }

            @Override
            protected void configureModule(@NotNull Module module, @NotNull ModifiableRootModel model, @NotNull ContentEntry contentEntry) {
                Library.ModifiableModel modifiableModel = null;
                try {
                    modifiableModel = model.getModuleLibraryTable().createLibrary("SCALA").getModifiableModel();
                    String scalaLib = getScalaLibraryJarPath() + "!/";
                    VirtualFile scalaJar = JarFileSystem.getInstance().refreshAndFindFileByPath(scalaLib);
                    assert scalaJar != null : "library not found: " + scalaLib;
                    modifiableModel.addRoot(scalaJar, OrderRootType.CLASSES);
                    File srcRoot = new File(getScalaLibrarySrc());
                    modifiableModel.addRoot(VfsUtil.getUrlForLibraryRoot(srcRoot), OrderRootType.SOURCES);
                } catch (Exception e) {
                    System.err.println("error configuring scala library for test module");
                    e.printStackTrace();
                }
                finally {
                    if (modifiableModel != null) {
                        modifiableModel.commit();
                    }
                }
            }
        };
    }
    private String getScalaLibrarySrc() {
        final String sourceFilepath = findGradleCachedFile(SCALA_LIB_NAME + "-sources.jar");
        System.out.println("scala source file path:"+sourceFilepath);
        return sourceFilepath;
    }

    private static String findGradleCachedFile(String scalaLibFilename) {
        final String scalaLibPath = getGradleCachePath() + SCALA_LIB_SUB_PATH;
        final File scalaLibDir = new File(scalaLibPath);
        assert scalaLibDir.exists() : "scala lib dir in gradle cache not found "+scalaLibPath;
        final List<String> results = searchDirectory(scalaLibDir, scalaLibFilename);
        assert results.size()>0 : "scala source dir not found "+scalaLibPath;
        return results.get(0);
    }

    private static String getGradleCachePath() {
        String userHomePath = System.getProperty("user.home");
        String homeEnvPath = System.getenv("HOME");
        String result = (userHomePath != null ? userHomePath : homeEnvPath)+"/.gradle/caches/modules-2/files-2.1/";
        return result.replace("\\", "/");
    }

    private static String getScalaLibraryJarPath() {
        final String scalaLibPath = findGradleCachedFile(SCALA_LIB_NAME + ".jar");
        System.out.println("scalaLibPath:"+scalaLibPath);
        return scalaLibPath;
    }

    private static List<String> searchDirectory(File directory, String fileNameToSearch) {
        List<String> result = new ArrayList<>();
        if (directory.isDirectory()) {
            search(directory,fileNameToSearch,result);
        } else {
            System.out.println(directory.getAbsoluteFile() + " is not a directory!");
        }
        return result;
    }

    private static void search(File file, String fileNameToSearch, List<String> result) {

        if (file.isDirectory()) {
            System.out.println("Searching directory ... " + file.getAbsoluteFile());
            if (file.canRead()) {
                final File[] files = file.listFiles();
                if (files != null) {
                    for (File temp : files) {
                        if (temp.isDirectory()) {
                            search(temp, fileNameToSearch, result);
                        } else {
                            if (fileNameToSearch.equals(temp.getName().toLowerCase())) {
                                result.add(temp.getAbsoluteFile().toString());
                            }
                        }
                    }
                }
            } else {
                System.out.println(file.getAbsoluteFile() + "Permission Denied");
            }
        }
    }
}
