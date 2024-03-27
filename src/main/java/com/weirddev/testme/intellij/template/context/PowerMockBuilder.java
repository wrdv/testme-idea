package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.ui.customizedialog.FileTemplateCustomization;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerMockBuilder extends MockitoMockBuilder{

    private static final Logger LOG = Logger.getInstance(PowerMockBuilder.class.getName());
    
    /**
     * true when render internal method call option is opted-in on custom settings
     */
    private final boolean renderInternalMethodCallStubs;

    private final FileTemplateCustomization fileTemplateCustomization;

    public PowerMockBuilder(boolean isMockitoMockMakerInlineOn, boolean stubMockMethodCallsReturnValues,
        TestSubjectInspector testSubjectInspector, @Nullable String mockitoCoreVersion,
        boolean renderInternalMethodCallStubs, FileTemplateCustomization fileTemplateCustomization) {
        super(isMockitoMockMakerInlineOn, stubMockMethodCallsReturnValues, testSubjectInspector, mockitoCoreVersion, fileTemplateCustomization);
        this.renderInternalMethodCallStubs = renderInternalMethodCallStubs;
        this.fileTemplateCustomization = fileTemplateCustomization;
    }

    /**
     *
     * @param method tested method
     * @param testedClass tested class
     * @return true if method has class internal method call
     */
    public boolean hasInternalMethodCall(Method method, Type testedClass) {
        return renderInternalMethodCallStubs && method.getMethodCalls().stream().anyMatch(methodCall -> testedClass.getMethods().stream()
            .anyMatch(classMethod -> classMethod.getMethodId().equals(methodCall.getMethod().getMethodId())));
    }

    /**
     * true - field can be mocked
     */
    @Override
    public boolean isMockable(Field field, Type testedClass) {
        boolean openUserCheckDialog = fileTemplateCustomization.isOpenUserCheckDialog();
        boolean isMockable;
        if (openUserCheckDialog) {
            isMockable =  fileTemplateCustomization.getSelectedFieldNameList().contains(field.getName());
        } else {
            isMockable =  isMockableCommonChecks(field, testedClass);;
        }
        LOG.debug("field " + field.getType().getCanonicalName() + " " + field.getName() + " is mockable:" + isMockable);
        return isMockable;
    }

}
