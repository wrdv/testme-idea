package com.weirddev.testme.intellij.generator;

import com.weirddev.testme.intellij.template.TemplateRegistry;

/**
 * Date: 10/20/2016
 * @author Yaron Yamin
 */
public class TestMeGeneratorJunit4Test extends TestMeGeneratorTestBase{

    public TestMeGeneratorJunit4Test() {
        super(TemplateRegistry.JUNIT4_MOCKITO_JAVA_TEMPLATE, "test");
    }

     TestMeGeneratorJunit4Test(String templateFilename, String testDirectory) {
        super(templateFilename, testDirectory);
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
        doTest(false, false, true);
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
    public void testMiscReplacementTypes() throws Exception {
        doTest(true, true, true);
    }
    public void testStaticFieldless() throws Exception {
        doTest(true, true, true);
    }
    public void testGroovy() throws Exception {
        doTest("com.example.services.impl", "Foovy", "FoovyTest", true, true, true);
    }

    //todo TC - use static init method when constructor not available

     // TODO TC different test target dir
}