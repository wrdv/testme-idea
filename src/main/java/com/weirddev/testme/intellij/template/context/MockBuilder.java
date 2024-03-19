package com.weirddev.testme.intellij.template.context;

import java.util.List;

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

    /**
     * constructs an error message explaining why field cannot be mocked
     * @param prefix add prefix to message
     * @param field reported field
     * @return an error message explaining why field cannot be mocked
     */
    String getImmockabiliyReason(String prefix, Field field);

    /**
     * constructs mocked arguments expression
     * @param params method params being mocked
     * @param language String representation of test code {@link com.weirddev.testme.intellij.template.context.Language}
     * @return mocked arguments expression
     * @see Language
     */
    String buildMockArgsMatchers(List<Param> params, String language);

    /**
     *  @return true - if Field should be mocked
     */
    boolean isMockExpected(Field field);
}
