package org.datafx.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.datafx.controller.cdi.DependencyInjection;
import org.datafx.controller.flow.FXMLFlowContainer;
import org.datafx.controller.flow.FXMLFlowException;
import org.datafx.controller.flow.FXMLFlowHandler;
import org.datafx.controller.flow.FXMLFlowView;

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

    public ViewContext createByController(final Class<?> controllerClass) throws FxmlLoadException {
        return createByControllerInViewFlow(controllerClass, new ViewFlowContext());
    }

    public ViewContext createByControllerInViewFlow(final Class<?> controllerClass, ViewFlowContext viewFlowContext)
            throws FxmlLoadException {
        Node viewNode = null;
        FXMLController controllerAnnotation = (FXMLController) controllerClass.getAnnotation(FXMLController.class);
        if (controllerAnnotation == null) {
            throw new FxmlLoadException("No FXMLController Annotation present!");
        }
        try {
            // 1. Create an instance of the Controller
            final Object controller = controllerClass.newInstance();
            FXMLLoader fxmlLoader = new FXMLLoader(controllerClass.getResource(controllerAnnotation.value()));
            fxmlLoader.setController(controller);
            fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {

                @Override public Object call(Class<?> arg0) {
                    return controller;
                }
            });
            // 2. load the FXML and make sure the @FXML annotations are injected
            viewNode = (Node) fxmlLoader.load();
            ViewContext context = new ViewContext(viewNode, viewFlowContext);
            // 3. Resolve the @Inject points in the Controller and call @PostConstruct
            injectContexts(controller, context);
            
            if(runtime != null) {
                runtime.resolve(controller, context);
            }
            
            for(final Method method : controller.getClass().getMethods()) {
            	if(method.isAnnotationPresent(PostConstruct.class)) {
            		method.invoke(controller);
            	}
            	//TODO: HACK!!!!!!!!!
            	if(method.isAnnotationPresent(PreDestroy.class)) {
            		viewNode.parentProperty().addListener(new ChangeListener<Parent>() {

						@Override
						public void changed(
								ObservableValue<? extends Parent> observable,
								Parent oldValue, Parent newValue) {
							if(oldValue != null && newValue == null) {
								try {
									method.invoke(controller);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					});
            	}
            	
            }
            
            return context;
        } catch (Exception e) {
            throw new FxmlLoadException(e);
        }
    }

	private void setPrivileged(final Field field, final Object bean, final Object value) {
    	 AccessController.doPrivileged(new PrivilegedAction<Void>() {
             @Override public Void run() {
                 boolean wasAccessible = field.isAccessible();
                 try {
                     field.setAccessible(true);
                     field.set(bean, value);
                     return null; // return nothing...
                 } catch (IllegalArgumentException | IllegalAccessException ex) {
                     throw new IllegalStateException("Cannot set field: " + field, ex);
                 } finally {
                     field.setAccessible(wasAccessible);
                 }
             }
         });
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
                    throw new RuntimeException("Can not set ViewContext to field of type " + type + " (" + field + ")");
                }
            } else if (field.isAnnotationPresent(FXMLViewFlowContext.class)) {
                Class<?> type = field.getType();
                if (FXMLViewFlowContext.class.isAssignableFrom(type)) {
                	setPrivileged(field, bean, context.getViewFlowContext());
                } else {
                    throw new RuntimeException("Can not set FXMLViewFlowContext to field of type " + type + " (" + field + ")");
                }
            } else if (field.isAnnotationPresent(FXMLApplicationContext.class)) {
                Class<?> type = field.getType();
                if (FXMLApplicationContext.class.isAssignableFrom(type)) {
                	setPrivileged(field, bean, context.getApplicationContext());
                } else {
                    throw new RuntimeException("Can not set FXMLViewFlowContext to field of type " + type + " (" + field + ")");
                }
            } else {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    @Override public Void run() {
                        boolean wasAccessible = field.isAccessible();
                        try {
                            field.setAccessible(true);
                            Object fieldData = field.get(bean);
                            if (fieldData != null) {
                                injectContexts(field, context);
                            }
                            return null; // return nothing...
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            throw new IllegalStateException("Cannot set field: " + field, ex);
                        } finally {
                            field.setAccessible(wasAccessible);
                        }
                    }
                });
            }
        }
    }

    public static ViewContext startFlowInPane(FXMLFlowView view, final Pane pane) throws FXMLFlowException {
        FXMLFlowContainer container = new FXMLFlowContainer() {
            
            @Override public void setView(ViewContext context) {
                pane.getChildren().clear();
                pane.getChildren().add(context.getRootNode());
            }
            
            @Override public void flowFinished() {
                
            }
        };
        return new FXMLFlowHandler(view, container).start();
    }
    
}
