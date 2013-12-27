package org.datafx.controller.context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract base class for all context classes. See {@link #ApplicationContext}
 * for more informations.
 *
 * @author hendrikebbers
 */
public class AbstractContext {

    private String id = UUID.randomUUID().toString();
    private Map<String, Object> registeredObjects;

    public AbstractContext() {
        registeredObjects = new HashMap<String, Object>();
    }

    /**
     * Returns the unique id of this context
     *
     * @return unique id
     */
    public String getId() {
        return id;
    }

    /**
     * Register a object to the context by using a key
     *
     * @param key   the key
     * @param value the object
     */
    public void register(String key, Object value) {
        registeredObjects.put(key, value);
    }

    /**
     * Returns a registered object that is registered in the context with the
     * given key
     *
     * @param key the key
     * @return the registered object
     */
    public Object getRegisteredObject(String key) {
        return registeredObjects.get(key);
    }

    /**
     * Returns a registered object that is registered by its' class
     *
     * @param cls the class of the object
     * @return the object
     */
    @SuppressWarnings("unchecked")
    public <T> T getRegisteredObject(Class<T> cls) {
        return (T) getRegisteredObject(cls.toString());
    }

    public void register(Object value) {
        register(value.getClass().toString(), value);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AbstractContext{");
        sb.append("id='").append(id).append('\'');
        sb.append(", registeredObjects=").append(registeredObjects);
        sb.append('}');
        return sb.toString();
    }
}
