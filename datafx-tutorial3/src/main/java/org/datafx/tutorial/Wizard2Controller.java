package org.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.LinkAction;

@FXMLController(value="wizard2.fxml", title = "Wizard: Step 2")
public class Wizard2Controller extends AbstractWizardController {

    @FXML
    @LinkAction(Wizard3Controller.class)
    private Button nextButton;
}
