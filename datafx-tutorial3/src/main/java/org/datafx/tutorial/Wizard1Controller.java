package org.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.LinkAction;

@FXMLController(value="wizard1.fxml", title = "Wizard: Step 1")
public class Wizard1Controller extends AbstractWizardController {

    @FXML
    @LinkAction(Wizard2Controller.class)
    private Button nextButton;
}
