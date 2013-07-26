package org.datafx.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import javax.annotation.PostConstruct;

import org.datafx.controller.cdi.DependencyInjection;
import org.datafx.controller.flow.FXMLFlowContainer;
import org.datafx.controller.flow.FXMLFlowException;
import org.datafx.controller.flow.FXMLFlowHandler;
import org.datafx.controller.flow.FXMLFlowView;
import org.datafx.controller.flow.FlowAction;

public class ViewFactory {

	private CDIRuntime runtime;

	private static ViewFactory instance;

	private ViewFactory() {
		runtime = DependencyInjection.getInstance();
	}

	public static synchronized ViewFactory getInstance() {
		if (instance == null) {
			instance = new ViewFactory();
		}
		return instance;
	}

	public ViewContext createByController(final Class<?> controllerClass)
			throws FxmlLoadException {
		return createByController(controllerClass, null);
	}

	public ViewContext createByController(final Class<?> controllerClass,
			String fxmlName) throws FxmlLoadException {
		return createByControllerInViewFlow(controllerClass,
				new ViewFlowContext(), fxmlName);
	}

	public ViewContext createByControllerInViewFlow(
			final Class<?> controllerClass, ViewFlowContext viewFlowContext)
			throws FxmlLoadException {
		return createByControllerInViewFlow(controllerClass, viewFlowContext,
				null);
	}

	public ViewContext createByControllerInViewFlow(
			final Class<?> controllerClass, ViewFlowContext viewFlowContext,
			String fxmlName) throws FxmlLoadException {
		return createByControllerInViewFlow(controllerClass, viewFlowContext,
				fxmlName, null);
	}

	public ViewContext createByControllerInViewFlow(
			final Class<?> controllerClass, ViewFlowContext viewFlowContext,
			String fxmlName, FXMLFlowHandler flowHandler)
			throws FxmlLoadException {
		try {
			// 1. Create an instance of the Controller
			final Object controller = controllerClass.newInstance();
			
			// 2. load the FXML and make sure the @FXML annotations are injected
			Node viewNode = (Node) createLoader(controller, fxmlName).load();
			ViewContext context = new ViewContext(viewNode, viewFlowContext);
			context.register("controller", controller);
			// 3. Resolve the @Inject points in the Controller and call
			// @PostConstruct
			injectContexts(controller, context);

			if (flowHandler != null) {
				injectFlow(controller, flowHandler);
			}

			if (runtime != null) {
				runtime.resolve(controller, context);
			}

			for (final Method method : controller.getClass().getMethods()) {
				if (method.isAnnotationPresent(PostConstruct.class)) {
					method.invoke(controller);
				}
			}
			return context;
		} catch (Exception e) {
			throw new FxmlLoadException(e);
		}
	}

	public Node createSubView(Class<?> controllerClass, ViewContext context) throws FxmlLoadException {
		return createSubView(controllerClass, null, context);
	}
	
	public Node createSubView(Class<?> controllerClass, String fxmlName, ViewContext context) throws FxmlLoadException {
		try {
			// 1. Create an instance of the Controller
			final Object controller = controllerClass.newInstance();
			
			// 2. load the FXML and make sure the @FXML annotations are injected
			Node viewNode = (Node) createLoader(controller, fxmlName).load();

			// 3. Resolve the @Inject points in the Controller and call
			// @PostConstruct
			injectContexts(controller, context);

			//TODO: We need to register the sub-controller in the context cause it can have a PreDestroy....
			
			for (final Method method : controller.getClass().getMethods()) {
				if (method.isAnnotationPresent(PostConstruct.class)) {
					method.invoke(controller);
				}
			}
			return viewNode;
		} catch (Exception e) {
			throw new FxmlLoadException(e);
		}
	}
	
	private FXMLLoader createLoader(final Object controller, String fxmlName) throws FxmlLoadException {
		Class<?> controllerClass = controller.getClass();
		String foundFxmlName = getFxmlName(controllerClass);
		if (fxmlName != null) {
			foundFxmlName = fxmlName;
		}
		if (foundFxmlName == null) {
			throw new FxmlLoadException("No FXML File specified!");
		}
		
		
		FXMLLoader fxmlLoader = new FXMLLoader(
				controllerClass.getResource(foundFxmlName));
		fxmlLoader.setController(controller);
		fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {

			@Override
			public Object call(Class<?> arg0) {
				return controller;
			}
		});
		return fxmlLoader;
	}
	
	private String getFxmlName(Class<?> controllerClass) {
		String foundFxmlName = null;

		if (controllerClass.getSimpleName().endsWith("Controller")) {
			String nameByController = controllerClass.getSimpleName()
					.substring(
							0,
							controllerClass.getSimpleName().length()
									- "Controller".length())
					+ ".fxml";
			if (canAccess(controllerClass, nameByController)) {
				foundFxmlName = nameByController;
			}
		}

		FXMLController controllerAnnotation = (FXMLController) controllerClass
				.getAnnotation(FXMLController.class);
		if (controllerAnnotation != null) {
			foundFxmlName = controllerAnnotation.value();
		}
		return foundFxmlName;
	}
	
	private void injectFlow(final Object controller, final FXMLFlowHandler flowHandler) {
		Class<? extends Object> cls = controller.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (final Field field : fields) {
			if (field.isAnnotationPresent(FlowAction.class)) {
				final FlowAction action = field.getAnnotation(FlowAction.class);
				Object content = getPrivileged(field, controller);
				if(content != null) {
					if(content instanceof Button) {
						((Button) content).setOnAction(new EventHandler<ActionEvent>() {
							
							@Override
							public void handle(ActionEvent event) {
								try {
									flowHandler.handle(action.value());
								} catch (FXMLFlowException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}
				}
			}
		}
	}

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
	
	private Object getPrivileged(final Field field, final Object bean) {
		return AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				boolean wasAccessible = field.isAccessible();
				try {
					field.setAccessible(true);
					return field.get(bean);
				} catch (IllegalArgumentException | IllegalAccessException ex) {
					throw new IllegalStateException("Cannot access field: "
							+ field, ex);
				} finally {
					field.setAccessible(wasAccessible);
				}
			}
		});
	}

	private boolean canAccess(Class<?> controllerClass, String resourceName) {
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

	public <T> T createInstanceWithInjections(Class<T> cls, ViewContext context) throws InstantiationException, IllegalAccessException {
		T instance = cls.newInstance();
		injectContexts(instance, context);
		return instance;
	}
	
	private void injectContexts(final Object bean, final ViewContext context) {
		Class<? extends Object> cls = bean.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (final Field field : fields) {
			if (field.isAnnotationPresent(FXMLViewContext.class)) {
				Class<?> type = field.getType();
				if (ViewContext.class.isAssignableFrom(type)) {
					setPrivileged(field, bean, context);
				} else {
					throw new RuntimeException(
							"Can not set ViewContext to field of type " + type
									+ " (" + field + ")");
				}
			} else if (field.isAnnotationPresent(FXMLViewFlowContext.class)) {
				Class<?> type = field.getType();
				if (ViewFlowContext.class.isAssignableFrom(type)) {
					setPrivileged(field, bean, context.getViewFlowContext());
				} else {
					throw new RuntimeException(
							"Can not set FXMLViewFlowContext to field of type "
									+ type + " (" + field + ")");
				}
			} else if (field.isAnnotationPresent(FXMLApplicationContext.class)) {
				Class<?> type = field.getType();
				if (ApplicationContext.class.isAssignableFrom(type)) {
					setPrivileged(field, bean, context.getApplicationContext());
				} else {
					throw new RuntimeException(
							"Can not set FXMLViewFlowContext to field of type "
									+ type + " (" + field + ")");
				}
			} else {
				// TODO: CHECK FOR RECURSION
				// AccessController.doPrivileged(new PrivilegedAction<Void>() {
				// @Override public Void run() {
				// boolean wasAccessible = field.isAccessible();
				// try {
				// field.setAccessible(true);
				// Object fieldData = field.get(bean);
				// if (fieldData != null) {
				// injectContexts(field, context);
				// }
				// return null; // return nothing...
				// } catch (IllegalArgumentException | IllegalAccessException
				// ex) {
				// throw new IllegalStateException("Cannot set field: " + field,
				// ex);
				// } finally {
				// field.setAccessible(wasAccessible);
				// }
				// }
				// });
			}
		}
	}

	public static ViewContext startFlowInContainer(FXMLFlowView view,
			final FXMLFlowContainer container, ViewFlowContext flowContext)
			throws FXMLFlowException {
		return new FXMLFlowHandler(view, container, flowContext).start();
	}
	
	public static ViewContext startFlowInPane(FXMLFlowView view,
			final Pane pane, ViewFlowContext flowContext)
			throws FXMLFlowException {
		FXMLFlowContainer container = new FXMLFlowContainer() {

			@Override
			public void setView(ViewContext context) {
				pane.getChildren().clear();
				pane.getChildren().add(context.getRootNode());
			}

			@Override
			public void flowFinished() {

			}
		};
		return startFlowInContainer(view, container, flowContext);
	}

	public static ViewContext startFlowInPane(FXMLFlowView view, final Pane pane)
			throws FXMLFlowException {
		return startFlowInPane(view, pane, new ViewFlowContext());
	}

}
