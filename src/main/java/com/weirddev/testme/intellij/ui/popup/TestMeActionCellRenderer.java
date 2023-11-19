package com.weirddev.testme.intellij.ui.popup;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;
import com.weirddev.testme.intellij.action.TestMeAdditionalAction;
import com.weirddev.testme.intellij.icon.IconTokensReplacer;
import com.weirddev.testme.intellij.icon.IconTokensReplacerImpl;
import com.weirddev.testme.intellij.icon.IconizedLabel;
import com.weirddev.testme.intellij.icon.TemplateNameFormatter;
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
    private final IconTokensReplacer iconTokensReplacer;
    private final TemplateNameFormatter templateNameFormatter;

    public TestMeActionCellRenderer(TemplateNameFormatter templateNameFormatter, IconTokensReplacerImpl iconTokensReplacer) {
        this.templateNameFormatter = templateNameFormatter;
        this.iconTokensReplacer = iconTokensReplacer;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component result = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value != null) {
            if (value instanceof TestMeAdditionalAction) {
                TestMeAdditionalAction action = (TestMeAdditionalAction) value;
                JPanel jPanel = createPanel(list, isSelected);
                if (action.getTokenizedtext() != null) {
                    addFromTokenizedText(list, value, index, isSelected, cellHasFocus, result, action, jPanel);
                }
                else{
                    jPanel.add(createSubLabel(list, value, index, isSelected, cellHasFocus, new IconizedLabel( templateNameFormatter.formatWithInnerImages(action.getText()),null,null)));
                }
                return jPanel;
            }
            else  if(value instanceof ConfigurationLinkAction) {
                ConfigurationLinkAction action = (ConfigurationLinkAction) value;
                action.getText();
                JPanel jPanel = createPanel(list, isSelected);
                Component subLabel = createSubLabel(list, value, index, isSelected, cellHasFocus, new IconizedLabel(action.getText(), action.getIcon(), action.getIcon()));
                subLabel.setForeground(JBColor.DARK_GRAY);
                jPanel.add(subLabel);
                return jPanel;
            }
        }
        return result;
    }

    private void addFromTokenizedText(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus, Component result, TestMeAdditionalAction action, JPanel jPanel) {
        ArrayList<IconizedLabel> iconizedLabels = iconTokensReplacer.tokenize(action.getTokenizedtext(),action.getIcon());
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
        defaultListCellRenderer.setIcon(isSelected || !JBColor.isBright() ? iconizedLabel.getDarkIcon():iconizedLabel.getIcon());
        defaultListCellRenderer.setBorder(new EmptyBorder(0,0,0,0));
        return listCellRendererComponent;
    }
}
