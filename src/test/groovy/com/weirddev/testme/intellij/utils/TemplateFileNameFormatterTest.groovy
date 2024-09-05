package com.weirddev.testme.intellij.utils

import spock.lang.*

/**
 * @author : Yaron Yamin
 * @since : 9/9/20
 * */
class TemplateFileNameFormatterTest extends Specification {

    @Unroll
    def "template Name To File where name=#name then expect: #expectedResult"() {
        expect:
        TemplateFileNameFormatter.templateNameToFile(name) == expectedResult

        where:
        name   || expectedResult
        "<html>Copy of <i>Spock</i><img src='/icons/groovy.png'> & <i>Mockito</i><img src='/icons/mockito.png'></html>.groovy" || "%3Chtml%3ECopy+of+%3Ci%3ESpock%3C%2Fi%3E%3Cimg+src%3D%27%2Ficons%2Fgroovy.png%27%3E+%26+%3Ci%3EMockito%3C%2Fi%3E%3Cimg+src%3D%27%2Ficons%2Fmockito.png%27%3E%3C%2Fhtml%3E.groovy"
    }
    @Unroll
    def "file Name To template name where name=#name then expect: #expectedResult"() {
        expect:
        TemplateFileNameFormatter.filenameToTemplateName(name) == expectedResult

        where:
        name   || expectedResult
         "%3Chtml%3ECopy+of+%3Ci%3ESpock%3C%2Fi%3E%3Cimg+src%3D%27%2Ficons%2Fgroovy.png%27%3E+%26+%3Ci%3EMockito%3C%2Fi%3E%3Cimg+src%3D%27%2Ficons%2Fmockito.png%27%3E%3C%2Fhtml%3E.groovy" | "<html>Copy of <i>Spock</i><img src='/icons/groovy.png'> & <i>Mockito</i><img src='/icons/mockito.png'></html>.groovy"
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme