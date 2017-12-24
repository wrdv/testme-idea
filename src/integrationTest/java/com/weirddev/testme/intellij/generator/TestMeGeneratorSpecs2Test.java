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

/**
 * Date: 24/12/2017
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorSpecs2Test extends TestMeGeneratorTestBase   {

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
                final Library.ModifiableModel modifiableModel = model.getModuleLibraryTable().createLibrary("SCALA").getModifiableModel();
                String scalaLib = getScalaLibraryPath() + "!/";
                VirtualFile scalaJar = JarFileSystem.getInstance().refreshAndFindFileByPath(scalaLib);
                modifiableModel.addRoot(scalaJar, OrderRootType.CLASSES);
                File srcRoot = new File(getScalaLibrarySrc());
                modifiableModel.addRoot(VfsUtil.getUrlForLibraryRoot(srcRoot), OrderRootType.SOURCES);
                // do not forget to commit a model!
                modifiableModel.commit();
            }
        };
    }
    private static String getScalaLibrarySrc() {
        return getIvyCachePath() + "/org.scala-lang/scala-library/srcs/scala-library-2.10.6-sources.jar";
    }

    private static String getIvyCachePath() {
        String homePath = System.getProperty("user.home") + "/.ivy2/cache";
        String ivyCachePath = System.getProperty("sbt.ivy.home");
        String result = ivyCachePath != null ? ivyCachePath + "/cache" : homePath;
        return result.replace("\\", "/");
    }

    private static String getScalaLibraryPath() {
        final String scalaLibPath = getIvyCachePath() + "/org.scala-lang/scala-library/jars/scala-library-2.10.6.jar";
        System.out.println("scalaLibPath:"+scalaLibPath);
        return scalaLibPath;
    }
}
