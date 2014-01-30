package org.datafx.util;

import java.net.URL;

public class DataFXUtils {

    public static boolean canAccess(Class<?> controllerClass, String resourceName) {
        try {
            URL url = controllerClass.getResource(resourceName);
            if (url == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
        }
        return false;
    }
}
