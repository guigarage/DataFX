package org.datafx.controller.context.resource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.datafx.controller.context.ViewContext;

public class AnnotatedResourceManager {
	
	private static AnnotatedResourceManager instance;
	
	private AnnotatedResourceManager() {
	}
	
	public synchronized static AnnotatedResourceManager getInstance() {
		if(instance == null) {
			instance = new AnnotatedResourceManager();
		}
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	public void create(ViewContext<?> context) {
		
		ServiceLoader<AnnotatedControllerResourceType> serviceLoader = ServiceLoader.load(AnnotatedControllerResourceType.class);
		
		//Check if a Annotation is defined with two resourceTypes
		Iterator<AnnotatedControllerResourceType> allResourceTypesIterator = serviceLoader.iterator();
		List<Class<Annotation>> supportedAnnotations = new ArrayList<>();
		List<AnnotatedControllerResourceType> allResourceTypes = new ArrayList<>();
		while(allResourceTypesIterator.hasNext()) {
			AnnotatedControllerResourceType currentResourceType = allResourceTypesIterator.next();
			if(supportedAnnotations.contains(currentResourceType.getSupportedAnnotation())) {
				//TODO: Custom Exception
				throw new RuntimeException("TODO: Annotation wird doppelt belegt");
			}
			supportedAnnotations.add(currentResourceType.getSupportedAnnotation());
			allResourceTypes.add(currentResourceType);
		}
		
		Class<? extends Object> cls = context.getController().getClass();
		Field[] fields = cls.getDeclaredFields();
		for (final Field field : fields) {
			List<Annotation> fieldAnnotations =Arrays.asList(field.getAnnotations());
			if(fieldAnnotations != null && !fieldAnnotations.isEmpty()) {
				boolean injected = false;
				for(AnnotatedControllerResourceType currentResourceType : allResourceTypes) {
					if(field.getAnnotation(currentResourceType.getSupportedAnnotation()) != null) {
						if(injected) {
							//TODO: Custom Exception
							throw new RuntimeException("TODO: double injection of field");
						}
						setPrivileged(field, context.getController(), currentResourceType.create(field.getAnnotation(currentResourceType.getSupportedAnnotation()), context));
						injected = true;
					}
				}
			}
		}
		
	}
	
	//TODO: Method refactoring -> ViewFactory.setPrivileged etc.
	private void setPrivileged(final Field field, final Object bean,
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
}
