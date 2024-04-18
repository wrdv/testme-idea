package com.weirddev.testme.intellij.template.context;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class Method.
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
@Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor @ToString(onlyExplicitlyIncluded = true)
public class Method {
    /**
     * formatted method id. a string used to uniquely discriminate this method from others
     */
    @EqualsAndHashCode.Include @ToString.Include
    private final String methodId;
    /**
     * method name
     */
    private final String name;
    /**
     * Method's return type
     */
    private final Type returnType;
    /**
     * method owner type cannonical name
     */
    private final String ownerClassCanonicalType;
    /**
     * method arguments
     */
    private final List<Param> methodParams;

    /**
     * method exception types
     */
    private final String methodExceptionTypes;
    /**
     * true - if method has private modifier
     */
    private final boolean isPrivate;
    /**
     * true - if method has protected modifier
     */
    private final boolean isProtected;
    /**
     * true - if method has default (package-private access modifier)
     */
    private final boolean isDefault;
    /**
     * true - if method has public modifier
     */
    private final boolean isPublic;
    /**
     * true - if method is abstract
     */
    private final boolean isAbstract;
    /**
     * true - if method defined as native
     */
    private final boolean isNative;
    /**
     * true - if this is a static method
     */
    private final boolean isStatic;
    /**
     * true - if method is a setter
     */
    private final boolean isSetter;
    /**
     * true - if method is a getter
     */
    private final boolean isGetter;
    /**
     * true - if method is a constructor
     */
    private final boolean constructor;
    /**
     * true - if method is overridden in child class
     */
    private final boolean overridden;
    /**
     * true - if method is inherited from parent class
     */
    private final boolean inherited;
    /**
     * true - if owner type is an interface
     */
    private final boolean isInInterface;
    /**
     * true -  if method is synthetically generated. common for scala methods
     */
    private final boolean isSynthetic;
    /**
     * the underlying field property name. relevant for getter/setter
     */
    private final String propertyName;
    /**
     *true - when accessible from class under test
     */
    private final boolean accessible;
    /**
     * true - is Primary Constructor (relevant for Scala)
     */
    private final boolean primaryConstructor;
    /**
     * true - method is accessible and can be relevant for unit testing
     */
    private final boolean testable;
    /**
     * methods called directly from this method
     */
    private final Set<MethodCall> directMethodCalls = new HashSet<>();
    /**
     * methods called directly from this method or on the call stack from this method via other methods belonging to the same type hierarchy
     */
    private final Set<MethodCall> methodCalls = new HashSet<>();
    /**
     * methods referenced from this method. i.e.  SomeClassName::someMethodName
     */
    private final Set<Method> methodReferences = new HashSet<>();
    /*
     *  method calls of methods in this owner's class type or one of it's ancestor type. including indirectly called methods up to max method call search depth. ResolvedMethodCall objects of the class under test are deeply resolved
     *  @deprecated not used. might be removed
     */
//   @Getter  private final Set<MethodCall> calledFamilyMembers=new HashSet<MethodCall>();
    /**
     * references included in this method's implementation
     */
    private final Set<Reference> internalReferences = new HashSet<>();
    /**
     *  Fields affected (assigned to) by methods called from this method. currently calculated only for constructors. i.e. when delegating to other constructors
     */
    private final Set<Field> indirectlyAffectedFields = new HashSet<>();

    /**
     *
     * true - if method has a return type and not void
     */
    public boolean hasReturn(){
        return returnType != null && !"void".equals(returnType.getName());
    }

    /**
     *
     * true - if method has parameters
     */
    public boolean hasParams() {
        return !methodParams.isEmpty();
    }

}

