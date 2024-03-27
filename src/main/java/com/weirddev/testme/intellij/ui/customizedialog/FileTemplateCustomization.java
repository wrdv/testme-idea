package com.weirddev.testme.intellij.ui.customizedialog;

import java.util.List;

/**
 *  customization of user selections
 * @author huangliang
 */
public class FileTemplateCustomization {

    private final List<String> selectedFieldNameList;

    private final List<String> selectedMethodIdList;

    private final boolean openUserCheckDialog;

    public FileTemplateCustomization(List<String> selectedFieldNameList, List<String> selectedMethodIdList,
        boolean openUserCheckDialog) {
        this.selectedFieldNameList = selectedFieldNameList;
        this.selectedMethodIdList = selectedMethodIdList;
        this.openUserCheckDialog = openUserCheckDialog;
    }

    public List<String> getSelectedFieldNameList() {
        return selectedFieldNameList;
    }

    public List<String> getSelectedMethodIdList() {
        return selectedMethodIdList;
    }

    public boolean isOpenUserCheckDialog() {
        return openUserCheckDialog;
    }
}
