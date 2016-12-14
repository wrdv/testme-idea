package com.weirddev.testme.intellij.generator;

/**
 * Date: 10/20/2016
 * @author Yaron Yamin
 */
public class TestMeGeneratorJunit4Test extends TestMeGeneratorTestBase{

    public TestMeGeneratorJunit4Test() {
        super("TestMe with JUnit4 & Mockito.java", "test");
    }

    public void testSimpleClass() throws Exception {
        doTest();
    }
    public void testDefaultPackage() throws Exception {
        doTest("", "Foo", "FooTest", true, false, true);
    }
    public void testVariousFieldTypes() throws Exception {
        doTest();
    }
    public void testWithSetters() throws Exception {
        doTest();
    }
    public void testConstructors() throws Exception {
        doTest(); //TODO template should initialize test subject directly with c'tor in this use case
    }
    public void testOverloading() throws Exception {
        doTest();
    }
    public void testNoFormatting() throws Exception {
        doTest(false, false, false);
    }
    public void testTypeNameCollision() throws Exception {
        doTest(false,false,true);
    }
    public void testTypeInDefaultPackageCollision() throws Exception {
        doTest("", "Foo", "FooTest", true, true, true);
    }
    public void testInheritance() throws Exception {
        doTest();
    }
    public void testGenerics() throws Exception {
        doTest(false, false, true);
    }
    public void testPrimitiveCallTypes() throws Exception {
        doTest(false, false, true);
    }
    public void testArrays() throws Exception {
        doTest(false, false, true);
    }
    public void testConstants() throws Exception {
        doTest();
    }
    public void testCollections() throws Exception {
        doTest(false, false, false);
    }
    public void testGenericsTypeCollision() throws Exception {
        doTest(false,false,true);
    }
    public void testEnum() throws Exception {
        doTest(false, false, true);
    }
    public void testStatic() throws Exception {
        doTest(false, false, true);
    }
    public void testDate() throws Exception {
        doTest(false, true, true);
    }
    public void testParamsConstructorsNoFqnReplacement() throws Exception {
        doTest(true, true, false);
    }
    public void testParamsConstructors() throws Exception {
        doTest(true, true, true);
    }
    //todo TC - use static init method when constructor not available. add default replacement for Class - Class.forName('?') (fqn of class under test?)

    // TODO assert caret position with <caret>

    // TODO TC different test target dir

}