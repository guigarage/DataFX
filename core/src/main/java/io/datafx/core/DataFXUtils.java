package io.datafx.core;

import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * General util class
 */
public class DataFXUtils {

    /**
     * Checks if the resource / file can be accessed by the controller class
     * @param controllerClass the controller class
     * @param resourceName name / path of the resource
     * @return true if the resource can be accessed
     */
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

    /**
     * Set's a field by using <tt>AccessController.doPrivileged</tt>
     * @param field the field
     * @param bean  the bean
     * @param value the value
     */
    public static void setPrivileged(final Field field, final Object bean,
                                     final Object value) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            @Override
            public Void run() {
                boolean wasAccessible = field.isAccessible();
                try {
                    field.setAccessible(true);
                    field.set(bean, value);
                    return null; // return nothing...
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new IllegalStateException("Cannot set field: "
                            + field, ex);
                } finally {
                    field.setAccessible(wasAccessible);
                }
            }
        });
    }

    /**
     * Access a field by using <tt>AccessController.doPrivileged</tt>
     * @param field the field
     * @param bean the bean
     * @return the value
     */
    public static <T> T getPrivileged(final Field field, final Object bean) {
        return AccessController.doPrivileged(new PrivilegedAction<T>() {
            @Override
            public T run() {
                boolean wasAccessible = field.isAccessible();
                try {
                    field.setAccessible(true);
                    return (T) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new IllegalStateException("Cannot access field: "
                            + field, ex);
                } finally {
                    field.setAccessible(wasAccessible);
                }
            }
        });
    }

    public static <T> T callPrivileged(final Method method, final Object bean, Object... args) {
        return AccessController.doPrivileged(new PrivilegedAction<T>() {
            @Override
            public T run() {
                boolean wasAccessible = method.isAccessible();
                try {
                    method.setAccessible(true);
                    return (T) method.invoke(bean, args);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
                    throw new IllegalStateException("Cannot call Method: "
                            + method, ex);
                } finally {
                    method.setAccessible(wasAccessible);
                }
            }
        });
    }

    public static List<Field> getInheritedDeclaredFields(Class<?> type) {
        List<Field> result = new ArrayList<>();
        Class<?> i = type;
        while (i != null && i != Object.class) {
            result.addAll(Arrays.asList(i.getDeclaredFields()));
            i = i.getSuperclass();
        }
        return result;
    }

    public static List<Method> getInheritedDeclaredMethods(Class<?> type) {
        List<Method> result = new ArrayList<>();
        Class<?> i = type;
        while (i != null && i != Object.class) {
            result.addAll(Arrays.asList(i.getDeclaredMethods()));
            i = i.getSuperclass();
        }
        return result;
    }
}
