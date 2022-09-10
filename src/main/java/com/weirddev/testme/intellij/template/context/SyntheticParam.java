package com.weirddev.testme.intellij.template.context;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Represents a synthetic parameter. not coded explicitly.
 *
 * Date: 25/02/2017
 * @author Yaron Yamin
 */
public class SyntheticParam extends Param {

    @Nullable
    private final UsageContext usageContext;

    public SyntheticParam(Type type, String name, UsageContext usageContext) {
        super(type, name,new ArrayList<Field>());
        this.usageContext = usageContext;
    }
    public SyntheticParam(Type type, String name) {
        super(type, name,new ArrayList<Field>());
        this.usageContext = null;
    }

    /**
     *
     * @return true when represents a bean property
     */
    public enum UsageContext {
        Property, Generic
    }

    public UsageContext getUsageContext() {
        return usageContext;
    }
}
