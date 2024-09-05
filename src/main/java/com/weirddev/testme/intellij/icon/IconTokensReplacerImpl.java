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
        for (String id : IconRegistry.getIds()) {
            for (int i = 0; i < iconizedLabels.size(); i++) {
                final String subText = iconizedLabels.get(i).getText();
                final String token = String.format(TOKEN_TEMPLATE, id);
                String[] subTexts = subText.split(token);
                for(int j=0;j<subTexts.length;j++) {
                    if (j == 0) {
                        iconizedLabels.get(i + j).setText(subTexts[j]);
                    } else {
                        iconizedLabels.add(i+j,new IconizedLabel(subTexts[j], IconRegistry.get(id).getIcon(), IconRegistry.get(id).getDarkIcon()));
                    }
                }
                if (subText.endsWith(token)) {
                    iconizedLabels.add(i+subTexts.length,new IconizedLabel("", IconRegistry.get(id).getIcon(), IconRegistry.get(id).getDarkIcon()));
                }
            }
        }
        return iconizedLabels;
    }
}
