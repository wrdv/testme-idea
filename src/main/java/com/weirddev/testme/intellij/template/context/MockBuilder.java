package com.weirddev.testme.intellij.template.context;

/***
 *  mock builder interface
 *
 * @author huangliang
 */
public interface MockBuilder {

    /**
     * true - field can be mocked
     */
    boolean isMockable(Field field, Type testedClass);
}
