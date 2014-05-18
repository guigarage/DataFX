package org.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.controller.flow.action.ActionTrigger;

import javax.annotation.PostConstruct;

/**
 * This is the controller class of the view this is used in this first tutorial. By using the @FXMLController annotation
 * the class defines its FXML file that contains the layout of the view and defines it's UI components.
 * By using the @FXML annotation that is part of the basic JavaFX API components that are defined in the FXML file can be easily
 * injected in the controller. Once the controller is created the init() methods will be called. This method is annoted by the
 *
 * @PostConstruct annotation. By doing so the DataFX flow container will call this method once all injectable values of the controller
 * instance are injected. There three different types of values / fields that can be injected:
 * <ul>
 * <li>UI components that are annotated by @FXML</li>
 * <li>DataFX objects. Here DataFX provides several annotations</li>
 * <li>Custom implementations. These will be injected by using the @Inject annotation</li>
 * </ul>
 * <p/>
 * The method that is annotated by @PostContruct will be called when all injections are finished.
 * <p/>
 * In this first example we will only use @FXML to inject FXML UI components to the controller.
 * <p/>
 * In addtion DataFX action handling is introduced in this example. The view contains a button named actionButton.
 * Once this button is clicked the onAction() method should be executed. To do so DataFX contains two annotation:
 * - The @ActionTrigger annotation defines a UI component that will trigger an action. Each action in DataFX is defined by a unique id.
 * In this example the "myAction" id is used. The id must be unique in on view controller.
 * - To handle the action a method of the controller can be annotated by the @ActionMethod annotation. The value of this annotation
 * must be the unique id of the defined action. Therefore @ActionMethod("myAction") is used here.
 * The method will be called in the JavaFX Application Thread
 * <p/>
 * As you will see later there are other types of actions than simply calling a method. These will be shown later in other tutorial.
 * For now the most important point is that a component that is annotated with @ActionTrigger can trigger a method that is annotated with
 * @ActionMethod if both annotations define the same unique action id.
 * So once the actionButton in this example is clicked the onAction() method will be called and the text of the label will change.
 * <p/>
 * Note: This example is quite easy and normally you could define the action binding by only one line of Java code in the init() method:
 * actionButton.setOnAction((e) -> onAction());
 * So why are these annotations used here?
 * As you will see in further tutorials that are more complex than this one it will make sense to use tha annotation to provide
 * a more readable code.
 */
@FXMLController("simpleView.fxml")
public class SimpleController {

    /**
     * In the FXML file of this view (simpleView.fxml) a Label is defined. This label has the attribute fx:id="resultLabel".
     * The definition of the @FXML annotation defines that the value of a field will be injected if the name of the field is
     * equal to an fx:id that is defined in the FXML file. So the value of this Label field will be injected and once the controller
     * is initialized it will contain the instance of the Label that is defined by the FXML file.
     */
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
