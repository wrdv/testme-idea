package com.weirddev.testme.intellij.template.context;

import com.weirddev.testme.intellij.ui.customizedialog.FileTemplateCustomization;
import org.jetbrains.annotations.Nullable;

public class PowerMockBuilder extends MockitoMockBuilder{

    /**
     * true when render internal method call option is opted-in on custom settings
     */
    private final boolean renderInternalMethodCallStubs;

    public PowerMockBuilder(boolean isMockitoMockMakerInlineOn, boolean stubMockMethodCallsReturnValues,
        TestSubjectInspector testSubjectInspector, @Nullable String mockitoCoreVersion,
        boolean renderInternalMethodCallStubs, FileTemplateCustomization fileTemplateCustomization) {
        super(isMockitoMockMakerInlineOn, stubMockMethodCallsReturnValues, testSubjectInspector, mockitoCoreVersion, fileTemplateCustomization);
        this.renderInternalMethodCallStubs = renderInternalMethodCallStubs;
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
    protected boolean isMockableByMockFramework(Field field) {
        return true;
    }

}
