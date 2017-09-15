package com.weirddev.testme.intellij.ui;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * Date: 15/09/2017
 *
 * @author Yaron Yamin
 */
public class TestMeTemplatesPopupHandler {
    public void show(Project project, Editor editor, PsiFile file, TestMePopUpHandler.GotoData gotoData) {

    }

/*
    private void showAddPopup(final boolean showApplicableTypesOnly) {
        ConfigurationType[] allTypes = getRunManager().getConfigurationFactories(false);
        final List<ConfigurationType> configurationTypes = getTypesToShow(showApplicableTypesOnly, allTypes);
        Collections.sort(configurationTypes, new Comparator<ConfigurationType>() {
            @Override
            public int compare(final ConfigurationType type1, final ConfigurationType type2) {
                return type1.getDisplayName().compareToIgnoreCase(type2.getDisplayName());
            }
        });
        final int hiddenCount = allTypes.length - configurationTypes.size();
        if (hiddenCount > 0) {
            configurationTypes.add(null);
        }

        final ListPopup popup = JBPopupFactory.getInstance().createListPopup(new BaseListPopupStep<ConfigurationType>(
                ExecutionBundle.message("add.new.run.configuration.acrtion.name"), configurationTypes) {

            @Override
            @NotNull
            public String getTextFor(final ConfigurationType type) {
                return type != null ? type.getDisplayName() :  hiddenCount + " items more (irrelevant)...";
            }

            @Override
            public boolean isSpeedSearchEnabled() {
                return true;
            }

            @Override
            public boolean canBeHidden(ConfigurationType value) {
                return true;
            }

            @Override
            public Icon getIconFor(final ConfigurationType type) {
                return type != null ? type.getIcon() : EmptyIcon.ICON_16;
            }

            @Override
            public PopupStep onChosen(final ConfigurationType type, final boolean finalChoice) {
                if (hasSubstep(type)) {
                    return getSupStep(type);
                }
                if (type == null) {
                    return doFinalStep(new Runnable() {
                        @Override
                        public void run() {
                            showAddPopup(false);
                        }
                    });
                }

                final ConfigurationFactory[] factories = type.getConfigurationFactories();
                if (factories.length > 0) {
                    createNewConfiguration(factories[0]);
                }
                return FINAL_CHOICE;
            }

            @Override
            public int getDefaultOptionIndex() {
                ConfigurationType type = getSelectedConfigurationType();
                return type != null ? configurationTypes.indexOf(type) : super.getDefaultOptionIndex();
            }

            private ListPopupStep getSupStep(final ConfigurationType type) {
                final ConfigurationFactory[] factories = type.getConfigurationFactories();
                Arrays.sort(factories, new Comparator<ConfigurationFactory>() {
                    @Override
                    public int compare(final ConfigurationFactory factory1, final ConfigurationFactory factory2) {
                        return factory1.getName().compareToIgnoreCase(factory2.getName());
                    }
                });
                return new BaseListPopupStep<ConfigurationFactory>(
                        ExecutionBundle.message("add.new.run.configuration.action.name", type.getDisplayName()), factories) {

                    @Override
                    @NotNull
                    public String getTextFor(final ConfigurationFactory value) {
                        return value.getName();
                    }

                    @Override
                    public Icon getIconFor(final ConfigurationFactory factory) {
                        return factory.getIcon();
                    }

                    @Override
                    public PopupStep onChosen(final ConfigurationFactory factory, final boolean finalChoice) {
                        createNewConfiguration(factory);
                        return FINAL_CHOICE;
                    }
                };
            }

            @Override
            public boolean hasSubstep(final ConfigurationType type) {
                return type != null && type.getConfigurationFactories().length > 1;
            }
        });
        //new TreeSpeedSearch(myTree);
        popup.showUnderneathOf(myToolbarDecorator.getActionsPanel());
    }
*/

}
