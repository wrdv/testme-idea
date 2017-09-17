package com.weirddev.testme.intellij.template.context;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: 31/03/2017
 * @author Yaron Yamin
 */

public class  MockitoUtils {

    private static final Set<String> WRAPPER_TYPES = new HashSet<String>(Arrays.asList(
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

    public MockitoUtils(boolean isMockitoMockMakerInlineOn) {
        this.isMockitoMockMakerInlineOn = isMockitoMockMakerInlineOn;
    }

    public boolean isMockable(Field field) {
        return !field.getType().isPrimitive() && !isWrapperType(field) && (!field.getType().isFinal() || isMockitoMockMakerInlineOn) && !field.isOverridden() && !field.getType().isArray() && !field.getType().isEnum();
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
        @return true - if Field should be mocked
     */
    public boolean isMockExpected(Field field) {
        return !field.getType().isPrimitive() && !isWrapperType(field) && !field.isStatic() && !field.isOverridden();
    }

    public boolean isWrapperType(Field field) {
        return WRAPPER_TYPES.contains(field.getType().getCanonicalName());
    }

}

