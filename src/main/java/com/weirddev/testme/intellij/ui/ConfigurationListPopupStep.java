package com.weirddev.testme.intellij.ui;

import com.intellij.execution.*;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.ConfigurationFromContext;
import com.intellij.execution.actions.ExecutorProvider;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.UnknownConfigurationType;
import com.intellij.execution.impl.EditConfigurationsDialog;
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl;
import com.intellij.execution.runners.ExecutionUtil;
import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.idea.ActionsBundle;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ListSeparator;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Date: 15/09/2017
 *
 * @author Yaron Yamin
 */
final class ConfigurationListPopupStep extends BaseListPopupStep<ItemWrapper> {
    private final Project myProject;
    private final ChooseRunConfigurationPopup myAction;
    private int myDefaultConfiguration = -1;

    ConfigurationListPopupStep(@NotNull final ChooseRunConfigurationPopup action,
                               @NotNull final Project project,
                               @NotNull final ExecutorProvider executorProvider,
                               @NotNull final String title) {
        super(title, createSettingsList(project, executorProvider, true));
        myProject = project;
        myAction = action;

        if (-1 == getDefaultOptionIndex()) {
            myDefaultConfiguration = getDynamicIndex();
        }
    }

    protected static boolean canRun(@NotNull final Executor executor, final RunnerAndConfigurationSettings settings) {
        return ProgramRunnerUtil.getRunner(executor.getId(), settings) != null;
    }

    public static ItemWrapper[] createSettingsList(@NotNull Project project, @NotNull ExecutorProvider executorProvider, boolean createEditAction) {
        List<ItemWrapper> result = new ArrayList<ItemWrapper>();

        if (createEditAction) {
            ItemWrapper<Void> edit = new ItemWrapper<Void>(null) {
                @Override
                public Icon getIcon() {
                    return AllIcons.Actions.EditSource;
                }

                @Override
                public String getText() {
                    return UIUtil.removeMnemonic(ActionsBundle.message("action.editRunConfigurations.text"));
                }

                @Override
                public void perform(@NotNull final Project project, @NotNull final Executor executor, @NotNull DataContext context) {
                    if (new EditConfigurationsDialog(project) {
                        @Override
                        protected void init() {
                            setOKButtonText(executor.getStartActionText());
                            setOKButtonIcon(executor.getIcon());
                            myExecutor = executor;
                            super.init();
                        }
                    }.showAndGet()) {
                        ApplicationManager.getApplication().invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                RunnerAndConfigurationSettings configuration = RunManager.getInstance(project).getSelectedConfiguration();
                                if (configuration != null) {
                                    ExecutionUtil.runConfiguration(configuration, executor);
                                }
                            }
                        }, project.getDisposed());
                    }
                }

                @Override
                public boolean available(Executor executor) {
                    return true;
                }
            };
            edit.setMnemonic(0);
            result.add(edit);
        }

        RunManagerEx manager = RunManagerEx.getInstanceEx(project);
        final RunnerAndConfigurationSettings selectedConfiguration = manager.getSelectedConfiguration();
        if (selectedConfiguration != null) {
            boolean isFirst = true;
            final ExecutionTarget activeTarget = ExecutionTargetManager.getActiveTarget(project);
            for (ExecutionTarget eachTarget : ExecutionTargetManager.getTargetsToChooseFor(project, selectedConfiguration)) {
                result.add(new ItemWrapper<ExecutionTarget>(eachTarget, isFirst) {
                    {
                        setChecked(getValue().equals(activeTarget));
                    }

                    @Override
                    public Icon getIcon() {
                        return getValue().getIcon();
                    }

                    @Override
                    public String getText() {
                        return getValue().getDisplayName();
                    }

                    @Override
                    public void perform(@NotNull final Project project, @NotNull final Executor executor, @NotNull DataContext context) {
                        ExecutionTargetManager.setActiveTarget(project, getValue());
                        ExecutionUtil.runConfiguration(selectedConfiguration, executor);
                    }

                    @Override
                    public boolean available(Executor executor) {
                        return true;
                    }
                });
                isFirst = false;
            }
        }

        Map<RunnerAndConfigurationSettings, ItemWrapper> wrappedExisting = new LinkedHashMap<RunnerAndConfigurationSettings, ItemWrapper>();
        for (ConfigurationType type : manager.getConfigurationFactories()) {
            if (!(type instanceof UnknownConfigurationType)) {
                Map<String, List<RunnerAndConfigurationSettings>> structure = manager.getStructure(type);
                for (Map.Entry<String, List<RunnerAndConfigurationSettings>> entry : structure.entrySet()) {
                    if (entry.getValue().isEmpty()) {
                        continue;
                    }

                    final String key = entry.getKey();
                    if (key != null) {
                        boolean isSelected = entry.getValue().contains(selectedConfiguration);
                        if (isSelected) {
                            assert selectedConfiguration != null;
                        }
                        ChooseRunConfigurationPopup.FolderWrapper folderWrapper = new ChooseRunConfigurationPopup.FolderWrapper(project, executorProvider,
                                key + (isSelected ? "  (mnemonic is to \"" + selectedConfiguration.getName() + "\")" : ""),
                                entry.getValue());
                        if (isSelected) {
                            folderWrapper.setMnemonic(1);
                        }
                        result.add(folderWrapper);
                    }
                    else {
                        for (RunnerAndConfigurationSettings configuration : entry.getValue()) {
                            final ItemWrapper wrapped = ItemWrapper.wrap(project, configuration);
                            if (configuration == selectedConfiguration) {
                                wrapped.setMnemonic(1);
                            }
                            wrappedExisting.put(configuration, wrapped);
                        }
                    }
                }
            }
        }

        populateWithDynamicRunners(result, wrappedExisting, project, manager, selectedConfiguration);
        result.addAll(wrappedExisting.values());
        return result.toArray(new ItemWrapper[result.size()]);
    }

    @NotNull
    private static List<RunnerAndConfigurationSettings> populateWithDynamicRunners(final List<ItemWrapper> result,
                                                                                   Map<RunnerAndConfigurationSettings, ItemWrapper> existing,
                                                                                   final Project project, final RunManagerEx manager,
                                                                                   final RunnerAndConfigurationSettings selectedConfiguration) {

        final ArrayList<RunnerAndConfigurationSettings> contextConfigurations = new ArrayList<RunnerAndConfigurationSettings>();
        if (!EventQueue.isDispatchThread()) {
            return Collections.emptyList();
        }

        final DataContext dataContext = DataManager.getInstance().getDataContext();
        final ConfigurationContext context = ConfigurationContext.getFromContext(dataContext);

        final List<ConfigurationFromContext> producers = PreferredProducerFind.getConfigurationsFromContext(context.getLocation(),
                context, false);
        if (producers == null) return Collections.emptyList();

        Collections.sort(producers, ConfigurationFromContext.NAME_COMPARATOR);

        final RunnerAndConfigurationSettings[] preferred = {null};

        int i = 2; // selectedConfiguration == null ? 1 : 2;
        for (final ConfigurationFromContext fromContext : producers) {
            final RunnerAndConfigurationSettings configuration = fromContext.getConfigurationSettings();
            if (existing.keySet().contains(configuration)) {
                final ItemWrapper wrapper = existing.get(configuration);
                if (wrapper.getMnemonic() != 1) {
                    wrapper.setMnemonic(i);
                    i++;
                }
            }
            else {
                if (selectedConfiguration != null && configuration.equals(selectedConfiguration)) continue;
                contextConfigurations.add(configuration);

                if (preferred[0] == null) {
                    preferred[0] = configuration;
                }

                //noinspection unchecked
                final ItemWrapper wrapper = new ItemWrapper(configuration) {
                    @Override
                    public Icon getIcon() {
                        return RunManagerEx.getInstanceEx(project).getConfigurationIcon(configuration);
                    }

                    @Override
                    public String getText() {
                        return configuration.getName() +"  Hacked";
                    }

                    @Override
                    public boolean available(Executor executor) {
                        return canRun(executor, configuration);
                    }

                    @Override
                    public void perform(@NotNull Project project, @NotNull Executor executor, @NotNull DataContext context) {
                        manager.setTemporaryConfiguration(configuration);
                        RunManagerEx.getInstanceEx(project).setSelectedConfiguration(configuration);
                        ExecutionUtil.runConfiguration(configuration, executor);
                    }

                    @Override
                    public PopupStep getNextStep(@NotNull final Project project, @NotNull final ChooseRunConfigurationPopup action) {
                        return new ChooseRunConfigurationPopup.ConfigurationActionsStep(project, action, configuration, isDynamic());
                    }

                    @Override
                    public boolean hasActions() {
                        return true;
                    }
                };

                wrapper.setDynamic(true);
                wrapper.setMnemonic(i);
                result.add(wrapper);
                i++;
            }
        }

        return contextConfigurations;
    }

    private int getDynamicIndex() {
        int i = 0;
        for (final ItemWrapper wrapper : getValues()) {
            if (wrapper.isDynamic()) {
                return i;
            }
            i++;
        }

        return -1;
    }

    @Override
    public boolean isAutoSelectionEnabled() {
        return false;
    }

    @Override
    public ListSeparator getSeparatorAbove(ItemWrapper value) {
        if (value.addSeparatorAbove()) return new ListSeparator();

        final List<ItemWrapper> configurations = getValues();
        final int index = configurations.indexOf(value);
        if (index > 0 && index <= configurations.size() - 1) {
            final ItemWrapper aboveConfiguration = configurations.get(index - 1);

            if (aboveConfiguration != null && aboveConfiguration.isDynamic() != value.isDynamic()) {
                return new ListSeparator();
            }

            final ConfigurationType currentType = value.getType();
            final ConfigurationType aboveType = aboveConfiguration == null ? null : aboveConfiguration.getType();
            if (aboveType != currentType && currentType != null) {
                return new ListSeparator(); // new ListSeparator(currentType.getDisplayName());
            }
        }

        return null;
    }

    @Override
    public boolean isSpeedSearchEnabled() {
        return true;
    }

    @Override
    public int getDefaultOptionIndex() {
        final RunnerAndConfigurationSettings currentConfiguration = RunManager.getInstance(myProject).getSelectedConfiguration();
        if (currentConfiguration == null && myDefaultConfiguration != -1) {
            return myDefaultConfiguration;
        }

        return currentConfiguration instanceof RunnerAndConfigurationSettingsImpl ? getValues()
                .indexOf(ItemWrapper.wrap(myProject, currentConfiguration)) : -1;
    }

    @Override
    public PopupStep onChosen(final ItemWrapper wrapper, boolean finalChoice) {
        if (myAction.myEditConfiguration) {
            final Object o = wrapper.getValue();
            if (o instanceof RunnerAndConfigurationSettingsImpl) {
                return doFinalStep(new Runnable() {
                    @Override
                    public void run() {
                        myAction.editConfiguration(myProject, (RunnerAndConfigurationSettings) o);
                    }
                });
            }
        }

        if (finalChoice && wrapper.available(myAction.getExecutor())) {
            return doFinalStep(new Runnable() {
                @Override
                public void run() {
                    if (myAction.getExecutor() == myAction.myAlternativeExecutor) {
                        PropertiesComponent.getInstance().setValue(myAction.myAddKey, Boolean.toString(true));
                    }

                    wrapper.perform(myProject, myAction.getExecutor(), DataManager.getInstance().getDataContext());
                }
            });
        } else {
            return wrapper.getNextStep(myProject, myAction);
        }
    }

    @Override
    public boolean hasSubstep(ItemWrapper selectedValue) {
        return selectedValue.hasActions();
    }

    @NotNull
    @Override
    public String getTextFor(ItemWrapper value) {
        return value.getText();
    }

    @Override
    public Icon getIconFor(ItemWrapper value) {
        return value.getIcon();
    }
}
