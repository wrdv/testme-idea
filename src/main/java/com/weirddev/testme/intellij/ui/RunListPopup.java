package com.weirddev.testme.intellij.ui;

import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.popup.ListPopupStep;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.ui.popup.WizardPopup;
import com.intellij.ui.popup.list.ListPopupImpl;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Date: 15/09/2017
 *
 * @author Yaron Yamin
 */
class RunListPopup extends ListPopupImpl {
    private ChooseRunConfigurationPopup chooseRunConfigurationPopup;

    public RunListPopup(ChooseRunConfigurationPopup chooseRunConfigurationPopup, ListPopupStep step) {
        super(step);
        this.chooseRunConfigurationPopup = chooseRunConfigurationPopup;
        chooseRunConfigurationPopup.registerActions(this);
    }

    protected RunListPopup(ChooseRunConfigurationPopup chooseRunConfigurationPopup, WizardPopup aParent, ListPopupStep aStep, Object parentValue) {
        super(aParent, aStep, parentValue);
        this.chooseRunConfigurationPopup = chooseRunConfigurationPopup;
        chooseRunConfigurationPopup.registerActions(this);
    }

    @Override
    protected WizardPopup createPopup(WizardPopup parent, PopupStep step, Object parentValue) {
        return new RunListPopup(chooseRunConfigurationPopup,parent, (ListPopupStep) step, parentValue);
    }

    @Override
    public void handleSelect(boolean handleFinalChoices, InputEvent e) {
        if (e instanceof MouseEvent && e.isShiftDown()) {
            handleShiftClick(handleFinalChoices, e, this);
            return;
        }

        _handleSelect(handleFinalChoices, e);
    }

    private void _handleSelect(boolean handleFinalChoices, InputEvent e) {
        super.handleSelect(handleFinalChoices, e);
    }

    protected void handleShiftClick(boolean handleFinalChoices, final InputEvent inputEvent, final RunListPopup popup) {
        chooseRunConfigurationPopup.myCurrentExecutor = chooseRunConfigurationPopup.myAlternativeExecutor;
        popup._handleSelect(handleFinalChoices, inputEvent);
    }

    @Override
    protected ListCellRenderer getListElementRenderer() {
        boolean hasSideBar = false;
        for (Object each : getListStep().getValues()) {
            if (each instanceof ChooseRunConfigurationPopup.Wrapper) {
                if (((ChooseRunConfigurationPopup.Wrapper) each).getMnemonic() != -1) {
                    hasSideBar = true;
                    break;
                }
            }
        }
        return new RunListElementRenderer(this, hasSideBar);
    }

    public void removeSelected() {
        final PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        if (!propertiesComponent.isTrueValue("run.configuration.delete.ad")) {
            propertiesComponent.setValue("run.configuration.delete.ad", Boolean.toString(true));
        }

        final int index = getSelectedIndex();
        if (index == -1) {
            return;
        }

        final Object o = getListModel().get(index);
        if (o != null && o instanceof ItemWrapper && ((ItemWrapper) o).canBeDeleted()) {
            ChooseRunConfigurationPopup.deleteConfiguration(chooseRunConfigurationPopup.myProject, (RunnerAndConfigurationSettings) ((ItemWrapper) o).getValue());
            getListModel().deleteItem(o);
            final List<Object> values = getListStep().getValues();
            values.remove(o);

            if (index < values.size()) {
                onChildSelectedFor(values.get(index));
            } else if (index - 1 >= 0) {
                onChildSelectedFor(values.get(index - 1));
            }
        }
    }
}
