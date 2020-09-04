// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.weirddev.testme.intellij.ui.template;



import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

/**
 * @see com.intellij.ide.fileTemplates.impl.ExportableFileTemplateSettings
 */
@State(
  name = FileTemplateSettings.FILE_TEMPLATE_SETTINGS,
  storages = @Storage(FileTemplateSettings.EXPORTABLE_SETTINGS_FILE),
  additionalExportFile = "fileTemplates"
)
class ExportableFileTemplateSettings extends FileTemplateSettings {

  ExportableFileTemplateSettings() {
    super(null);
  }
}
