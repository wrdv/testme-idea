package com.weirddev.testme.intellij.generator;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.impl.scopes.ModuleWithDependenciesScope;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.lang.JavaVersion;
import com.weirddev.testme.intellij.builder.MethodReferencesBuilder;
import com.weirddev.testme.intellij.template.FileTemplateContext;
import com.weirddev.testme.intellij.template.TypeDictionary;
import com.weirddev.testme.intellij.template.context.*;
import com.weirddev.testme.intellij.template.context.impl.TestBuilderImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Date: 20/11/2016
 *
 * @author Yaron Yamin
 */
public class TestTemplateContextBuilder {
    private static final Logger logger = Logger.getInstance(TestTemplateContextBuilder.class.getName());
    private final MockBuilderFactory mockBuilderFactory;
    private final MethodReferencesBuilder methodReferencesBuilder;

    public TestTemplateContextBuilder(MockBuilderFactory mockBuilderFactory, MethodReferencesBuilder methodReferencesBuilder) {
        this.mockBuilderFactory = mockBuilderFactory;
        this.methodReferencesBuilder = methodReferencesBuilder;
    }

    public Map<String, Object> build(FileTemplateContext context, Properties defaultProperties) {
        final long start = new Date().getTime();
        HashMap<String, Object> ctxtParams = initTemplateContext(defaultProperties);
        populateDateFields(ctxtParams, Calendar.getInstance());
        ctxtParams.put(TestMeTemplateParams.CLASS_NAME, context.getTargetClass());
        ctxtParams.put("TAB", "    ");
        ctxtParams.put(TestMeTemplateParams.PACKAGE_NAME, context.getTargetPackage().getQualifiedName());
        int maxRecursionDepth = context.getFileTemplateConfig().getMaxRecursionDepth();
        ctxtParams.put(TestMeTemplateParams.MAX_RECURSION_DEPTH, maxRecursionDepth);
        ctxtParams.put(TestMeTemplateParams.StringUtils, new StringUtils());
        final TypeDictionary typeDictionary = TypeDictionary.create(context.getSrcClass(), context.getTargetPackage(),context.getFileTemplateConfig().isThrowSpecificExceptionTypes());
        JavaVersion javaVersion = getJavaVersion(context.getTestModule());
        ctxtParams.put(TestMeTemplateParams.JAVA_VERSION, javaVersion);
        ctxtParams.put(TestMeTemplateParams.TestBuilder, new TestBuilderImpl(context.getLanguage(), context.getSrcModule(), typeDictionary, context.getFileTemplateConfig(), javaVersion));
        final PsiClass targetClass = context.getSrcClass();
        if (targetClass != null && targetClass.isValid()) {
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS_LANGUAGE, targetClass.getLanguage().getID());
            PsiClassType psiClassType = Type.resolveType(targetClass);
            final Type type = typeDictionary.getType(psiClassType, maxRecursionDepth, true);
            typeDictionary.logStatistics();
            ctxtParams.put(TestMeTemplateParams.TESTED_CLASS, type);
            if (type != null) {
                methodReferencesBuilder.resolveMethodReferences(maxRecursionDepth, type.getMethods());
            }
        }
        final TestSubjectInspector testSubjectInspector =
            new TestSubjectInspector(context.getFileTemplateConfig().isGenerateTestsForInheritedMethods(),
                context.getFileTemplateCustomization());
        ctxtParams.put(TestMeTemplateParams.TestSubjectUtils, testSubjectInspector);
        List<String> classpathJars = resolveClasspathJars(context);
        ctxtParams.put(TestMeTemplateParams.MockitoMockBuilder, mockBuilderFactory.createMockitoMockBuilder(context, testSubjectInspector, classpathJars));
        ctxtParams.put(TestMeTemplateParams.PowerMockBuilder, mockBuilderFactory.createPowerMockBuilder(context, testSubjectInspector, classpathJars));
        ctxtParams.put(TestMeTemplateParams.TestedClasspathJars, classpathJars);
        logger.debug("Done building Test Template context in "+(new Date().getTime()-start)+" millis");
        return ctxtParams;
    }

    @NotNull
    private List<String> resolveClasspathJars(FileTemplateContext context) {
        GlobalSearchScope searchScope = context.getTestModule().getModuleWithDependenciesAndLibrariesScope(true);
        if (searchScope instanceof ModuleWithDependenciesScope) {
            ModuleWithDependenciesScope moduleWithDependenciesScope = (ModuleWithDependenciesScope) searchScope;
            return moduleWithDependenciesScope.getRoots().stream().map(VirtualFile::getName).filter(name -> name.endsWith(".jar")).collect(Collectors.toList());
        }
        else {
            return List.of();
        }
    }

    @Nullable
    private JavaVersion getJavaVersion(Module testModule) {
        ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(testModule);
        Sdk sdk = moduleRootManager.getSdk();
        if (sdk != null && sdk.getSdkType().getName().toLowerCase().contains("java")) {
            return JavaVersion.tryParse(sdk.getVersionString());
        }
        else {
            return null;
        }
    }

    void populateDateFields(Map<String, Object> ctxtParams, Calendar calendar) {
        ctxtParams.put(TestMeTemplateParams.MONTH_NAME_EN, new SimpleDateFormat("MMMM", Locale.ENGLISH).format(calendar.getTime()));
        ctxtParams.put(TestMeTemplateParams.DAY_NUMERIC, calendar.get(Calendar.DAY_OF_MONTH));
        ctxtParams.put(TestMeTemplateParams.HOUR_NUMERIC, calendar.get(Calendar.HOUR_OF_DAY));
        ctxtParams.put(TestMeTemplateParams.MINUTE_NUMERIC, calendar.get(Calendar.MINUTE));
        ctxtParams.put(TestMeTemplateParams.SECOND_NUMERIC, calendar.get(Calendar.SECOND));
    }

    @NotNull
    private HashMap<String, Object> initTemplateContext(Properties defaultProperties) {
        HashMap<String, Object> templateCtxtParams = new HashMap<String, Object>();
        for (Map.Entry<Object, Object> entry : defaultProperties.entrySet()) {
            templateCtxtParams.put((String) entry.getKey(), entry.getValue());
        }
        return templateCtxtParams;
    }


}
