package io.datafx.tutorial;

import javafx.application.Application;
import javafx.stage.Stage;
import io.datafx.controller.flow.Flow;

/**
 * Main class for the tutorial. This application will show only a simple wizard in the given {@link Stage}. It's mainly
 * the same wizard as discussed in tutorial 3. But here some new features were added.
 * The views of the wizard are defined by the DataFX controller API and shown in a DataFX flow.
 * As shown in tutorial 1 and 2 the {@link Flow#startInStage(javafx.stage.Stage)} method of the Flow class is used to
 * visualize the wizard on screen.
 * The first view of the wizard is defined by the {@link WizardStartController} class that is passed to the constructor
 * of the {@link Flow} class.
 * In difference to the last tutorials a lot of actions are directly defined in the {@link Flow} definition instead of
 * defining them in the view controller classes. Therefore the {@link Flow} class provides a fluent API that can be used
 * to define actions for single views or the complete flow. All actions that are added to a flow have to implement the
 * {@link io.datafx.controller.flow.action.FlowAction} interface. The {@link Flow} class provides a lot of methods
 * to add specific action types the the flow. Examples are {@link Flow#withGlobalBackAction(String)} or
 *  {@link Flow#withLink(Class, String, Class)}. Some of them are used here to define the flow. As you can see in the
 *  view controller classes of the wizard the actions are bound to controls by using the
 *  {@link io.datafx.controller.flow.action.ActionTrigger} annotation with the unique id of the action that is defined
 *  here. The flow defines two different types of actions: global actions that can be accessed in each view of the flow.
 *  Examples are the "back", "help" and "finish" action in this example. In addition a action can be defined for a
 *  specific view in the flow. To do so the view controller class of the view must be speficied. Examples are the "next"
 *  actions that are added to the {@link WizardStartController}, {@link Wizard1Controller},
 *  {@link Wizard2Controller} and {@link Wizard3Controller} controllers. The {@link WizardDoneController} can't use this
 *  actions, for example.
 */
public class Tutorial4Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Flow(WizardStartController.class).
                withLink(WizardStartController.class, "next", Wizard1Controller.class).
                withLink(Wizard1Controller.class, "next", Wizard2Controller.class).
                withLink(Wizard2Controller.class, "next", Wizard3Controller.class).
                withLink(Wizard3Controller.class, "next", WizardDoneController.class).
                withGlobalBackAction("back").
                withGlobalLink("finish", WizardDoneController.class).
                withGlobalTaskAction("help", () -> System.out.println("There is no help for the application :(")).
                startInStage(primaryStage);
    }
}

