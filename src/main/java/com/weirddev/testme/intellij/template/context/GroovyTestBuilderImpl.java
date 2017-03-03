package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Date: 24/02/2017
 *
 * @author Yaron Yamin
 */
public class GroovyTestBuilderImpl extends JavaTestBuilderImpl {
    private static final Logger LOG = Logger.getInstance(GroovyTestBuilderImpl.class.getName());
    public GroovyTestBuilderImpl(int maxRecursionDepth) {
        super(maxRecursionDepth);
    }

    @Override
    protected void buildCallParam(Param param, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, StringBuilder testBuilder) {
        final Type type = param.getType();
        if (param instanceof SyntheticParam && ((SyntheticParam) param).isProperty) {
            testBuilder.append(param.getName()).append(" : ");
        }
        if (type.isArray()) {
            testBuilder.append("[");
        }
        buildJavaParam(param, replacementTypes, defaultTypeValues, recursionDepth, testBuilder);
        if (type.isArray()) {
            testBuilder.append("] as ").append(type.getCanonicalName()).append("[]");
        }
    }

    @Override
    protected void buildCtorParams(Type type, String typeName, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, boolean isReplaced, StringBuilder testBuilder) {
        LOG.debug("recursionDepth:"+recursionDepth+". maxRecursionDepth "+maxRecursionDepth);
        if (recursionDepth <= maxRecursionDepth && (typeName.equals(type.getName()) || typeName.equals(type.getCanonicalName()))) {
            if (isReplaced) {
                buildJavaCallParams(Collections.singletonList(new SyntheticParam(type, typeName, false)), replacementTypes, defaultTypeValues, recursionDepth, testBuilder);
            } else{
                final boolean hasEmptyConstructor = hasEmptyConstructor(type);
                for (Method method : type.getConstructors()) {
                    if (isValidNonEmptyConstructor(type, method,hasEmptyConstructor, replacementTypes)) {
                        buildJavaCallParams(method.getMethodParams(), replacementTypes, defaultTypeValues, recursionDepth, testBuilder);
                        return;
                    }
                }
                List<SyntheticParam> syntheticParams = findProperties(type);
                if (syntheticParams.size() > 0) {
                    buildJavaCallParams(syntheticParams, replacementTypes, defaultTypeValues, recursionDepth, testBuilder);
                }
            }
        }
    }

    @NotNull
    private List<SyntheticParam> findProperties(Type type) {
        final List<Method> methods = type.getMethods();
        List<SyntheticParam> syntheticParams=new ArrayList<SyntheticParam>();
        for (Method method : methods) {
            if (method.isSetter()&&  method.getMethodParams().size()>0 &&method.getPropertyName()!=null) {
                syntheticParams.add(new SyntheticParam(method.getMethodParams().get(0).getType(), method.getPropertyName(),true));
            }
        }
        return syntheticParams;
    }

}
