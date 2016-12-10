package com.weirddev.testme.intellij.icon;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 10/15/2016
 *
 * @author Yaron Yamin
 */
public class IconTokensReplacerImpl implements IconTokensReplacer {
    static Map<String, Icon> token2Icon;
    private static final String TOKEN_TEMPLATE = "<%s>";
    static {
        token2Icon = new HashMap<String, Icon>();
        token2Icon.put("JUnit4",Icons.JUNIT4_DARK);
        token2Icon.put("JUnit5",Icons.JUNIT5);
        token2Icon.put("Mockito", Icons.MOCKITO);
    }

    public static String stripTokens(String text) {
        String regex = "";
        for (String token : token2Icon.keySet()) {
            if(!regex.isEmpty()){
                regex += "|";
            }
            regex += String.format(TOKEN_TEMPLATE, token);
        }
        return text.replaceAll(regex, "");
    }

    @Override
    public ArrayList<IconizedLabel> tokenize(String text, Icon icon) {
        ArrayList<IconizedLabel> iconizedLabels = new ArrayList<IconizedLabel>();
        iconizedLabels.add(new IconizedLabel(text, icon));
        for (String token : token2Icon.keySet()) {
            for (int i = 0; i < iconizedLabels.size(); i++) {
                String[] subTexts = iconizedLabels.get(i).getText().split(String.format(TOKEN_TEMPLATE, token));
                if (subTexts.length > 1) {
                    iconizedLabels.get(i).setText(subTexts[0]);
                    iconizedLabels.add(i+1,new IconizedLabel(subTexts[1], token2Icon.get(token)));
                }
            }
        }
        return iconizedLabels;
    }
}
