package com.weirddev.testme.intellij.template.context;

import java.util.List;

/**
 * Date: 31/03/2017
 * @author Yaron Yamin
 */
@SuppressWarnings("unused")
public class  MockitoUtils {
    public static boolean isMockable(Field field) {
        return !field.getType().isPrimitive() && !field.isFinalType() && !field.isOverridden() && !field.getType().isArray() && !field.getType().isEnum();
    }
    public static boolean hasMockable(List<Field> fields) {
        for (Field field : fields) {
            if (isMockable(field)) {
                return true;
            }
        }
        return false;
    }

    public static String getImmockabiliyReason(String prefix,Field field) {
        final String reasonMsgPrefix = prefix+"Field " + field.getName() + " of type " + field.getType().getName();
        if (field.isFinalType() && isMockExpected(field)) {
            return reasonMsgPrefix + " - was not mocked since Mockito doesn't mock a Final class";
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
    public static boolean isMockExpected(Field field) {
        return !field.getType().isPrimitive() && !"java.lang.String".equals(field.getType().getCanonicalName()) && !field.isStatic() && !field.isOverridden();
    }
}

