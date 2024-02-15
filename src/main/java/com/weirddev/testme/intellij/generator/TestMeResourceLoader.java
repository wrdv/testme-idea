package com.weirddev.testme.intellij.generator;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.weirddev.testme.intellij.ui.template.TestMeTemplateManager;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.util.ExtProperties;

import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;
import java.util.stream.Stream;

public class TestMeResourceLoader extends ResourceLoader {
    @Override
    public void init(ExtProperties extProperties) {
    }

    @Override
    public Reader getResourceReader(String source, String encoding) throws ResourceNotFoundException {
        TestMeTemplateManager fileTemplateManager = TestMeTemplateManager.getDefaultInstance();
        FileTemplate[] allPatterns = fileTemplateManager.getAllPatterns();
        Optional<FileTemplate> optTemplate = Stream.of(allPatterns).filter(t -> source.equals(t.getName() + "." + t.getExtension())).findAny();
        final FileTemplate include = optTemplate.orElseThrow(() -> new ResourceNotFoundException("Template not found: " + source));
        final String text = include.getText();
        return new StringReader(text);
    }
    @Override
    public boolean isSourceModified(Resource resource) {
        return true;
    }
    @Override
    public long getLastModified(Resource resource) {
        return 0L;
    }
//            @Override
//            public InputStream getResourceStream(String resourceName) throws ResourceNotFoundException {
//                TestMeTemplateManager fileTemplateManager = TestMeTemplateManager.getDefaultInstance();
//                FileTemplate[] allPatterns = fileTemplateManager.getAllPatterns();
//                Optional<FileTemplate> optTemplate = Stream.of(allPatterns).filter(t -> resourceName.equals(t.getName() + "." + t.getExtension())).findAny();
//                final FileTemplate include = optTemplate.orElseThrow(() -> new ResourceNotFoundException("Template not found: " + resourceName));
//                final String text = include.getText();
//                try {
//                    return new ByteArrayInputStream(text.getBytes(FileTemplate.ourEncoding));
//                } catch (UnsupportedEncodingException e) {
//                    throw new RuntimeException(e);
//                }
//            }
}
