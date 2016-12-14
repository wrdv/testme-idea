package com.weirddev.testme.intellij.generator;

/**
 * Date: 13/12/2016
 *
 * @author Yaron Yamin
 */
public class TestMeGeneratorJunit5Test extends TestMeGeneratorTestBase {

    public TestMeGeneratorJunit5Test() {
        super("TestMe with JUnit5 & Mockito.java", "testJunit5");
    }

    public void testSimpleClass() throws Exception {
        doTest();
    }
    public void testVariousFieldTypes() throws Exception {
        doTest();
    }
    public void testArrays() throws Exception {
        doTest(false, false, true);
    }
}
