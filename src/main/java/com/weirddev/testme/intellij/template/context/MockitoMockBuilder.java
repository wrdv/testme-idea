package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.generator.TestBuilderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for Building string constructs of Mockito mock expressions.
 * Date: 31/03/2017
 * @author Yaron Yamin
 */

public class  MockitoMockBuilder implements MockBuilder{
    private static final Logger LOG = Logger.getInstance(MockitoMockBuilder.class.getName());
    public static final Map<String, String> TYPE_TO_ARG_MATCHERS;
    private static final Pattern SEMVER_PATTERN = Pattern.compile("^(\\d*)\\.(\\d*)\\.*");

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
    private final boolean isMockitoMockMakerInlineOn;
    private final boolean stubMockMethodCallsReturnValues;
    protected final TestSubjectInspector testSubjectInspector;
    @Nullable
    private final String mockitoCoreVersion;
    private final Integer mockitoCoreMajorVersion;
    private final Integer mockitoCoreMinorVersion;

    public MockitoMockBuilder(boolean isMockitoMockMakerInlineOn, boolean stubMockMethodCallsReturnValues, TestSubjectInspector testSubjectInspector, @Nullable String mockitoCoreVersion) {
        this.isMockitoMockMakerInlineOn = isMockitoMockMakerInlineOn;
        this.stubMockMethodCallsReturnValues = stubMockMethodCallsReturnValues;
        this.testSubjectInspector = testSubjectInspector;
        this.mockitoCoreVersion = mockitoCoreVersion;
        if (mockitoCoreVersion != null) {
            Matcher matcher = SEMVER_PATTERN.matcher(mockitoCoreVersion);
            if (matcher.find()) {
                this.mockitoCoreMajorVersion = safeParseInteger(matcher.group(1));
                this.mockitoCoreMinorVersion = safeParseInteger(matcher.group(2));
                return;
            }
        }
        this.mockitoCoreMajorVersion = null;
        this.mockitoCoreMinorVersion = null;

    }

    private Integer safeParseInteger(String intStr) {
        if (intStr != null) {
            try {
                return Integer.parseInt(intStr);
            }
            catch (Exception ignore){}
        }
        return null;
    }

    /**
     * true - field can be mocked
     */
    @SuppressWarnings("unused")
    @Deprecated
    public boolean isMockable(Field field) {
        return isMockable(field, null);
    }

    /**
     * true - field can be mocked
     */
    @SuppressWarnings("unused")
    @Override
    public boolean isMockable(Field field, Type testedClass) {
        final boolean isMockable = !field.getType().isPrimitive() && !isWrapperType(field.getType())
            && (!field.getType().isFinal() || isMockitoMockMakerInlineOn) && !field.isOverridden()
            && !field.getType().isArray() && !field.getType().isEnum()
            && !testSubjectInspector.isNotInjectedInDiClass(field, testedClass);
        LOG.debug("field " + field.getType().getCanonicalName() + " " + field.getName() + " is mockable:" + isMockable);
        return isMockable;
    }

    /**
     * true - if argument can be mocked given the provided default types
     */
    @SuppressWarnings("unused")
    public boolean isMockable(Param param, Map<String,String> defaultTypes) {
        final Type type = param.getType();
        final boolean isMockable = !type.isPrimitive() && !TestBuilderUtil.isStringType(type.getCanonicalName()) && !isWrapperType(type) && (!type.isFinal() || isMockitoMockMakerInlineOn) && !type.isArray() && !type.isEnum() &&  defaultTypes.get(type.getCanonicalName()) == null;
        LOG.debug("param "+ type.getCanonicalName()+" "+param.getName()+" is mockable:"+isMockable);
        return isMockable;
    }

    /**
     * true - if any given field can be mocked
     */
    @SuppressWarnings("unused")
    public boolean hasMockable(List<Field> fields, Type testedClass) {
        for (Field field : fields) {
            if (isMockable(field, testedClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param testedClass the tested class
     * @return true - if the tested class has mockable field
     */
    public boolean hasMocks(Type testedClass) {
        return testSubjectInspector.hasAccessibleCtor(testedClass) && hasMockable(testedClass.getFields(), testedClass);
    }

    /**
     * true - if method had any argument that can be mocked given the provided default types
     */
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

    /**
     * constructs an error message explaining why field cannot be mocked
     * @param prefix add prefix to message
     * @param field reported field
     * @return an error message explaining why field cannot be mocked
     */
    @SuppressWarnings("unused")
    public String getImmockabiliyReason(String prefix,Field field) {//consider deprecating when supporting explicit type initialization
        final String reasonMsgPrefix = prefix+"Field " + field.getName() + " of type " + field.getType().getName();
        if (field.getType().isFinal() && !isMockitoMockMakerInlineOn && isMockExpected(field)) {
            return reasonMsgPrefix + " - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set";
        } else if (field.getType().isArray()) {
            return reasonMsgPrefix + "[] - was not mocked since Mockito doesn't mock arrays";
//        } else if (field.getType().isEnum()) { //msg makes sense only when test subject is not an enum. can't test such condition since test subject not being passed.
//            return reasonMsgPrefix + " - was not mocked since Mockito doesn't mock enums";
        } else {
            return "";
        }
    }

    /**
     * constructs mocked arguments expression
     * @param params method params being mocked
     * @param language String representation of test code {@link com.weirddev.testme.intellij.template.context.Language}
     * @return mocked arguments expression
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

    /**
     * resolves relevant mock matcher from param
     * @param param being mocked
     * @param language test code language
     * @return relevant mock matcher for param
     */
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
    @Deprecated
    public boolean shouldStub(Method testMethod, List<Field> testedClassFields) {
        return callsMockMethod(testMethod, testedClassFields, Method::hasReturn, null);
    }


    /**
     * true - if should stub tested method
     * @param testMethod method being tested
     * @param testedClass tested class
     */
    @SuppressWarnings("unused")
    public boolean shouldStub(Method testMethod, Type testedClass) {
        return callsMockMethod(testMethod, testedClass.getFields(), Method::hasReturn, testedClass);
    }

    /**
     * true - if should verify tested method
     * @param testMethod method being tested
     * @param testedClassFields tested class fields
     */
    @Deprecated
    @SuppressWarnings("unused")
    public boolean shouldVerify(Method testMethod, List<Field> testedClassFields) {
        return callsMockMethod(testMethod, testedClassFields, method -> !method.hasReturn(), null);
    }

    /**
     * true - if should verify tested method
     * @param testMethod method being tested
     * @param testedClass tested class
     */
    @SuppressWarnings("unused")
    public boolean shouldVerify(Method testMethod, Type testedClass) {
        return callsMockMethod(testMethod, testedClass.getFields(), method -> !method.hasReturn(), testedClass);
    }

    private boolean callsMockMethod(Method testMethod, List<Field> testedClassFields,
        Predicate<Method> mockMethodRelevant, Type testedClass) {
        boolean callsMockMethod = false;
        if (!stubMockMethodCallsReturnValues) {
            LOG.debug("method " + testMethod.getMethodId() + " is calling a mock method:" + callsMockMethod);
            return callsMockMethod;
        }
        for (Field testedClassField : testedClassFields) {
            if (!isMockable(testedClassField, testedClass)) {
                continue;
            }
            LOG.debug("field " + testedClassField.getName() + " type " + testedClassField.getType().getCanonicalName()
                + " type methods:" + testedClassField.getType().getMethods().size());
            for (Method fieldMethod : testedClassField.getType().getMethods()) {
                if (mockMethodRelevant.test(fieldMethod)
                    && testSubjectInspector.isMethodCalled(fieldMethod, testMethod)) {
                    return true;
                }
            }
        }
        LOG.debug("method " + testMethod.getMethodId() + " is calling a mock method:" + callsMockMethod);
        return callsMockMethod;
    }

    /**
     * true - if tested method should be stubbed
     * @param testMethod - method being tested
     * @param ctor - constructor of method return type
     * @param defaultTypes - default type values
     */
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
                    if (method.getReturnType() != null && !"void".equals(method.getReturnType().getCanonicalName()) && testSubjectInspector.isMethodCalled(method, testMethod)) {
                        shouldStub = true;
                        break;
                    }
                }
            }
        }
        LOG.debug("method "+testMethod.getMethodId()+" should be stabbed:"+shouldStub);
        return shouldStub;
    }
/*

    public boolean hasStubsReturningScalaFuture(Type testedClass, Map<String,String> defaultTypes) { //todo - probably will not be needed and can be removed
        Method ctor = testSubjectInspector.findOptimalConstructor(testedClass);
        if (ctor == null) {
            return false;
        }
        for (Method method : testedClass.getMethods()) {
            if (method.isTestable()) {
                for (Param param : ctor.getMethodParams()) {
                    isMockable(param, defaultTypes);
                    for (Method methodOfDependency : param.getType().getMethods()) {
                        if (testSubjectInspector.isMethodCalled(methodOfDependency, method) && methodOfDependency.getReturnType() != null && TestSubjectInspector.isScalaFuture(methodOfDependency.getReturnType())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
*/

    /**
      *  @return true - if Field should be mocked
      */
    public boolean isMockExpected(Field field) {
        return !field.getType().isPrimitive() && !isWrapperType(field.getType()) && !field.isStatic() && !field.isOverridden();
    }

    /**
     * true - if type is a wrapper for other type, such as a primitive
     */
    public boolean isWrapperType(Type type) {
        return WRAPPER_TYPES.contains(type.getCanonicalName());
    }

    /**
     * Mockito core version. in case mockito-core jar can be found on the target test module classpath. null otherwise
     */
    @Nullable
    public String getMockitoCoreVersion() {
        return mockitoCoreVersion;
    }

    /**
     * @return Mockito init mocks method name. typically "initMocks" or "openMocks" depending on Mockito version
     */
    public String getInitMocksMethod() {
        if (mockitoCoreMajorVersion != null && mockitoCoreMinorVersion != null && ((mockitoCoreMajorVersion == 3  && mockitoCoreMinorVersion >= 4) || mockitoCoreMajorVersion > 3) ) {
            return "openMocks";
        }
        else {
            return "initMocks";
        }
    }
}
