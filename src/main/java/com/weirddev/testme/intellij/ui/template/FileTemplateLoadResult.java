package com.weirddev.testme.intellij.ui.template;

import com.intellij.util.containers.MultiMap;
import com.intellij.ide.fileTemplates.impl.DefaultTemplate;
import java.net.URL;

/**
 * @author : Yaron Yamin
 * @since : 5/29/20
 **/
public class FileTemplateLoadResult {
    private MultiMap<String, DefaultTemplate> result;
    private URL defaultTemplateDescription;
    private URL  defaultIncludeDescription;

    public FileTemplateLoadResult(MultiMap<String, DefaultTemplate> result) {
        this.result = result;
    }

    public FileTemplateLoadResult() {
    }

    public FileTemplateLoadResult(MultiMap<String, DefaultTemplate> result, URL defaultTemplateDescription, URL defaultIncludeDescription) {
        this.result = result;
        this.defaultTemplateDescription = defaultTemplateDescription;
        this.defaultIncludeDescription = defaultIncludeDescription;
    }

    public MultiMap<String, DefaultTemplate> getResult() {
        return result;
    }

    public void setResult(MultiMap<String, DefaultTemplate> result) {
        this.result = result;
    }

    public URL getDefaultTemplateDescription() {
        return defaultTemplateDescription;
    }

    public void setDefaultTemplateDescription(URL defaultTemplateDescription) {
        this.defaultTemplateDescription = defaultTemplateDescription;
    }

    public URL getDefaultIncludeDescription() {
        return defaultIncludeDescription;
    }

    public void setDefaultIncludeDescription(URL defaultIncludeDescription) {
        this.defaultIncludeDescription = defaultIncludeDescription;
    }
}
