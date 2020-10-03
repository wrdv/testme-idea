package com.weirddev.testme.intellij.icon;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Yaron Yamin
 * @since : 6/22/20
 **/
public class TemplateNameFormatter {
    private static final String HTML_OPEN_TAG_REGEX = "<\\s*[hH][tT][mM][lL]\\s*>";
    private static final String TAG_REGEX = "<.*?>";
    private static final String CLOSE_TAG_REGEX = "<.*>";
    private static final String HTML_CLOSE_TAG_REGEX = "</\\s*[hH][tT][mM][lL]\\s*>";
    private static final Pattern HTML_OPEN_TAG_PATTERN = Pattern.compile(HTML_OPEN_TAG_REGEX);
    private static final Pattern IMG_SRC_PATTERN = Pattern.compile("(<\\s*[iI][mM][gG]\\s*src\\s*=\\s*['\"])([^\"']+)(['\"]\\s*[//]?>)");
    private static final Pattern HTML_PATTERN = Pattern.compile("(" + HTML_OPEN_TAG_REGEX + ")(.*)(" + HTML_CLOSE_TAG_REGEX + ")");

    public String formatWithInnerImages(String html){
        String replacedHtml = html;
        Matcher matcher = IMG_SRC_PATTERN.matcher(html);
        while (matcher.find()) {
            String group = matcher.group(2);
            URL resource = TemplateIcons.class.getResource(group);
            replacedHtml = replacedHtml.replaceFirst(group, resource.toString());
        }
        return replacedHtml;
    }
    public String formatClonedName(String html, String prefix){
        Matcher matcher = HTML_OPEN_TAG_PATTERN.matcher(html);
        int startIndex = matcher.find() ? matcher.end(): 0;
        return  html.substring(0, startIndex) + prefix + html.substring(startIndex, html.length());

    }

    public String sanitizeHtml(String name) {
        return name == null ? null : name.replaceAll(TAG_REGEX," ");
    }
//    public List<IconizedLabel> formatAsLabels(String html){
//        if (!hmlRegEx.matcher(html).matches() || !imgSrcRegEx.matcher(html).matches()) {
//            return Collections.singletonList(new IconizedLabel(html, null, null));
//        }
//        String htmlContent = hmlRegEx.matcher(html).group(2);
//
////remove surrounding html for each img group
////        String replacedHtml = html;
//
//        Matcher matcher = imgSrcRegEx.matcher(htmlContent);
//        while (matcher.find()) {
//            String group = matcher.group(2);
//            URL resource = TemplateIcons.class.getResource(group);
//            if (resource!=null) {
//                Icon icon = IconLoader.getIcon(group);
//                if (icon != null) {
//                    new IconizedLabel()
//                }
//            }
//            //if exists convert to icon ( use cached icons?)
//            replacedHtml = replacedHtml.replaceFirst(group, resource.toString());
//        }
//        return replacedHtml;
//    }


}
