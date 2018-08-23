package com.intellij.openapi.help;

/**
 * Date: 21/08/2018
 *
 * @author Yaron Yamin
 * A stub class to support forward compatibility with IDEA2017.3+
 */
public abstract class WebHelpProvider {

    String getHelpPageUrl(String helpTopicId) {
        return null;
    }

    String getHelpTopicPrefix() {
        return null;
    }
}
