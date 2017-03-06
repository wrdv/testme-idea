package com.weirddev.testme.intellij.template.context;

import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.utils.Node;
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
    protected void buildCallParam(Param param, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, StringBuilder testBuilder, Node<Type> typeNode) {
        final Type type = param.getType();
        if (param instanceof SyntheticParam && ((SyntheticParam) param).isProperty) {
            testBuilder.append(param.getName()).append(" : ");
        }
        if (type.isArray()) {
            testBuilder.append("[");
        }
        buildJavaParam(param, replacementTypes, defaultTypeValues, recursionDepth, testBuilder, typeNode);
        if (type.isArray()) {
            testBuilder.append("] as ").append(type.getCanonicalName()).append("[]");
        }
    }

    @Override
    protected void buildCallParams(Type ownerType, List<? extends Param> params, Map<String, String> replacementTypes, Map<String, String> defaultTypeValues, int recursionDepth, StringBuilder testBuilder, Node<Type> typeNode) {
        if (params != null && params.size()>0) {
            super.buildCallParams(ownerType, params, replacementTypes, defaultTypeValues, recursionDepth, testBuilder, typeNode);
        } else if(ownerType!=null){
            List<SyntheticParam> syntheticParams = findProperties(ownerType);
            if (syntheticParams.size() > 0) {
                buildCallParams(ownerType, syntheticParams, replacementTypes, defaultTypeValues, recursionDepth, testBuilder, typeNode);
            }
        }
    }

    @NotNull
    private List<SyntheticParam> findProperties(Type type) {
        final List<Method> methods = type.getMethods();
        Map<String,SyntheticParam> syntheticParams=new LinkedHashMap<String,SyntheticParam>();
        for (Method method : methods) {
            if (method.isSetter()&&  method.getMethodParams().size()>0 &&method.getPropertyName()!=null) {
                final SyntheticParam syntheticParam = syntheticParams.get(method.getPropertyName());
                if (syntheticParam == null) {
                    syntheticParams.put(method.getPropertyName(),new SyntheticParam(method.getMethodParams().get(0).getType(), method.getPropertyName(),true));
                } else if (!syntheticParam.getName().equalsIgnoreCase(syntheticParam.getType().getName())) {//todo should be rejected based on actual target type vs param type rather than rely on naming convention (impl of com.intellij.psi.util.PropertyUtil#getFieldOfSetter - that works for groovy)
                    syntheticParams.remove(method.getPropertyName());
                    syntheticParams.put(method.getPropertyName(), new SyntheticParam(method.getMethodParams().get(0).getType(), method.getPropertyName(), true));
                }

            }
        }
        return new ArrayList<SyntheticParam>(syntheticParams.values());
    }
}
