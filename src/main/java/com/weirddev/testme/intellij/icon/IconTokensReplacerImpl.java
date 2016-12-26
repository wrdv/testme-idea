package com.weirddev.testme.intellij.icon;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Date: 10/15/2016
 *
 * @author Yaron Yamin
 */
public class IconTokensReplacerImpl implements IconTokensReplacer {
    private static final String TOKEN_TEMPLATE = "<%s>";

    public static String stripTokens(String text) {
        String regex = "";
        for (String token : IconRegistry.getIds()) {
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
        iconizedLabels.add(new IconizedLabel(text, icon, icon));
        for (String token : IconRegistry.getIds()) {
            for (int i = 0; i < iconizedLabels.size(); i++) {
                String[] subTexts = iconizedLabels.get(i).getText().split(String.format(TOKEN_TEMPLATE, token));
                if (subTexts.length > 1) {
                    iconizedLabels.get(i).setText(subTexts[0]);
                    iconizedLabels.add(i+1,new IconizedLabel(subTexts[1], IconRegistry.get(token).getIcon(), IconRegistry.get(token).getDarkIcon()));
                }
            }
        }
        return iconizedLabels;
    }
}
