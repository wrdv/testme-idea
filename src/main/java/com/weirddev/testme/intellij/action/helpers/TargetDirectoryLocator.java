package com.weirddev.testme.intellij.action.helpers;

import com.intellij.CommonBundle;
import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.JavaProjectRootsUtil;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.SourceFolder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import com.intellij.refactoring.PackageWrapper;
import com.intellij.refactoring.move.moveClassesOrPackages.MoveClassesOrPackagesUtil;
import com.intellij.refactoring.util.RefactoringUtil;
import com.intellij.testIntegration.createTest.CreateTestDialog;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.containers.ContainerUtil;
import com.weirddev.testme.intellij.action.CreateTestMeAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;
import org.jetbrains.jps.model.java.JavaResourceRootProperties;
import org.jetbrains.jps.model.java.JavaSourceRootProperties;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 10/18/2016
 *
 * @author Yaron Yamin
 * @see CreateTestDialog
 */

public class TargetDirectoryLocator{
//    private static final String RECENTS_KEY = "TargetDirectoryLocator.RecentsKey";
//    private static final String RECENT_SUPERS_KEY = "TargetDirectoryLocator.Recents.Supers";

    public PsiDirectory getOrCreateDirectory(@NotNull Project project, PsiPackage targetPackage, Module targetModule) {
        String packageQualifiedName = targetPackage.getQualifiedName();
//        RecentsManager.getInstance(project).registerRecentEntry(RECENTS_KEY, packageQualifiedName);
//        RecentsManager.getInstance(project).registerRecentEntry(RECENT_SUPERS_KEY, "" /*mySuperClassField.getText()*/ );
        PsiDirectory myTargetDirectory = null;
        try {
            myTargetDirectory = selectTargetDirectory(packageQualifiedName, project, targetModule);
            if (myTargetDirectory == null) return null;
        } catch (IncorrectOperationException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null) {
                Messages.showMessageDialog(project, errorMessage, CommonBundle.getErrorTitle(), Messages.getErrorIcon());
            }
        }
        return myTargetDirectory;
    }

    @Nullable
    private PsiDirectory selectTargetDirectory(final String packageName, final Project myProject, final Module myTargetModule) throws IncorrectOperationException {
        final PackageWrapper targetPackage = new PackageWrapper(PsiManager.getInstance(myProject), packageName);
        final VirtualFile selectedRoot = new ReadAction<VirtualFile>() {
            protected void run(Result<? super VirtualFile> result) throws Throwable {
//                final HashSet<VirtualFile> testFolders = new HashSet<VirtualFile>();
//                CreateTestMeAction.checkForTestRoots(myTargetModule, testFolders);
                final List<VirtualFile> testFolders = CreateTestMeAction.computeTestRoots(myTargetModule); // replaces above from v14
                List<VirtualFile> roots;
                if (testFolders==null || testFolders.isEmpty()) {
                    roots = new ArrayList<VirtualFile>();
                    List<String> urls = CreateTestMeAction.computeSuitableTestRootUrls(myTargetModule);
                    for (String url : urls) {
                        ContainerUtil.addIfNotNull(roots, VfsUtil.createDirectories(VfsUtilCore.urlToPath(url)));
                    }
                    //  roots = ModuleRootManager.getInstance(myTargetModule).getSourceRoots(JavaModuleSourceRootTypes.SOURCES); // from v14
                    if (roots.isEmpty()) {
                        collectSuitableDestinationSourceRoots(myTargetModule, roots);
                    }
                    if (roots.isEmpty()) return;
                } else {
                    roots = new ArrayList<VirtualFile>(testFolders);
                }

                if (roots.size() == 1) {
                    result.setResult(roots.get(0));
                }
                else {
                    PsiDirectory defaultDir = chooseDefaultDirectory(targetPackage.getDirectories(),roots, myTargetModule, myProject);
                    result.setResult(MoveClassesOrPackagesUtil.chooseSourceRoot(targetPackage, roots, defaultDir));
                }
            }
        }.execute().getResultObject();

        if (selectedRoot == null) return null;

        return new WriteCommandAction<PsiDirectory>(myProject, CodeInsightBundle.message("create.directory.command")) {
            protected void run(Result<? super PsiDirectory> result) throws Throwable {
                result.setResult(RefactoringUtil.createPackageDirectoryInSourceRoot(targetPackage, selectedRoot));
            }
        }.execute().getResultObject();
    }

    @Nullable
    private PsiDirectory chooseDefaultDirectory(PsiDirectory[] directories, List<VirtualFile> roots, Module myTargetModule, Project myProject) {
        List<PsiDirectory> dirs = new ArrayList<PsiDirectory>();
        for (VirtualFile file : ModuleRootManager.getInstance(myTargetModule).getSourceRoots(JavaSourceRootType.TEST_SOURCE)) {
            final PsiDirectory dir = PsiManager.getInstance(myProject).findDirectory(file);
            if (dir != null) {
                dirs.add(dir);
            }
        }
        if (!dirs.isEmpty()) {
            for (PsiDirectory dir : dirs) {
                final String dirName = dir.getVirtualFile().getPath();
                if (dirName.contains("generated")) continue;
                return dir;
            }
            return dirs.get(0);
        }
        for (PsiDirectory dir : directories) {
            final VirtualFile file = dir.getVirtualFile();
            for (VirtualFile root : roots) {
                if (VfsUtilCore.isAncestor(root, file, false)) {
                    final PsiDirectory rootDir = PsiManager.getInstance(myProject).findDirectory(root);
                    if (rootDir != null) {
                        return rootDir;
                    }
                }
            }
        }
        return null;
//        return PackageUtil.findPossiblePackageDirectoryInModule(myTargetModule, packageName); //from v14
    }
    /**
     * @see JavaProjectRootsUtil#collectSuitableDestinationSourceRoots (*added in v15)
     */
    public static void collectSuitableDestinationSourceRoots(@NotNull Module module, @NotNull List<VirtualFile> result) {
        for (ContentEntry entry : ModuleRootManager.getInstance(module).getContentEntries()) {
            for (SourceFolder sourceFolder : entry.getSourceFolders(JavaModuleSourceRootTypes.SOURCES)) {
                if (!isForGeneratedSources(sourceFolder)) {
                    ContainerUtil.addIfNotNull(result, sourceFolder.getFile());
                }
            }
        }
    }
    /**
     * @see JavaProjectRootsUtil#isForGeneratedSources
     */
    public static boolean isForGeneratedSources(SourceFolder sourceFolder) {
        JavaSourceRootProperties properties = sourceFolder.getJpsElement().getProperties(JavaModuleSourceRootTypes.SOURCES);
        JavaResourceRootProperties resourceProperties = sourceFolder.getJpsElement().getProperties(JavaModuleSourceRootTypes.RESOURCES);
        return properties != null && properties.isForGeneratedSources() || resourceProperties != null && resourceProperties.isForGeneratedSources();
    }

}
