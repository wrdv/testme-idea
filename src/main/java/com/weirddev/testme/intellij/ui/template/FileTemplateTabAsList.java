
package com.weirddev.testme.intellij.ui.template;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ui.ListSpeedSearch;
import com.intellij.ui.components.JBList;
import com.intellij.util.Function;
import com.intellij.util.ui.UIUtil;
import com.weirddev.testme.intellij.icon.TemplateNameFormatter;
import com.weirddev.testme.intellij.ui.model.TestMeFileTemplate;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

abstract class FileTemplateTabAsList extends FileTemplateTab {
  private final JList<FileTemplate> myList = new JBList<>();
  private MyListModel myModel;
  private TemplateNameFormatter templateNameFormatter = new TemplateNameFormatter();

  FileTemplateTabAsList(String title) {
    super(title);
    myList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    myList.setCellRenderer(new MyListCellRenderer());
    myList.addListSelectionListener(__ -> onTemplateSelected());
    new ListSpeedSearch(myList, (Function<Object, String>)o -> {
      if (o instanceof FileTemplate) {
        return ((FileTemplate)o).getName();
      }
      return null;
    });
  }

  private class MyListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, false);
      Icon icon = null;
      if (value instanceof FileTemplate) {
        FileTemplate template = (FileTemplate) value;
        icon = FileTemplateUtil.getIcon(template);
        String name = template instanceof TestMeFileTemplate ? ((TestMeFileTemplate) template).getDisplayName() : template.getName();
        String formattedName = templateNameFormatter.formatWithInnerImages(name);
        if (TestTemplatesConfigurable.isInternalTemplate(template)) {
          setFont(getFont().deriveFont(Font.BOLD));
        }
        else {
          setFont(getFont().deriveFont(Font.PLAIN));
        }
        setText(formattedName);
        if (!template.isDefault()) {
          if (!isSelected) {
            setForeground(MODIFIED_FOREGROUND);
          }
        }
      }
      setIcon(icon);
      if (isSelected) setBackground(UIUtil.getListSelectionBackground(cellHasFocus));
      return this;
    }
  }

  @Override
  public void removeSelected() {
    final FileTemplate selectedTemplate = getSelectedTemplate();
    if (selectedTemplate == null) {
      return;
    }
    final DefaultListModel model = (DefaultListModel) myList.getModel();
    final int selectedIndex = myList.getSelectedIndex();
    model.remove(selectedIndex);
    if (!model.isEmpty()) {
      myList.setSelectedIndex(Math.min(selectedIndex, model.size() - 1));
    }
    onTemplateSelected();
  }

  private static class MyListModel extends DefaultListModel<FileTemplate> {
    void fireListDataChanged() {
      int size = getSize();
      if (size > 0) {
        fireContentsChanged(this, 0, size - 1);
      }
    }
  }

  @Override
  protected void initSelection(FileTemplate selection) {
    myModel = new MyListModel();
    myList.setModel(myModel);
    for (FileTemplate template : myTemplates) {
      myModel.addElement(template);
    }
    if (selection != null) {
      selectTemplate(selection);
    }
    else if (myList.getModel().getSize() > 0) {
      myList.setSelectedIndex(0);
    }
  }

  @Override
  public void fireDataChanged() {
    myModel.fireListDataChanged();
  }

  @Override
  @NotNull
  public FileTemplate[] getTemplates() {
    final int size = myModel.getSize(); // todo fix
    List<FileTemplate> templates = new ArrayList<>(size);
    for (int i =0; i<size; i++) {
      templates.add(myModel.getElementAt(i));
    }
    return templates.toArray(FileTemplate.EMPTY_ARRAY);
  }

  @Override
  public void addTemplate(FileTemplate newTemplate) {
    myModel.addElement(newTemplate);
  }

  @Override
  public void selectTemplate(FileTemplate template) {
    myList.setSelectedValue(template, true);
  }

  @Override
  public FileTemplate getSelectedTemplate() {
    return myList.getSelectedValue();
  }

  @Override
  public JComponent getComponent() {
    return myList;
  }
}
