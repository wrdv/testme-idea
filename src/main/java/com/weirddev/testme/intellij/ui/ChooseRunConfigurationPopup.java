package com.weirddev.testme.intellij.ui;

/**
 * Date: 15/09/2017
 *
 * @author Yaron Yamin
 */

import com.intellij.execution.*;
import com.intellij.execution.actions.ExecutorProvider;
import com.intellij.execution.impl.RunDialog;
import com.intellij.execution.runners.ExecutionUtil;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ListSeparator;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.ui.popup.list.ListPopupImpl;
import com.intellij.ui.speedSearch.SpeedSearch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @see com.intellij.execution.actions.ChooseRunConfigurationPopup
 */
public class ChooseRunConfigurationPopup implements ExecutorProvider {

    final Project myProject;
    @NotNull
    final String myAddKey;
    @NotNull private final Executor myDefaultExecutor;
    @Nullable
    final Executor myAlternativeExecutor;

    Executor myCurrentExecutor;
    boolean myEditConfiguration;
    private final RunListPopup myPopup;

    public ChooseRunConfigurationPopup(@NotNull Project project,
                                       @NotNull String addKey,
                                       @NotNull Executor defaultExecutor,
                                       @Nullable Executor alternativeExecutor) {
        myProject = project;
        myAddKey = addKey;
        myDefaultExecutor = defaultExecutor;
        myAlternativeExecutor = alternativeExecutor;

        myPopup = new RunListPopup(this, new ConfigurationListPopupStep(this, myProject, this, myDefaultExecutor.getActionName()));
    }

    public void show() {

        final String adText = getAdText(myAlternativeExecutor);
        if (adText != null) {
            myPopup.setAdText(adText);
        }

        myPopup.showCenteredInCurrentWindow(myProject);
    }

    @Nullable
    protected String getAdText(final Executor alternateExecutor) {
        final PropertiesComponent properties = PropertiesComponent.getInstance();
        if (alternateExecutor != null && !properties.isTrueValue(myAddKey)) {
            return String
                    .format("Hold %s to %s", KeymapUtil.getKeystrokeText(KeyStroke.getKeyStroke("SHIFT")), alternateExecutor.getActionName());
        }

        if (!properties.isTrueValue("run.configuration.edit.ad")) {
            return String.format("Press %s to Edit", KeymapUtil.getKeystrokeText(KeyStroke.getKeyStroke("F4")));
        }

        if (!properties.isTrueValue("run.configuration.delete.ad")) {
            return String.format("Press %s to Delete configuration", KeymapUtil.getKeystrokeText(KeyStroke.getKeyStroke("DELETE")));
        }

        return null;
    }

    void registerActions(final RunListPopup popup) {
        popup.registerAction("alternateExecutor", KeyStroke.getKeyStroke("shift pressed SHIFT"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myCurrentExecutor = myAlternativeExecutor;
                updatePresentation();
            }
        });

        popup.registerAction("restoreDefaultExecutor", KeyStroke.getKeyStroke("released SHIFT"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myCurrentExecutor = myDefaultExecutor;
                updatePresentation();
            }
        });


        popup.registerAction("invokeAction", KeyStroke.getKeyStroke("shift ENTER"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popup.handleSelect(true);
            }
        });

        popup.registerAction("editConfiguration", KeyStroke.getKeyStroke("F4"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myEditConfiguration = true;
                popup.handleSelect(true);
            }
        });


        popup.registerAction("deleteConfiguration", KeyStroke.getKeyStroke("DELETE"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popup.removeSelected();
            }
        });

        popup.registerAction("deleteConfiguration_bksp", KeyStroke.getKeyStroke("BACK_SPACE"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SpeedSearch speedSearch = popup.getSpeedSearch();
                if (speedSearch.isHoldingFilter()) {
                    speedSearch.backspace();
                    speedSearch.update();
                }
                else {
                    popup.removeSelected();
                }
            }
        });

        final Action action0 = createNumberAction(0, popup, myDefaultExecutor);
        final Action action0_ = createNumberAction(0, popup, myAlternativeExecutor);
        popup.registerAction("0Action", KeyStroke.getKeyStroke("0"), action0);
        popup.registerAction("0Action_", KeyStroke.getKeyStroke("shift pressed 0"), action0_);
        popup.registerAction("0Action1", KeyStroke.getKeyStroke("NUMPAD0"), action0);
        popup.registerAction("0Action_1", KeyStroke.getKeyStroke("shift pressed NUMPAD0"), action0_);

        final Action action1 = createNumberAction(1, popup, myDefaultExecutor);
        final Action action1_ = createNumberAction(1, popup, myAlternativeExecutor);
        popup.registerAction("1Action", KeyStroke.getKeyStroke("1"), action1);
        popup.registerAction("1Action_", KeyStroke.getKeyStroke("shift pressed 1"), action1_);
        popup.registerAction("1Action1", KeyStroke.getKeyStroke("NUMPAD1"), action1);
        popup.registerAction("1Action_1", KeyStroke.getKeyStroke("shift pressed NUMPAD1"), action1_);

        final Action action2 = createNumberAction(2, popup, myDefaultExecutor);
        final Action action2_ = createNumberAction(2, popup, myAlternativeExecutor);
        popup.registerAction("2Action", KeyStroke.getKeyStroke("2"), action2);
        popup.registerAction("2Action_", KeyStroke.getKeyStroke("shift pressed 2"), action2_);
        popup.registerAction("2Action1", KeyStroke.getKeyStroke("NUMPAD2"), action2);
        popup.registerAction("2Action_1", KeyStroke.getKeyStroke("shift pressed NUMPAD2"), action2_);

        final Action action3 = createNumberAction(3, popup, myDefaultExecutor);
        final Action action3_ = createNumberAction(3, popup, myAlternativeExecutor);
        popup.registerAction("3Action", KeyStroke.getKeyStroke("3"), action3);
        popup.registerAction("3Action_", KeyStroke.getKeyStroke("shift pressed 3"), action3_);
        popup.registerAction("3Action1", KeyStroke.getKeyStroke("NUMPAD3"), action3);
        popup.registerAction("3Action_1", KeyStroke.getKeyStroke("shift pressed NUMPAD3"), action3_);
    }

    private void updatePresentation() {
        myPopup.setCaption(getExecutor().getActionName());
    }

    static void execute(final ItemWrapper itemWrapper, final Executor executor) {
        if (executor == null) {
            return;
        }

        final DataContext dataContext = DataManager.getInstance().getDataContext();
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        if (project != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    itemWrapper.perform(project, executor, dataContext);
                }
            });
        }
    }

    void editConfiguration(@NotNull final Project project, @NotNull final RunnerAndConfigurationSettings configuration) {
        final Executor executor = getExecutor();
        PropertiesComponent.getInstance().setValue("run.configuration.edit.ad", Boolean.toString(true));
        if (RunDialog.editConfiguration(project, configuration, "Edit configuration settings", executor)) {
            RunManagerEx.getInstanceEx(project).setSelectedConfiguration(configuration);
            ExecutionUtil.runConfiguration(configuration, executor);
        }
    }

    static void deleteConfiguration(final Project project, @NotNull final RunnerAndConfigurationSettings configurationSettings) {
        final RunManagerEx manager = RunManagerEx.getInstanceEx(project);
        manager.removeConfiguration(configurationSettings);
    }

    @Override
    @NotNull
    public Executor getExecutor() {
        return myCurrentExecutor == null ? myDefaultExecutor : myCurrentExecutor;
    }

    private static Action createNumberAction(final int number, final ListPopupImpl listPopup, final Executor executor) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listPopup.getSpeedSearch().isHoldingFilter())
                    return;
                for (final Object item : listPopup.getListStep().getValues()) {
                    if (item instanceof ItemWrapper && ((ItemWrapper)item).getMnemonic() == number) {
                        listPopup.setFinalRunnable(new Runnable() {
                            @Override
                            public void run() {
                                execute((ItemWrapper)item, executor);
                            }
                        });
                        listPopup.closeOk(null);
                    }
                }
            }
        };
    }

    abstract static class Wrapper {
        private int myMnemonic = -1;
        private final boolean myAddSeparatorAbove;
        private boolean myChecked;

        protected Wrapper(boolean addSeparatorAbove) {
            myAddSeparatorAbove = addSeparatorAbove;
        }

        public int getMnemonic() {
            return myMnemonic;
        }

        public boolean isChecked() {
            return myChecked;
        }

        public void setChecked(boolean checked) {
            myChecked = checked;
        }

        public void setMnemonic(int mnemonic) {
            myMnemonic = mnemonic;
        }

        public boolean addSeparatorAbove() {
            return myAddSeparatorAbove;
        }

        @Nullable
        public abstract Icon getIcon();

        public abstract String getText();

        public boolean canBeDeleted() {
            return false;
        }

        @Override
        public String toString() {
            return "Wrapper[" + getText() + "]";
        }
    }

    static final class ConfigurationActionsStep extends BaseListPopupStep<ActionWrapper> {

        @NotNull private final RunnerAndConfigurationSettings mySettings;
        @NotNull private final Project myProject;

        ConfigurationActionsStep(@NotNull final Project project,
                                 ChooseRunConfigurationPopup action,
                                 @NotNull final RunnerAndConfigurationSettings settings, final boolean dynamic) {
            super(null, buildActions(project, action, settings, dynamic));
            myProject = project;
            mySettings = settings;
        }

        @NotNull
        public RunnerAndConfigurationSettings getSettings() {
            return mySettings;
        }

        public String getName() {
            return mySettings.getName();
        }

        public Icon getIcon() {
            return RunManagerEx.getInstanceEx(myProject).getConfigurationIcon(mySettings);
        }

        @Override
        public ListSeparator getSeparatorAbove(ActionWrapper value) {
            return value.addSeparatorAbove() ? new ListSeparator() : null;
        }

        private static ActionWrapper[] buildActions(@NotNull final Project project,
                                                                                                               final ChooseRunConfigurationPopup action,
                                                                                                               @NotNull final RunnerAndConfigurationSettings settings,
                                                                                                               final boolean dynamic) {
            final List<ActionWrapper> result = new ArrayList<ActionWrapper>();

            final ExecutionTarget active = ExecutionTargetManager.getActiveTarget(project);
            for (final ExecutionTarget eachTarget : ExecutionTargetManager.getTargetsToChooseFor(project, settings)) {
                result.add(new ActionWrapper(eachTarget.getDisplayName(), eachTarget.getIcon()) {
                    {
                        setChecked(eachTarget.equals(active));
                    }

                    @Override
                    public void perform() {
                        final RunManagerEx manager = RunManagerEx.getInstanceEx(project);
                        if (dynamic) {
                            manager.setTemporaryConfiguration(settings);
                        }
                        manager.setSelectedConfiguration(settings);

                        ExecutionTargetManager.setActiveTarget(project, eachTarget);
                        ExecutionUtil.runConfiguration(settings, action.getExecutor());
                    }
                });
            }

            boolean isFirst = true;
            for (final Executor executor : ExecutorRegistry.getInstance().getRegisteredExecutors()) {
                final ProgramRunner runner = RunnerRegistry.getInstance().getRunner(executor.getId(), settings.getConfiguration());
                if (runner != null) {
                    result.add(new ActionWrapper(executor.getActionName(), executor.getIcon(), isFirst) {
                        @Override
                        public void perform() {
                            final RunManagerEx manager = RunManagerEx.getInstanceEx(project);
                            if (dynamic) {
                                manager.setTemporaryConfiguration(settings);
                            }
                            manager.setSelectedConfiguration(settings);
                            ExecutionUtil.runConfiguration(settings, executor);
                        }
                    });
                    isFirst = false;
                }
            }

            result.add(new ActionWrapper("Edit...", AllIcons.Actions.EditSource, true) {
                @Override
                public void perform() {
                    final RunManagerEx manager = RunManagerEx.getInstanceEx(project);
                    if (dynamic) manager.setTemporaryConfiguration(settings);
                    action.editConfiguration(project, settings);
                }
            });

            if (settings.isTemporary() || dynamic) {
                result.add(new ActionWrapper("Save configuration", AllIcons.Actions.Menu_saveall) {
                    @Override
                    public void perform() {
                        final RunManagerEx manager = RunManagerEx.getInstanceEx(project);
                        if (dynamic) manager.setTemporaryConfiguration(settings);
                        manager.makeStable(settings);
                    }
                });
            }

            return result.toArray(new ActionWrapper[result.size()]);
        }

        @Override
        public PopupStep onChosen(final ActionWrapper selectedValue, boolean finalChoice) {
            return doFinalStep(new Runnable() {
                @Override
                public void run() {
                    selectedValue.perform();
                }
            });
        }

        @Override
        public Icon getIconFor(ActionWrapper aValue) {
            return aValue.getIcon();
        }

        @NotNull
        @Override
        public String getTextFor(ActionWrapper value) {
            return value.getText();
        }
    }

    private abstract static class ActionWrapper extends Wrapper {
        private final String myName;
        private final Icon myIcon;

        private ActionWrapper(String name, Icon icon) {
            this(name, icon, false);
        }

        private ActionWrapper(String name, Icon icon, boolean addSeparatorAbove) {
            super(addSeparatorAbove);
            myName = name;
            myIcon = icon;
        }

        public abstract void perform();

        @Override
        public String getText() {
            return myName;
        }

        @Override
        public Icon getIcon() {
            return myIcon;
        }
    }

    static class FolderWrapper extends ItemWrapper<String> {
        private final Project myProject;
        private final ExecutorProvider myExecutorProvider;
        private final List<RunnerAndConfigurationSettings> myConfigurations;

        FolderWrapper(Project project, ExecutorProvider executorProvider, @Nullable String value, List<RunnerAndConfigurationSettings> configurations) {
            super(value);
            myProject = project;
            myExecutorProvider = executorProvider;
            myConfigurations = configurations;
        }

        @Override
        public void perform(@NotNull Project project, @NotNull Executor executor, @NotNull DataContext context) {
            RunnerAndConfigurationSettings selectedConfiguration = RunManagerEx.getInstanceEx(project).getSelectedConfiguration();
            if (myConfigurations.contains(selectedConfiguration)) {
                RunManagerEx.getInstanceEx(project).setSelectedConfiguration(selectedConfiguration);
                ExecutionUtil.runConfiguration(selectedConfiguration, myExecutorProvider.getExecutor());
            }
        }

        @Nullable
        @Override
        public Icon getIcon() {
            return AllIcons.Nodes.Folder;
        }

        @Override
        public String getText() {
            return getValue();
        }

        @Override
        public boolean hasActions() {
            return true;
        }

        @Override
        public PopupStep getNextStep(Project project, ChooseRunConfigurationPopup action) {
            List<ConfigurationActionsStep> steps = new ArrayList<ConfigurationActionsStep>();
            for (RunnerAndConfigurationSettings settings : myConfigurations) {
                steps.add(new ConfigurationActionsStep(project, action, settings, false));
            }
            return new FolderStep(myProject, myExecutorProvider, null, steps);
        }
    }

    private static final class FolderStep extends BaseListPopupStep<ConfigurationActionsStep> {
        private final Project myProject;
        private final ExecutorProvider myExecutorProvider;

        private FolderStep(Project project, ExecutorProvider executorProvider, String folderName, List<ConfigurationActionsStep> children
        ) {
            super(folderName, children, new ArrayList<Icon>());
            myProject = project;
            myExecutorProvider = executorProvider;
        }

        @Override
        public PopupStep onChosen(final ConfigurationActionsStep selectedValue, boolean finalChoice) {
            if (finalChoice) {
                return doFinalStep(new Runnable() {
                    @Override
                    public void run() {
                        RunnerAndConfigurationSettings settings = selectedValue.getSettings();
                        RunManagerEx.getInstanceEx(myProject).setSelectedConfiguration(settings);
                        ExecutionUtil.runConfiguration(settings, myExecutorProvider.getExecutor());
                    }
                });
            } else {
                return selectedValue;
            }
        }

        @Override
        public Icon getIconFor(ConfigurationActionsStep aValue) {
            return aValue.getIcon();
        }

        @NotNull
        @Override
        public String getTextFor(ConfigurationActionsStep value) {
            return value.getName();
        }

        @Override
        public boolean hasSubstep(ConfigurationActionsStep selectedValue) {
            return !selectedValue.getValues().isEmpty();
        }
    }

}
