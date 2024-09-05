
package com.weirddev.testme.intellij.ui.template;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.impl.FileTemplateBase;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

abstract class FileTemplateTab {
  protected final List<FileTemplateBase> myTemplates = new ArrayList<>();
  private final String myTitle;
  protected static final Color MODIFIED_FOREGROUND = JBColor.BLUE;

  protected FileTemplateTab(String title) {
    myTitle = title;
  }

  public abstract JComponent getComponent();

  @Nullable
  public abstract FileTemplate getSelectedTemplate();

  public abstract void selectTemplate(FileTemplate template);

  public abstract void removeSelected();
  public abstract void onTemplateSelected();

  public void init(FileTemplate[] templates) {
    final FileTemplate oldSelection = getSelectedTemplate();
    final String oldSelectionName = oldSelection != null? ((FileTemplateBase)oldSelection).getQualifiedName() : null;

    myTemplates.clear();
    FileTemplate newSelection = null;
    for (FileTemplate original : templates) {
      final FileTemplateBase copy = (FileTemplateBase)original.clone();
      if (oldSelectionName != null && oldSelectionName.equals(copy.getQualifiedName())) {
        newSelection = copy;
      }
      myTemplates.add(copy);
    }
    initSelection(newSelection);
  }

  protected abstract void initSelection(FileTemplate selection);

  public abstract void fireDataChanged();

  @NotNull 
  public FileTemplate[] getTemplates() {
    return myTemplates.toArray(FileTemplate.EMPTY_ARRAY);
  }

  public abstract void addTemplate(FileTemplate newTemplate);

  public String getTitle() {
    return myTitle;
  }

}
