package com.weirddev.testme.intellij.action;

import com.intellij.CommonBundle;
import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiPackage;
import com.intellij.refactoring.PackageWrapper;
import com.intellij.refactoring.move.moveClassesOrPackages.MoveClassesOrPackagesUtil;
import com.intellij.refactoring.util.RefactoringMessageUtil;
import com.intellij.refactoring.util.RefactoringUtil;
import com.intellij.testIntegration.createTest.CreateTestDialog;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.util.ArrayList;
import java.util.HashSet;
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

    public PsiDirectory getOrCreateDirectory(@NotNull Project project, PsiPackage targetPackage, Module targetModule, String targetClass) {
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
private PsiDirectory chooseDefaultDirectory(String packageName, Module myTargetModule, Project myProject) {
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
    return PackageUtil.findPossiblePackageDirectoryInModule(myTargetModule, packageName);
}

    @Nullable
    private PsiDirectory selectTargetDirectory(final String packageName, final Project myProject, final Module myTargetModule) throws IncorrectOperationException {
        final PackageWrapper targetPackage = new PackageWrapper(PsiManager.getInstance(myProject), packageName);
        final VirtualFile selectedRoot = new ReadAction<VirtualFile>() {
            protected void run(Result<VirtualFile> result) throws Throwable {
                final HashSet<VirtualFile> testFolders = new HashSet<VirtualFile>();
                CreateTestMeAction.checkForTestRoots(myTargetModule, testFolders);
                List<VirtualFile> roots;
                if (testFolders.isEmpty()) {
                    roots = ModuleRootManager.getInstance(myTargetModule).getSourceRoots(JavaModuleSourceRootTypes.SOURCES);
                    if (roots.isEmpty()) return;
                } else {
                    roots = new ArrayList<VirtualFile>(testFolders);
                }

                if (roots.size() == 1) {
                    result.setResult(roots.get(0));
                }
                else {
                    PsiDirectory defaultDir = chooseDefaultDirectory(packageName, myTargetModule, myProject);
                    result.setResult(MoveClassesOrPackagesUtil.chooseSourceRoot(targetPackage, roots, defaultDir));
                }
            }
        }.execute().getResultObject();

        if (selectedRoot == null) return null;

        return new WriteCommandAction<PsiDirectory>(myProject, CodeInsightBundle.message("create.directory.command")) {
            protected void run(Result<PsiDirectory> result) throws Throwable {
                result.setResult(RefactoringUtil.createPackageDirectoryInSourceRoot(targetPackage, selectedRoot));
            }
        }.execute().getResultObject();
    }

}
