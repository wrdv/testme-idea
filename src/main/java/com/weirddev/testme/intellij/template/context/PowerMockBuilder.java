package com.weirddev.testme.intellij.template.context;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerMockBuilder extends MockitoMockBuilder{
    /**
     * true when render internal method call option is opted-in on custom settings
     */
    private final boolean renderInternalMethodCallStubs;

    public PowerMockBuilder(boolean isMockitoMockMakerInlineOn, boolean stubMockMethodCallsReturnValues,
        TestSubjectInspector testSubjectInspector, @Nullable String mockitoCoreVersion,
        boolean renderInternalMethodCallStubs) {
        super(isMockitoMockMakerInlineOn, stubMockMethodCallsReturnValues, testSubjectInspector, mockitoCoreVersion);
        this.renderInternalMethodCallStubs = renderInternalMethodCallStubs;

    }

    /**
     *
     * @param method call method
     * @param testedClass the tested class
     * @return true - if the method is  class object self called method
     */
    public boolean isMethodOwnedByClass(Method method, Type testedClass) {
        List<Method> methods = testedClass.getMethods();
        return methods.stream().anyMatch(classMethod -> method.getMethodId().equals(classMethod.getMethodId()));
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

}
