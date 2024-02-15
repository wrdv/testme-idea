package com.weirddev.testme.intellij.builder;

import com.intellij.openapi.diagnostic.Logger;
import com.weirddev.testme.intellij.template.context.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Enrich com.weirddev.testme.intellij.template.context.Method#methodCalls with potentially relevant method calls
 * //todo consider refactoring. ideally should be resolved when Method created
 */
public class MethodReferencesBuilder {
    private static final Logger logger = Logger.getInstance(MethodReferencesBuilder.class.getName());
    public void resolveMethodReferences(int maxMethodCallsDepth, List<Method> methods) {
//              todo test generic methods and type params. use actual type params passed
        for (int i = 0; i < maxMethodCallsDepth; i++) {
            for (Method method : methods) {
                resolveMethodCalls(methods, method);
            }
        }
        for (Method method : methods) {
            resolveFieldsAffectedByCtor(method.getReturnType(),maxMethodCallsDepth);
        }
        List<Type> methodParamTypes = methods.stream().flatMap(method -> method.getMethodParams().stream().map(Param::getType)).toList();
        for (Type methodParamType : methodParamTypes) {
            resolveFieldsAffectedByCtor(methodParamType,maxMethodCallsDepth);
        }
        logger.debug("Resolved internal references in test template context");
    }

    private void resolveFieldsAffectedByCtor(Type type, int maxMethodCallsDepth) {//todo consider moving to test builder
        if (maxMethodCallsDepth < 1) {
            return;
        }
        if (isValidObject(type)) {
            for (Method ctor : type.findConstructors()) {
                Set<Field> affectedFields = new HashSet<Field>();
                for (MethodCall methodCall : ctor.getMethodCalls()) {
                    for (Param param : methodCall.getMethod().getMethodParams()) {
                        for (Field assignedToField : param.getAssignedToFields()) {
                            if (assignedToField.getOwnerClassCanonicalName().equals(ctor.getOwnerClassCanonicalType())) {
                                affectedFields.add(assignedToField);
                            }
                        }
                        resolveFieldsAffectedByCtor(param.getType(), maxMethodCallsDepth--);
                    }
                }
                ctor.getIndirectlyAffectedFields().addAll(affectedFields);
            }
        }
    }

    private boolean isValidObject(Type type) {
        return type != null && !type.isPrimitive() && !type.isArray() && !type.isInterface() && !type.isAbstract() && !type.isVarargs();
    }

    private void resolveMethodCalls(List<Method> methods, Method method) {
        final Set<MethodCall> calledMethodsByMethodCalls = new HashSet<MethodCall>();
//        final Set<MethodCall> methodsInMyFamilyTree= new HashSet<MethodCall>();
        for (MethodCall methodCall : method.getMethodCalls()) {
            final Method calledMethodFound = find(methods, methodCall.getMethod().getMethodId());//find originally resolved method since methods in resolved method call are resolved in a shallow manner
            if (calledMethodFound != null) {
                MethodCall methodCallFound;
                if (methodCall.getMethod() == calledMethodFound) {
                    methodCallFound = methodCall;
                } else {
                    methodCallFound = new MethodCall(calledMethodFound, methodCall.getMethodCallArguments());
                }
//                methodsInMyFamilyTree.add(methodCallFound);
                calledMethodsByMethodCalls.add(methodCallFound);
                if (method.getOwnerClassCanonicalType()!=null && method.getOwnerClassCanonicalType().equals(methodCallFound.getMethod().getOwnerClassCanonicalType())) {
                    calledMethodsByMethodCalls.addAll(calledMethodFound.getMethodCalls());
                }
            }
        }
        method.getMethodCalls().removeAll(calledMethodsByMethodCalls);
        method.getMethodCalls().addAll(calledMethodsByMethodCalls);
//        method.getCalledFamilyMembers().addAll(methodsInMyFamilyTree);
    }

    private Method find(List<Method> methods, String methodId) {
        for (Method method : methods) {
            if (method.getMethodId().equals(methodId)) {
                return method;
            }
            if (method.getReturnType() != null) {
                for (Method returnTypeMethod : method.getReturnType().getMethods()) {
                    if (returnTypeMethod.getMethodId().equals(methodId)) {
                        return returnTypeMethod;
                    }
                }

            }
        }
        return null;
    }

}
