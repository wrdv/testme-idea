package com.weirddev.testme.intellij.ui;

import com.intellij.execution.Executor;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManagerEx;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.runners.ExecutionUtil;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupStep;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Date: 15/09/2017
 *
 * @author Yaron Yamin
 */
public abstract class ItemWrapper<T> extends ChooseRunConfigurationPopup.Wrapper {
    private final T myValue;
    private boolean myDynamic;


    protected ItemWrapper(@Nullable final T value) {
        this(value, false);
    }

    protected ItemWrapper(@Nullable final T value, boolean addSeparatorAbove) {
        super(addSeparatorAbove);
        myValue = value;
    }

    public T getValue() {
        return myValue;
    }

    public boolean isDynamic() {
        return myDynamic;
    }

    public void setDynamic(final boolean b) {
        myDynamic = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemWrapper)) return false;

        ItemWrapper that = (ItemWrapper) o;

        if (myValue != null ? !myValue.equals(that.myValue) : that.myValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return myValue != null ? myValue.hashCode() : 0;
    }

    public abstract void perform(@NotNull final Project project, @NotNull final Executor executor, @NotNull final DataContext context);

    @Nullable
    public ConfigurationType getType() {
        return null;
    }

    public boolean available(Executor executor) {
        return false;
    }

    public boolean hasActions() {
        return false;
    }

    public PopupStep getNextStep(Project project, ChooseRunConfigurationPopup action) {
        return PopupStep.FINAL_CHOICE;
    }

    public static ItemWrapper wrap(@NotNull final Project project,
                                   @NotNull final RunnerAndConfigurationSettings settings,
                                   final boolean dynamic) {
        final ItemWrapper result = wrap(project, settings);
        result.setDynamic(dynamic);
        return result;
    }

    public static ItemWrapper wrap(@NotNull final Project project, @NotNull final RunnerAndConfigurationSettings settings) {
        return new ItemWrapper<RunnerAndConfigurationSettings>(settings) {
            @Override
            public void perform(@NotNull Project project, @NotNull Executor executor, @NotNull DataContext context) {
                RunnerAndConfigurationSettings config = getValue();
                RunManagerEx.getInstanceEx(project).setSelectedConfiguration(config);
                ExecutionUtil.runConfiguration(config, executor);
            }

            @Override
            public ConfigurationType getType() {
                return getValue().getType();
            }

            @Override
            public Icon getIcon() {
                return RunManagerEx.getInstanceEx(project).getConfigurationIcon(getValue());
            }

            @Override
            public String getText() {
                return getValue().getName()+" hacked in wrap()";
            }

            @Override
            public boolean hasActions() {
                return true;
            }

            @Override
            public boolean available(Executor executor) {
                return ProgramRunnerUtil.getRunner(executor.getId(), getValue()) != null;
            }

            @Override
            public PopupStep getNextStep(@NotNull final Project project, @NotNull final ChooseRunConfigurationPopup action) {
                return new ChooseRunConfigurationPopup.ConfigurationActionsStep(project, action, getValue(), isDynamic());
            }
        };
    }

    @Override
    public boolean canBeDeleted() {
        return !isDynamic() && getValue() instanceof RunnerAndConfigurationSettings;
    }
}
