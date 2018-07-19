package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.generator.TestBuilderUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Date: 31/03/2017
 * @author Yaron Yamin
 */

public class  MockitoMockBuilder {
    private static final Logger LOG = Logger.getInstance(MockitoMockBuilder.class.getName());
    private static final Map<String, String> TYPE_TO_ARG_MATCHERS;

    static {
        TYPE_TO_ARG_MATCHERS = new HashMap<>();
        TYPE_TO_ARG_MATCHERS.put("byte", "anyByte");
        TYPE_TO_ARG_MATCHERS.put("short", "anyShort");
        TYPE_TO_ARG_MATCHERS.put("int", "anyInt");
        TYPE_TO_ARG_MATCHERS.put("long", "anyLong");
        TYPE_TO_ARG_MATCHERS.put("float", "anyFloat");
        TYPE_TO_ARG_MATCHERS.put("double", "anyDouble");
        TYPE_TO_ARG_MATCHERS.put("char", "anyChar");
        TYPE_TO_ARG_MATCHERS.put("boolean", "anyBoolean");

        TYPE_TO_ARG_MATCHERS.put("java.lang.Byte", "anyByte");
        TYPE_TO_ARG_MATCHERS.put("java.lang.Short", "anyShort");
        TYPE_TO_ARG_MATCHERS.put("java.lang.Integer", "anyInt");
        TYPE_TO_ARG_MATCHERS.put("java.lang.Long", "anyLong");
        TYPE_TO_ARG_MATCHERS.put("java.lang.Float", "anyFloat");
        TYPE_TO_ARG_MATCHERS.put("java.lang.Double", "anyDouble");
        TYPE_TO_ARG_MATCHERS.put("java.lang.Character", "anyChar");
        TYPE_TO_ARG_MATCHERS.put("java.lang.Boolean", "anyBoolean");
        TYPE_TO_ARG_MATCHERS.put("java.lang.String", "anyString");

        TYPE_TO_ARG_MATCHERS.put("scala.Byte", "anyByte");
        TYPE_TO_ARG_MATCHERS.put("scala.Short", "anyShort");
        TYPE_TO_ARG_MATCHERS.put("scala.Int", "anyInt");
        TYPE_TO_ARG_MATCHERS.put("scala.Long", "anyLong");
        TYPE_TO_ARG_MATCHERS.put("scala.Float", "anyFloat");
        TYPE_TO_ARG_MATCHERS.put("scala.Double", "anyDouble");
        TYPE_TO_ARG_MATCHERS.put("scala.Char", "anyChar");
        TYPE_TO_ARG_MATCHERS.put("scala.Boolean", "anyBoolean");
        TYPE_TO_ARG_MATCHERS.put("scala.Predef.String", "anyString");

    }

    private static final Set<String> WRAPPER_TYPES = new HashSet<>(Arrays.asList(
            Class.class.getCanonicalName(),
            Boolean.class.getCanonicalName(),
            Byte.class.getCanonicalName(),
            Short.class.getCanonicalName(),
            Character.class.getCanonicalName(),
            Integer.class.getCanonicalName(),
            Long.class.getCanonicalName(),
            Float.class.getCanonicalName(),
            Double.class.getCanonicalName(),
            String.class.getCanonicalName()));
    /**
     * true when mock-maker-inline option is opted-in on the target test module classpath
     */
    private boolean isMockitoMockMakerInlineOn;
    private boolean stubMockMethodCallsReturnValues;

    public MockitoMockBuilder(boolean isMockitoMockMakerInlineOn, boolean stubMockMethodCallsReturnValues) {
        this.isMockitoMockMakerInlineOn = isMockitoMockMakerInlineOn;
        this.stubMockMethodCallsReturnValues = stubMockMethodCallsReturnValues;
    }
    @SuppressWarnings("unused")
    public boolean isMockable(Field field) {
        final boolean isMockable = !field.getType().isPrimitive() && !isWrapperType(field.getType()) && (!field.getType().isFinal() || isMockitoMockMakerInlineOn) && !field.isOverridden() && !field.getType().isArray() && !field.getType().isEnum();
        LOG.debug("field "+field.getType().getCanonicalName()+" "+field.getName()+" is mockable:"+isMockable);
        return isMockable;
    }
    @SuppressWarnings("unused")
    public boolean isMockable(Param param, Map<String,String> defaultTypes) {
        final Type type = param.getType();
        final boolean isMockable = !type.isPrimitive() && !TestBuilderUtil.isStringType(type.getCanonicalName()) && !isWrapperType(type) && (!type.isFinal() || isMockitoMockMakerInlineOn) && !type.isArray() && !type.isEnum() &&  defaultTypes.get(type.getCanonicalName()) == null;
        LOG.debug("param "+ type.getCanonicalName()+" "+param.getName()+" is mockable:"+isMockable);
        return isMockable;
    }
    @SuppressWarnings("unused")
    public boolean hasMockable(List<Field> fields) {
        for (Field field : fields) {
            if (isMockable(field)) {
                return true;
            }
        }
        return false;
    }
    @SuppressWarnings("unused")
    public boolean hasMocks(Method ctor, Map<String,String> defaultTypes) {
        if (ctor == null) {
            return false;
        }
        List<Param> params = ctor.getMethodParams();
        for (Param param : params) {
            if (isMockable(param, defaultTypes)) {
                return true;
            }
        }
        return false;
    }
    @SuppressWarnings("unused")
    public String getImmockabiliyReason(String prefix,Field field) {
        final String reasonMsgPrefix = prefix+"Field " + field.getName() + " of type " + field.getType().getName();
        if (field.getType().isFinal() && !isMockitoMockMakerInlineOn && isMockExpected(field)) {
            return reasonMsgPrefix + " - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set";
        } else if (field.getType().isArray()) {
            return reasonMsgPrefix + "[] - was not mocked since Mockito doesn't mock arrays";
        } else if (field.getType().isEnum()) {
            return reasonMsgPrefix + " - was not mocked since Mockito doesn't mock enums";
        } else {
            return "";
        }
    }

    /**
     *
     * @param params
     * @param language String representation of com.weirddev.testme.intellij.template.context.Language
     * @return
     * @see Language
     */
    @SuppressWarnings("unused")
    public String buildMockArgsMatchers(List<Param> params,String language) {
        final StringBuilder sb = new StringBuilder();
        for (Param param : params) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(deductMatcherTypeMethod(param, Language.safeValueOf(language)));
        }
        return sb.toString();
    }

    @NotNull
    private String deductMatcherTypeMethod(Param param, Language language) {
        String matcherType;
        if (param.getType().isVarargs()) {
            matcherType = "anyVararg";
        }
        else {
            matcherType = TYPE_TO_ARG_MATCHERS.get(param.getType().getCanonicalName());
        }
        if (matcherType == null) {
            matcherType = "any";
        }
        //todo support anyCollection(),anyMap(),anySet() and consider arrays
        if (language != Language.Scala) {
            matcherType += "()";
        }
        return matcherType;
    }

    @SuppressWarnings("unused")
    public boolean shouldStub(Method testMethod, List<Field> testedClassFields) {
        boolean shouldStub = false;
        if (stubMockMethodCallsReturnValues) {
            for (Field testedClassField : testedClassFields) {
                if (isMockable(testedClassField)) {
                    LOG.debug("field "+testedClassField.getName()+" type "+testedClassField.getType().getCanonicalName()+" type methods:"+testedClassField.getType().getMethods().size());
                    for (Method fieldMethod : testedClassField.getType().getMethods()) {
                        if (fieldMethod.getReturnType() != null && !"void".equals(fieldMethod.getReturnType().getCanonicalName()) && TestSubjectUtils.isMethodCalled(fieldMethod, testMethod)) {
                            shouldStub = true;
                            break;
                        }
                    }
                }
            }
        }
        LOG.debug("method "+testMethod.getMethodId()+" should be stabbed:"+shouldStub);
        return shouldStub;
    }
    @SuppressWarnings("unused")
    public boolean shouldStub(Method testMethod, Method ctor, Map<String,String> defaultTypes) {
        boolean shouldStub = false;
        if (ctor == null || !stubMockMethodCallsReturnValues) {
            return false;
        }
        List<Param> ctorParams = ctor.getMethodParams();
        for (Param param : ctorParams) {
            if (isMockable(param, defaultTypes)) {
                LOG.debug("ctor param "+param.getName()+" type "+param.getType().getCanonicalName()+" type methods:"+param.getType().getMethods().size());
                for (Method method : param.getType().getMethods()) {
                    if (method.getReturnType() != null && !"void".equals(method.getReturnType().getCanonicalName()) && TestSubjectUtils.isMethodCalled(method, testMethod)) {
                        shouldStub = true;
                        break;
                    }
                }
            }
        }
        LOG.debug("method "+testMethod.getMethodId()+" should be stabbed:"+shouldStub);
        return shouldStub;
    }
    public boolean hasStubsReturningScalaFuture(Type testedClass, Map<String,String> defaultTypes) { //todo - probably will not be needed and can be removed
        Method ctor = TestSubjectUtils.findOptimalConstructor(testedClass);
        if (ctor == null) {
            return false;
        }
        for (Method method : testedClass.getMethods()) {
            if (method.isTestable()) {
                for (Param param : ctor.getMethodParams()) {
                    isMockable(param, defaultTypes);
                    for (Method methodOfDependency : param.getType().getMethods()) {
                        if (TestSubjectUtils.isMethodCalled(methodOfDependency, method) && methodOfDependency.getReturnType() != null && TestSubjectUtils.isScalaFuture(methodOfDependency.getReturnType())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
        @return true - if Field should be mocked
     */
    public boolean isMockExpected(Field field) {
        return !field.getType().isPrimitive() && !isWrapperType(field.getType()) && !field.isStatic() && !field.isOverridden();
    }

    private boolean isWrapperType(Type type) {
        return WRAPPER_TYPES.contains(type.getCanonicalName());
    }

}

