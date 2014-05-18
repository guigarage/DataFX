package org.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.action.ActionMethod;

import javax.annotation.PostConstruct;

/**
 * This is the controller class of the view this is used in this first tutorial. By using the @FXMLController annotation
 * the class defines its FXML file that contains the layout of the view and defines it's UI components.
 * By using the @FXML annotation that is part of the basic JavaFX API components that are defined in the FXML file can be easily
 * injected in the controller. Once the controller is created the init() methods will be called. This method is annoted by the
 * @PostConstruct annotation. By doing so the DataFX flow container will call this method once all injectable values of the controller
 * instance are injected. There three different types of values / fields that can be injected:
 *  - UI components that are annotated by @FXML
 *  - DataFX objects. Here DataFX provides several annotations
 *  - Custom implementations. These will be injected by using the @Inject annotation
 *
 *  The method that is annotated by @PostContruct will be called when all injections are finished.
 *
 *  In this first example we will only use @FXML to inject FXML UI components to the controller.
 *
 *  In addtion DataFX action handling is introduced in this example. The view contains a button named actionButton.
 *  Once this button is clicked the onAction() method should be executed. To do so DataFX contains two annotation:
 *  -  The @ActionTrigger annotation defines a UI component that will trigger an action. Each action in DataFX is defined by a unique id.
 *     In this example the "myAction" id is used. The id must be unique in on view controller.
 *  - To handle the action a method of the controller can be annotated by the @ActionMethod annotation. The value of this annotation
 *    must be the unique id of the defined action. Therefore @ActionMethod("myAction") is used here.
 *    The method will be called in the JavaFX Application Thread
 *
 * As you will see later there are other types of actions than simply calling a method. These will be shown later in other tutorial.
 * For now the most important point is that a component that is annotated with @ActionTrigger can trigger a method that is annotated with
 * @ActionMethod if both annotations define the same unique action id.
 * So once the actionButton in this example is clicked the onAction() method will be called and the text of the label will change.
 *
 */
@FXMLController("simpleView.fxml")
public class SimpleController {

    @FXML
    private Label resultLabel;

    @FXML
    @ActionTrigger("myAction")
    private Button actionButton;

    private int clickCount = 0;

    @PostConstruct
    public void init() {
        resultLabel.setText("Button was clicked " + clickCount + " times");
    }

    @ActionMethod("myAction")
    public void onAction() {
        clickCount++;
        resultLabel.setText("Button was clicked " + clickCount + " times");
    }
}
