package com.weirddev.testme.intellij.icon;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Date: 10/19/2016
 *
 * @author Yaron Yamin
 */
public interface IconTokensReplacer {
        ArrayList<IconizedLabel> tokenize(String text, Icon icon);
}
