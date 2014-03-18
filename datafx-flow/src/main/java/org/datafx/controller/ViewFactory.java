/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.datafx.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.util.Callback;

import javax.annotation.PostConstruct;

import org.datafx.controller.context.ViewContext;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.flow.action.FXMLFlowAction;
import org.datafx.util.DataFXUtils;
import org.datafx.util.PrivilegedReflection;
import org.datafx.controller.util.ViewConfiguration;
import org.datafx.controller.util.FxmlLoadException;


/**
 * <p>
 * Main Class to create MVC based views that use the DataFX controller and flow
 * APIs. By using this class you can simple create a view by only using the
 * class of the controller, for example. The class is designed as a singleton.
 * Here is a short example how to create a view:
 * </p>
 * <p>
 * ViewContext<MyController> context = ViewFactory.getInstance().createByController(MyController.class);<br/>
 * context.getRootNode();<br/>
 * context.getController();<br/>
 * </p>
 * <p>
 * The factory always returns a {@link #ViewContext}. This context is a wrapper
 * around the controller and the view (JavaFX node) that is created by the FXML
 * file (see {@link #FXMLController} for more information).
 * </p>
 * <p>
 * If you want to create a flow you only need a {@link #FlowViewContext} and the
 * factory will create the initial view with all flow actions and links for you:
 * </p>
 * <p>
 * ViewContext<MyController> context
 * ViewFactory.getInstance().createByControllerInViewFlow(MyController.class,
 * myFlowContext);<br/>
 * context.getRootNode();<br/>
 * context.getController();<br/>
 * </p>
 * 
 * @author hendrikebbers
 * 
 */
public class ViewFactory {

	private static ViewFactory instance;

	private ViewFactory() {
	}

	/**
	 * Returns the single instance of the ViewFactory class (singleton pattern)
	 * 
	 * @return the ViewFactory singleton
	 */
	public static synchronized ViewFactory getInstance() {
		if (instance == null) {
			instance = new ViewFactory();
		}
		return instance;
	}

	/**
	 * Creates a new MVC based view by using the given controller class. The
	 * class needs a default constructor (no parameters) and a
	 * {@link #FXMLController} annotation to link to the fxml file. You can skip
	 * the annotation if you want to use the controller API conventions. By
	 * doing so the fxml files has to be in the package as the controller and
	 * must fit to a naming convention (see {@link #FXMLController} for more
	 * informations). The method returns a {@link #ViewContext}. This is a
	 * wrapper around the view (view-node and controller) and can be used to
	 * register your datamodel to the view. The doc of {@link #ViewContext} will
	 * provide more information about this topic.
	 * 
	 * @param controllerClass
	 *            the class of the controller.
	 * @return a ViewContext that encapsulate the complete MVC
	 * @throws FxmlLoadException
	 *             if the fxml file can not be loaded
	 */
	public <T> ViewContext<T> createByController(final Class<T> controllerClass)
			throws FxmlLoadException {
		return createByController(controllerClass, null);
	}

	/**
	 * Creates a new MVC based view by using the given controller class. The
	 * class needs a default constructor (no parameters) and a
	 * {@link #FXMLController} annotation to link to the fxml file. You can skip
	 * the annotation if you want to use the controller API conventions. By
	 * doing so the fxml files has to be in the package as the controller and
	 * must fit to a naming convention (see {@link #org.datafx.controller.FXMLController} for more
	 * informations). The method returns a {@link #org.datafx.controller.context.ViewContext}. This is a
	 * wrapper around the view (view-node and controller) and can be used to
	 * register your datamodel to the view. The doc of {@link #org.datafx.controller.context.ViewContext} will
	 * provide more information about this topic. By using this method you can
	 * overwrite the path to your fxml file.
	 * 
	 * @param controllerClass
	 *            the class of the controller.
	 * @param fxmlName
	 *            path to the fxml file that will be used for the generated MVC
	 *            context
	 * @return a ViewContext that encapsulate the complete MVC
	 * @throws FxmlLoadException
	 *             if the fxml file can not be loaded
	 */
	public <T> ViewContext<T> createByController(
			final Class<T> controllerClass, String fxmlName)
			throws FxmlLoadException {
		return createByControllerInViewFlow(controllerClass,
				new ViewFlowContext(), fxmlName);
	}

	/**
	 * Creates a new MVC based view by using the given controller class. The
	 * class needs a default constructor (no parameters) and a
	 * {@link #FXMLController} annotation to link to the fxml file. You can skip
	 * the annotation if you want to use the controller API conventions. By
	 * doing so the fxml files has to be in the package as the controller and
	 * must fit to a naming convention (see {@link #FXMLController} for more
	 * informations). The method returns a {@link #ViewContext}. This is a
	 * wrapper around the view (view-node and controller) and can be used to
	 * register your datamodel to the view. The doc of {@link #ViewContext} will
	 * provide more information about this topic. By using this method you can
	 * use a predefined {@link #ViewFlowContext}. The created
	 * {@link #ViewContext} will be part of this flow context.
	 * 
	 * @param controllerClass
	 *            the class of the controller.
	 * @param viewFlowContext
	 *            a predefined flow context
	 * @return a ViewContext that encapsulate the complete MVC
	 * @throws FxmlLoadException
	 *             if the fxml file can not be loaded
	 */
	public <T> ViewContext<T> createByControllerInViewFlow(
			final Class<T> controllerClass, ViewFlowContext viewFlowContext)
			throws FxmlLoadException {
		return createByControllerInViewFlow(controllerClass, viewFlowContext,
				null);
	}

	/**
	 * Creates a new MVC based view by using the given controller class. The
	 * class needs a default constructor (no parameters) and a
	 * {@link #FXMLController} annotation to link to the fxml file. You can skip
	 * the annotation if you want to use the controller API conventions. By
	 * doing so the fxml files has to be in the package as the controller and
	 * must fit to a naming convention (see {@link #FXMLController} for more
	 * informations). The method returns a {@link #ViewContext}. This is a
	 * wrapper around the view (view-node and controller) and can be used to
	 * register your datamodel to the view. The doc of {@link #ViewContext} will
	 * provide more information about this topic. By using this method you can
	 * use a predefined {@link #ViewFlowContext} and overwrite the fxml path.
	 * The created {@link #ViewContext} will be part of the given flow context.
	 * 
	 * @param controllerClass
	 *            the class of the controller.
	 * @param viewFlowContext
	 *            a predefined flow context
	 * @param fxmlName
	 *            path to the fxml file that will be used for the generated MVC
	 *            context
	 * @return a ViewContext that encapsulate the complete MVC
	 * @throws FxmlLoadException
	 *             if the fxml file can not be loaded
	 */
	public <T> ViewContext<T> createByControllerInViewFlow(
			final Class<T> controllerClass, ViewFlowContext viewFlowContext,
			String fxmlName) throws FxmlLoadException {
		return createByControllerInViewFlow(controllerClass, viewFlowContext,
				fxmlName, null);
	}

	public <T> ViewContext<T> createByControllerInViewFlow(
			final Class<T> controllerClass, ViewFlowContext viewFlowContext,
			String fxmlName, FlowHandler flowHandler)
			throws FxmlLoadException {
		return createByControllerInViewFlow(controllerClass, viewFlowContext, fxmlName, flowHandler, new ViewConfiguration());
	}

    public <T> ViewContext<T> createByControllerInViewFlow(
            final Class<T> controllerClass, ViewFlowContext viewFlowContext,
            String fxmlName, FlowHandler flowHandler, ViewConfiguration viewConfiguration)
            throws FxmlLoadException {
        try {
            // 1. Create an instance of the Controller
            final T controller = controllerClass.newInstance();

            // 2. load the FXML and make sure the @FXML annotations are injected
            Node viewNode = (Node) createLoader(controller, fxmlName, viewConfiguration).load();
            ViewContext<T> context = new ViewContext<>(viewNode,
                     controller, viewFlowContext);
            context.register(controller);
            context.register("controller", controller);
            // 3. Resolve the @Inject points in the Controller and call
            // @PostConstruct
            context.getResolver().injectResources(controller);

            if (flowHandler != null) {
                injectFlow(controller, flowHandler);
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

	public Node createSubView(Class<?> controllerClass, ViewContext context)
			throws FxmlLoadException {
		return createSubView(controllerClass, null, context);
	}

    public Node createSubView(Class<?> controllerClass, String fxmlName, ViewContext context)
            throws FxmlLoadException {
        return createSubView(controllerClass, fxmlName, context, new ViewConfiguration());
    }

	public Node createSubView(Class<?> controllerClass, String fxmlName,
			ViewContext context, ViewConfiguration viewConfiguration) throws FxmlLoadException {
		try {
			// 1. Create an instance of the Controller
			final Object controller = controllerClass.newInstance();

			// 2. load the FXML and make sure the @FXML annotations are injected
			Node viewNode = (Node) createLoader(controller, fxmlName, viewConfiguration).load();

			// 3. Resolve the @Inject points in the Controller and call
			// @PostConstruct
            context.getResolver().injectResources(controller);

			// TODO: We need to register the sub-controller in the context cause
			// it can have a PreDestroy....

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

	private FXMLLoader createLoader(final Object controller, String fxmlName, ViewConfiguration viewConfiguration)
			throws FxmlLoadException {
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
        fxmlLoader.setBuilderFactory(viewConfiguration.getBuilderFactory());
        fxmlLoader.setCharset(viewConfiguration.getCharset());
        fxmlLoader.setResources(viewConfiguration.getResources());
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
			if (DataFXUtils.canAccess(controllerClass, nameByController)) {
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

	private void injectFlow(final Object controller,
			final FlowHandler flowHandler) {
		Class<? extends Object> cls = controller.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (final Field field : fields) {
			if (field.isAnnotationPresent(FXMLFlowAction.class)) {
				final FXMLFlowAction action = field.getAnnotation(FXMLFlowAction.class);
				Object content = PrivilegedReflection.getPrivileged(field, controller);
				if (content != null) {
					if (content instanceof Button) {
						((Button) content)
								.setOnAction(new EventHandler<ActionEvent>() {

									@Override
									public void handle(ActionEvent event) {
											flowHandler.handle(action.value());

									}
								});
					}
				}
			}
		}
	}

}
