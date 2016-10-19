package com.weirddev.testme.intellij;

import com.intellij.codeInsight.navigation.GotoTargetHandler;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Date: 10/15/2016
 *
 * @author Yaron Yamin
 */
public class TestMeActionCellRenderer extends DefaultListCellRenderer {
    private final IconTokensReplacer iconTokensReplacer=new IconTokensReplacerImpl();
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component result = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            GotoTargetHandler.AdditionalAction action = (GotoTargetHandler.AdditionalAction) value;
            if (action instanceof TestMeAdditionalAction) {
                JPanel jPanel = createPanel(list, isSelected);
                ArrayList<IconizedLabel> iconizedLabels = iconTokensReplacer.tokenize(action.getText(),action.getIcon());
                for (int i = 0; i < iconizedLabels.size(); i++) {
                    if (i == 0) {
                        setText(iconizedLabels.get(i).getText());
                        setIcon(action.getIcon());
                        setBorder(new EmptyBorder(0,0,0,0));
                        jPanel.add(result);
                    } else {
                        jPanel.add(createSubLabel(list, value, index, isSelected, cellHasFocus, iconizedLabels.get(i)));
                    }
                }
                return jPanel;
            } else {
                setText(action.getText());
                setIcon(action.getIcon());
            }
        }
        return result;
    }

    @NotNull
    private JPanel createPanel(JList list, boolean isSelected) {
        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        if (isSelected) {
            jPanel.setBackground(list.getSelectionBackground());
            jPanel.setForeground(list.getSelectionForeground());
        }
        else {
            jPanel.setBackground(list.getBackground());
            jPanel.setForeground(list.getForeground());
        }
        return jPanel;
    }

    private Component createSubLabel(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus, IconizedLabel iconizedLabel) {
        DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();
        Component listCellRendererComponent = defaultListCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        defaultListCellRenderer.setText(iconizedLabel.getText());
        defaultListCellRenderer.setIcon(iconizedLabel.getIcon());
        defaultListCellRenderer.setBorder(new EmptyBorder(0,0,0,0));
        return listCellRendererComponent;
    }
}
