package org.datafx.controller.context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract base class for all context classes. See {@link #ApplicationContext}
 * for more informations.
 * 
 * @author hendrikebbers
 * 
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
	 * @param key
	 *            the key
	 * @param value
	 *            the object
	 */
	public void register(String key, Object value) {
		registeredObjects.put(key, value);
	}

	/**
	 * Returns a registered object that is registered in the context with the
	 * given key
	 * 
	 * @param key
	 *            the key
	 * @return the registered object
	 */
	public Object getRegisteredObject(String key) {
		return registeredObjects.get(key);
	}

	/**
	 * Returns a registered object that is registered by its' class
	 * 
	 * @param cls
	 *            the class of the object
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	public <T> T getRegisteredObject(Class<T> cls) {
		return (T) registeredObjects.get(cls.toString());
	}

	public void register(Object value) {
		registeredObjects.put(value.getClass().toString(), value);
	}
}
