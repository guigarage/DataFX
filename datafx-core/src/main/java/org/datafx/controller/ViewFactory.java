package org.datafx.controller;

import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.datafx.controller.context.ViewContext;
import org.datafx.controller.context.ViewMetadata;
import org.datafx.util.DataFXUtils;
import org.datafx.util.ExceptionHandler;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

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
     * {@link FXMLController} annotation to link to the fxml file. You can skip
     * the annotation if you want to use the controller API conventions. By
     * doing so the fxml files has to be in the package as the controller and
     * must fit to a naming convention (see {@link org.datafx.controller.FXMLController} for more
     * informations). The method returns a {@link org.datafx.controller.context.ViewContext}. This is a
     * wrapper around the view (view-node and controller) and can be used to
     * register your datamodel to the view. The doc of {@link org.datafx.controller.context.ViewContext} will
     * provide more information about this topic.
     *
     * @param controllerClass the class of the controller.
     * @return a ViewContext that encapsulate the complete MVC
     * @throws FxmlLoadException if the fxml file can not be loaded
     */
    public <T> ViewContext<T> createByController(final Class<T> controllerClass)
            throws FxmlLoadException {
        return createByController(controllerClass, null);
    }

    /**
     * Creates a new MVC based view by using the given controller class. The
     * class needs a default constructor (no parameters) and a
     * {@link FXMLController} annotation to link to the fxml file. You can skip
     * the annotation if you want to use the controller API conventions. By
     * doing so the fxml files has to be in the package as the controller and
     * must fit to a naming convention (see {@link org.datafx.controller.FXMLController} for more
     * informations). The method returns a {@link org.datafx.controller.context.ViewContext}. This is a
     * wrapper around the view (view-node and controller) and can be used to
     * register your datamodel to the view. The doc of {@link org.datafx.controller.context.ViewContext} will
     * provide more information about this topic. By using this method you can
     * overwrite the path to your fxml file.
     *
     * @param controllerClass the class of the controller.
     * @param fxmlName        path to the fxml file that will be used for the generated MVC
     *                        context
     * @return a ViewContext that encapsulate the complete MVC
     * @throws FxmlLoadException if the fxml file can not be loaded
     */
    public <T> ViewContext<T> createByController(
            final Class<T> controllerClass, String fxmlName)
            throws FxmlLoadException {
        return createByController(controllerClass, fxmlName, new ViewConfiguration());
    }

    public <T> ViewContext<T> createByController(
            final Class<T> controllerClass, String fxmlName, ViewConfiguration viewConfiguration, Object... viewContextResources)
            throws FxmlLoadException {
        try {
            // 1. Create an instance of the Controller
            final T controller = controllerClass.newInstance();
            ViewMetadata metadata = new ViewMetadata();
            FXMLController controllerAnnotation = (FXMLController) controllerClass
                    .getAnnotation(FXMLController.class);
            if (controllerAnnotation != null && !controllerAnnotation.title().isEmpty()) {
                metadata.setTitle(controllerAnnotation.title());
            }
            if (controllerAnnotation != null && !controllerAnnotation.iconPath().isEmpty()) {
                metadata.setGraphic(new ImageView(controllerClass.getResource(controllerAnnotation.iconPath()).toExternalForm()));
            }

            // 2. load the FXML and make sure the @FXML annotations are injected
            DataFXMLLoader loader = createLoader(controller, fxmlName, viewConfiguration);
            ObservableMap<String, Object> map1 = loader.getNamespace();
            Node viewNode = (Node) loader.load();
            ObservableMap<String, Object> map2 = loader.getNamespace();
            ViewContext<T> context = new ViewContext<>(viewNode,
                    controller, metadata, viewConfiguration, viewContextResources);
            context.register(controller);
            context.register("controller", controller);
            // 3. Resolve the @Inject points in the Controller and call
            // @PostConstruct
            context.getResolver().injectResources(controller);

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

    public void showInStage(Stage stage, Class<?> controllerClass) throws FxmlLoadException {
        Scene myScene = new Scene((Parent) createByController(controllerClass).getRootNode());
        stage.setScene(myScene);
        stage.show();
    }

    private DataFXMLLoader createLoader(final Object controller, String fxmlName, ViewConfiguration viewConfiguration)
            throws FxmlLoadException {
        Class<?> controllerClass = controller.getClass();
        String foundFxmlName = getFxmlName(controllerClass);
        if (fxmlName != null) {
            foundFxmlName = fxmlName;
        }
        if (foundFxmlName == null) {
            throw new FxmlLoadException("No FXML File specified!");
        }

        DataFXMLLoader fxmlLoader = new DataFXMLLoader(
                controllerClass.getResource(foundFxmlName));
        fxmlLoader.setBuilderFactory(viewConfiguration.getBuilderFactory());
        fxmlLoader.setCharset(viewConfiguration.getCharset());
        fxmlLoader.setResources(viewConfiguration.getResources());
        fxmlLoader.setController(controller);
        fxmlLoader.setControllerFactory(c -> controller);
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

    public <T> Tab createTab(Class<T> controllerClass) throws FxmlLoadException {
        return createTab(createByController(controllerClass));
    }

    public <T> Tab createTab(Class<T> controllerClass, ExceptionHandler exceptionHandler) throws FxmlLoadException {
        return createTab(createByController(controllerClass), exceptionHandler);
    }

    public <T> Tab createTab(ViewContext<T> context) {
        return createTab(context, ExceptionHandler.getDefaultInstance());
    }

    public <T> Tab createTab(ViewContext<T> context, ExceptionHandler exceptionHandler) {
        Tab tab = new Tab();
        tab.textProperty().bind(context.getMetadata().titleProperty());
        tab.graphicProperty().bind(context.getMetadata().graphicsProperty());
        tab.setOnClosed(e -> {
            try {
                context.destroy();
            } catch (Exception exception) {
                exceptionHandler.setException(exception);
            }
        });
        tab.setContent(context.getRootNode());
        return tab;
    }
}
