package com.weirddev.testme.intellij.action.helpers;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.psi.PsiDirectory;
import com.intellij.refactoring.util.RefactoringMessageUtil;
import com.weirddev.testme.intellij.template.TemplateDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeneratedClassNameResolver {
    @NotNull
    public ClassNameSelection resolveClassName(@NotNull Project project, PsiDirectory targetDirectory, String targetTestSubjectClassName, TemplateDescriptor templateDescriptor) {
        String className = buildDefaultTestClassName(targetTestSubjectClassName, templateDescriptor);
        ClassNameSelection classNameSelection;
        //TODO add merge option
        final String fileCreateErrorMessage = RefactoringMessageUtil.checkCanCreateFile(targetDirectory, className + "." + FileUtilRt.getExtension(templateDescriptor.getFilename()));
        if (fileCreateErrorMessage != null) {
            classNameSelection = getUserDecision(project, className, fileCreateErrorMessage, "Target Test File Already Exists");
        } else {
            String classCreationErrorMessage = RefactoringMessageUtil.checkCanCreateClass(targetDirectory, className);
            if (classCreationErrorMessage != null) {
                classNameSelection = getUserDecision(project, className, classCreationErrorMessage, "Target Test Class Already Exists");
            } else {
                classNameSelection = new ClassNameSelection(className, ClassNameSelection.UserDecision.New);
            }
        }
        return classNameSelection;
    }

    @NotNull
    private ClassNameSelection getUserDecision(@NotNull Project project, final String className, String fileCreateErrorMessage, String dialogTitle) {
        ClassNameSelection classNameSelection;
        final int selection = Messages.showDialog(project, fileCreateErrorMessage + "\nWhat would you like to do?", dialogTitle, new String[]{"Go to existing test", "&Pick a different name...", "&Cancel"}, 0, Messages.getQuestionIcon());
        if (selection == 2 || selection == -1) {
            classNameSelection = new ClassNameSelection(null, ClassNameSelection.UserDecision.Abort);
        } else if (selection == 1) {
            String resolvedClassName = Messages.showInputDialog(project, "Please rename target test class:", "Rename Test Class", Messages.getQuestionIcon(), className, new InputValidatorEx() {
                @Nullable
                @Override
                public String getErrorText(String inputString) {
                    if (checkInput(inputString)) {
                        return null;
                    } else {
                        return "Class name collides with existing class - " + className + ". Please enter a different name";
                    }
                }

                @Override
                public boolean checkInput(String inputString) {
                    return !className.equalsIgnoreCase(inputString);
                }

                @Override
                public boolean canClose(String inputString) {
                    return checkInput(inputString);
                }

            });
            classNameSelection = new ClassNameSelection(resolvedClassName, ClassNameSelection.UserDecision.New);
        } else {
            classNameSelection = new ClassNameSelection(className, ClassNameSelection.UserDecision.Goto);
        }
        return classNameSelection;
    }

    private String buildDefaultTestClassName(String targetTestSubjectClassName, TemplateDescriptor templateDescriptor) {
        return String.format(templateDescriptor.getTestClassFormat(), targetTestSubjectClassName);
    }
}