package com.weirddev.testme.intellij

import com.weirddev.testme.intellij.icon.IconTokensReplacerImpl
import com.weirddev.testme.intellij.icon.IconizedLabel
import com.weirddev.testme.intellij.icon.Icons
import org.junit.Test

/**
 * Date: 10/15/2016
 * @author Yaron Yamin
 */
class IconTokensReplacerTest {
    private String text = "TestMe w/ <JUnit4>JUnit4 & <Mockito>Mockito"
    private String textTokenLast = "with JUnit5<JUnit5> & Mockito<Mockito>"
    private String textMultipleTokens = "with JUnit5<JUnit5> & Mockito<Mockito>& Mockito<Mockito>"
    @Test
    void testStripTokens() {
      assert "TestMe w/ JUnit4 & Mockito" == new IconTokensReplacerImpl().stripTokens(text)
    }
    @Test
    void testStripLastToken() {
      assert "with JUnit5 & Mockito" == new IconTokensReplacerImpl().stripTokens(textTokenLast)
    }
    @Test
    void testTokenize() {
        assert [new IconizedLabel("TestMe w/ ", Icons.TEST_ME,Icons.TEST_ME), new IconizedLabel("JUnit4 & ", Icons.JUNIT4, Icons.JUNIT4_DARK), new IconizedLabel("Mockito", Icons.MOCKITO, Icons.MOCKITO)] == test(text)
    }
    @Test
    void testTokenizeLast() {
        assert [new IconizedLabel("with JUnit5", Icons.TEST_ME,Icons.TEST_ME), new IconizedLabel(" & Mockito", Icons.JUNIT5, Icons.JUNIT5), new IconizedLabel("", Icons.MOCKITO, Icons.MOCKITO)] == test(textTokenLast)
    }
    @Test
    void testMultipleTokens() {
        assert [new IconizedLabel("with JUnit5", Icons.TEST_ME,Icons.TEST_ME), new IconizedLabel(" & Mockito", Icons.JUNIT5, Icons.JUNIT5), new IconizedLabel("& Mockito", Icons.MOCKITO, Icons.MOCKITO), new IconizedLabel("", Icons.MOCKITO, Icons.MOCKITO)] == test(textMultipleTokens)
    }

    private ArrayList<IconizedLabel> test(String text) {
        new IconTokensReplacerImpl().tokenize(text, Icons.TEST_ME)
    }
}
