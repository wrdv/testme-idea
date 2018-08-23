package com.weirddev.testme.intellij.icon;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Date: 25/12/2016
 *
 * @author Yaron Yamin
 */
public class IconRegistry {
    private static Map<String, IconDescriptor> iconDescriptors = new HashMap<String, IconDescriptor>();
    static {
        add(new IconDescriptor("JUnit4", Icons.JUNIT4, Icons.JUNIT4_DARK));
        add(new IconDescriptor("JUnit5", Icons.JUNIT5,Icons.JUNIT5));
        add(new IconDescriptor("Mockito", Icons.MOCKITO,Icons.MOCKITO));
        add(new IconDescriptor("Groovy", Icons.GROOVY,Icons.GROOVY));
        add(new IconDescriptor("Scala", Icons.SCALA,Icons.SCALA));
        add(new IconDescriptor("TestNG", Icons.TESTNG,Icons.TESTNG));
    }

    private static IconDescriptor add(IconDescriptor iconDescriptor) {
        return iconDescriptors.put(iconDescriptor.getId(), iconDescriptor);
    }

    public static IconDescriptor get(String id){
        return iconDescriptors.get(id);
    }
    public static Set<String> getIds(){
        return iconDescriptors.keySet();
    }
    public static class IconDescriptor{
        String id;
        Icon icon;
        Icon darkIcon;

        public IconDescriptor(String id, Icon icon, Icon darkIcon) {
            this.id = id;
            this.icon = icon;
            this.darkIcon = darkIcon;
        }

        public String getId() {
            return id;
        }

        public Icon getIcon() {
            return icon;
        }

        public Icon getDarkIcon() {
            return darkIcon;
        }
    }
}
