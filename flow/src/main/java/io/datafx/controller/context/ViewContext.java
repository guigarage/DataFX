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
package io.datafx.controller.context;

import io.datafx.controller.context.event.ContextDestroyedListener;
import io.datafx.core.DataFXUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import io.datafx.controller.ViewConfiguration;

import javax.annotation.PreDestroy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 * The view context is the context with a life time of one view. For each view
 * there is exactly one ViewContext. By using the {@link io.datafx.controller.ViewFactory} to create
 * MVC views a ViewContext will be automatically created.<br>
 * Example:
 * </p>
 * <pre>
 * <code>
 * ViewContext&lt;MyController&gt; context =
 * ViewFactory.getInstance().createByController(MyController.class);<br>
 * </code>
 * </pre>
 * <p>
 * How to use a context<br>
 * A context has a defined life time and can hold the data model of your
 * application or of a part of your application. To do so you can easily register
 * objects to your context:<br>
 * </p>
 * <p>
 * DataModel model = ... <br>
 * context.register(model);<br>
 * <br>
 * In your controller: <br>
 * DataModel model = context.getRegisteredObject(DataModel.class);
 * </p>
 * <p>
 * If you need more than one instance of a class in your context you can simple
 * register the by using string based keys:
 * </p>
 * <p>
 * DataModel model1 = ... <br>
 * DataModel model2 = ... <br>
 * context.register("firstModel", model);<br>
 * context.register("secondModel", model);<br>
 * <br>
 * In your controller: <br>
 * DataModel firstModel = context.getRegisteredObject("firstModel");
 * </p>
 * <p>
 * A context can simple used in a controller by injecting the context. DataFX
 * provides annotations to inject all different context types:
 * </p>
 * <p>
 * "@FXMLApplicationContext" <br>
 * ApplicationContext myApplicationContext;<br>
 * </p>
 * <p>
 * By doing so you can easily access all your data in your controller and share
 * data between different controllers.
 * </p>
 *
 * @param <U> Controller class of the MVC view that is defined in this context
 * @author Hendrik Ebbers
 */
public class ViewContext<U> extends AbstractContext {

    private final Node rootNode;

    private final U controller;

    private final ViewConfiguration configuration;

    private final ViewMetadata metadata;

    private final ObservableList<ContextDestroyedListener<U>> contextDestroyedListeners;

    private ContextResolver<U> resolver;

    /**
     * Create a new ViewContext for a (view-){@link Node} and a controller.
     * Normally this constructor is used by the {@link io.datafx.controller.ViewFactory} and should
     * not be used in application code.
     *
     * @param rootNode   the (view-)node
     * @param controller the controller
     * @param metadata
     * @param configuration
     * @param resources
     */
    public ViewContext(final Node rootNode, final U controller, final ViewMetadata metadata, final ViewConfiguration configuration, final Object... resources) {
        this.rootNode = rootNode;
        this.controller = controller;
        this.configuration = configuration;
        this.metadata = metadata;
        this.contextDestroyedListeners = FXCollections.observableArrayList();

        if (resources != null) {
            for (Object resource : resources) {
                register(resource);
            }
        }
    }

    public ContextResolver<U> getResolver() {
        if (resolver == null) {
            resolver = new ContextResolver<U>(this);
        }
        return resolver;
    }

    /**
     * Returns the controller of the MVC view that is wrapped by this context.
     *
     * @return the controller
     */
    public U getController() {
        return controller;
    }

    /**
     * Returns the JavaFX view node of the MVC view that is wrapped by this
     * context.
     *
     * @return the view node
     */
    public Node getRootNode() {
        return rootNode;
    }

    /**
     * Returns the global application context
     *
     * @return application context
     */
    public ApplicationContext getApplicationContext() {
        return ApplicationContext.getInstance();
    }

    /**
     * Destroyes the context. This will be called by the flow API if a view is
     * closed. Normally an application should never call this.
     *
     * @throws IllegalAccessException    if the context can't be destroyed
     * @throws IllegalArgumentException  if the context can't be destroyed
     * @throws InvocationTargetException if the context can't be destroyed
     */
    public void destroy() throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        // TODO: All managed Object should be checked for a pre destroy....
        if (controller != null) {
            for (final Method method : DataFXUtils.getInheritedDeclaredMethods(getController().getClass())) {
                if (method.isAnnotationPresent(PreDestroy.class)) {
                    DataFXUtils.callPrivileged(method, getController());
                }
            }
        }
        for(ContextDestroyedListener<U> listener : contextDestroyedListeners) {
            listener.contextDestroyed(this);
        }
    }

    public void addContextDestroyedListener(ContextDestroyedListener<U> listener) {
        contextDestroyedListeners.add(listener);
    }

    public void removeContextDestroyedListener(ContextDestroyedListener<U> listener) {
        contextDestroyedListeners.remove(listener);
    }

    public ViewConfiguration getConfiguration() {
        return configuration;
    }

    public ViewMetadata getMetadata() {
        return metadata;
    }
}
