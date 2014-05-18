package org.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.LinkAction;

/**
 * This class defines the controller for the first view of this example. The @FXMLController annotation defines that the
 * view1.fxml file contains the definition of the UI of this view.
 * As descriped in the Tutorial2Main class this example will show how you can navigate between several views in flow. In
 * this example there are only two views that are defined by its controller classes: View1Controller and View2Controller.
 *
 * The only object that is needed in the controller is the instance if the Button that should trigger the navigation action.
 * By using the @FXML annotation the instance is injected in the controller. Whenever this button is pressed the flow should
 * navigate to the second view that is defined by the View2Controller class. DataFX provides the @LinkAction annotation
 * that can be use to define a link / navigation action in a flow. By adding this annotation to a field that is injected
 * with the help of @FXML DataFX will automatically register an action handler on this component. Once the action is triggered
 * the flow will navigate to the defined view. The view can be defined by the value field of the annotation. Here the controller
 * class of the target view must be specified. The @LinkAction can be added to any JavaFX Node. If the component extends the ButtonBase
 * class or the MenuItem class a handler for action events will be added to the control. Otherwise the action will be called once
 * the control is clicked by mouse.
 *
 * By using the annotation a developer doesn't need to handle the complete navigation like changing the view or create a new data model.
 * DataFX will handle all these steps automatically and the defined view will appear on screen once the action is triggered.
 *
 * As said in the first example each action in DataFX is defined by an unique ID. In this case the developer doesn't need to define an
 * ID on its own. DataFX will create a unique ID once the controller will be initialized. By doing so the source code is much shorter and cleaner.
 * As you will see later in the tutorials there are several other ways to define a navigation in a flow. Some of these work with an unique ID
 * as shown in tutorial 1.
 */
@FXMLController("view1.fxml")
public class View1Controller {

    @FXML
    @LinkAction(View2Controller.class)
    private Button actionButton;
}
