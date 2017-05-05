package com.weirddev.testme.intellij.action

import org.junit.Test

/**
 * Date: 05/05/2017
 *
 * @author Yaron Yamin
 */
class GotoTestOrCodeActionExtTest {
    @Test
    void testGetHandler() throws Exception {
        assert new GotoTestOrCodeActionExt().getHandler() instanceof GotoTestOrCodeHandlerExt
    }
}
