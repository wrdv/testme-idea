package com.weirddev.testme.intellij.icon

import spock.lang.*

/**
 * @author : Yaron Yamin
 * @since : 6/20/20
 * */
class TemplateNameFormatterTest extends Specification {

    @Unroll
    def "replace Html where html=#html then expect: #expectedResult"() {
        expect:
        expectedResult == new TemplateNameFormatter().formatWithInnerImages(html)

        //todo add TC's

        where:
        html   || expectedResult
        "<html><i>JUnit4</i><img src='/icons/junit.png'>& <i>Mockito</i><img src='/icons/mockito.png'></html>" || "<html><i>JUnit4</i><img src='"+TemplateIcons.class.getResource("/icons/junit.png")+"'>& <i>Mockito</i><img src='"+TemplateIcons.class.getResource("/icons/mockito.png")+"'></html>"
    }

    @Unroll
    def "add prefix to html content where html=#html then expect: #expectedResult"() {
        expect:
        expectedResult == new TemplateNameFormatter().formatClonedName(html, "Copy of ")

        where:
        html   || expectedResult
        "<html><i>JUnit4</i><img src='/icons/junit.png'>& <i>Mockito</i><img src='/icons/mockito.png'></html>" || "<html>Copy of <i>JUnit4</i><img src='/icons/junit.png'>& <i>Mockito</i><img src='/icons/mockito.png'></html>"
        "JUnit4 & Mockito" || "Copy of JUnit4 & Mockito"
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme