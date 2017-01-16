package io.datafx.ejb;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.commons.lang3.ClassUtils;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EjbLookup {

	private Context context;

	@SuppressWarnings("rawtypes")
	private Hashtable contextEnvironment;

	private Map<Class<?>, String> jndiMapping;

	private String contextId;
	
	private BooleanProperty unwrapEjbException;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EjbLookup(EjbLookupConfiguration configuration) {
		contextEnvironment = new Hashtable();
		contextEnvironment.putAll(configuration.getContextEnvironment());
		contextId = configuration.getContextId();
		
		jndiMapping = new HashMap<Class<?>, String>();
		ServiceLoader<JndiClassBindingProvider> loader = ServiceLoader
				.load(JndiClassBindingProvider.class);
		Iterator<JndiClassBindingProvider> providers = loader.iterator();
		while (providers.hasNext()) {
			JndiClassBindingProvider provider = providers.next();
			if(matchContextId(provider.getContextId())) {
				List<JndiClassBinding<?>> namedBindings = provider.getBindings();
				for (JndiClassBinding<?> binding : namedBindings) {
					add(binding);
				}
			}
		}
	}

	public BooleanProperty unwrapEjbExceptionProperty() {
		if(unwrapEjbException == null) {
			unwrapEjbException = new SimpleBooleanProperty(true);
		}
		return unwrapEjbException;
	}
	
	public boolean matchContextId(String id) {
		if(contextId == null && id == null) {
			return true;
		}
		if(contextId == null && id != null) {
			if(id == "") {
				return true;
			}
			return false;
		}
		if(contextId != null && id == null) {
			if(contextId == "") {
				return true;
			}
			return false;
		}
		return contextId.equals(id);
	}
	
	public void add(JndiClassBinding<?> binding) {
		Class<?> boundClass = binding.getBoundClass();
		String name = binding.getJndiPath();
		if (jndiMapping.containsKey(boundClass)) {
			throw new IllegalArgumentException("Class " + boundClass
					+ " already registered.");
		}
		jndiMapping.put(boundClass, name);
	}

	public <T> T lookup(Class<T> namedObjectClass) throws NamingException {
		return lookupByJndi(getJndiPathForClass(namedObjectClass));
	}

	@SuppressWarnings("unchecked")
	public <T> T lookupByJndi(String path) throws NamingException {
		final T innerInstance = (T) getContext().lookup(path);

		if (innerInstance != null) {
            List<Class<?>> interfaces = ClassUtils.getAllInterfaces(innerInstance.getClass());
			Class<?>[] clsList =  interfaces.toArray(new Class[interfaces.size()]);
			return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
					clsList, new InvocationHandler() {

						public Object invoke(Object proxy, Method method,
								Object[] args) throws Throwable {
							try {
								return method.invoke(innerInstance, args);
							} catch (EJBException e) {
								if(unwrapEjbException.get()) {
									throw e.getCausedByException();
								} else {
									throw e;
								}
							}
						}
					});
		} else {
			return null;
		}
	}
	
	private String getJndiPathForClass(Class<?> namedObjectClass) {
		return jndiMapping.get(namedObjectClass);
	}

	private Context getContext() throws NamingException {
		if (context == null) {
			context = new InitialContext(contextEnvironment);
		}
		return context;
	}
}
